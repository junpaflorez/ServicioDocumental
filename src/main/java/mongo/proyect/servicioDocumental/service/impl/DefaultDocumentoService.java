/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import mongo.proyect.servicioDocumental.dto.DocumentoDTO;
import mongo.proyect.servicioDocumental.dto.ArchivoDTO;
import mongo.proyect.servicioDocumental.dto.UsuarioDTO;
import mongo.proyect.servicioDocumental.entity.Documento;
import mongo.proyect.servicioDocumental.entity.Etiquetas;
import mongo.proyect.servicioDocumental.repository.DocumentoRepository;
import mongo.proyect.servicioDocumental.service.DocumentoService;
import mongo.proyect.servicioDocumental.service.UsuarioService;
import mongo.proyect.servicioDocumental.storage.FileSystemStorageService;

import net.sourceforge.tess4j.Tesseract;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author junpa
 */
@Service
public class DefaultDocumentoService implements DocumentoService{
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private FileSystemStorageService storageService;
    @Autowired
    private MongoTemplate mongoTemplate;
    
    public static final String origen = "./archivos/";
    public static final String idiomaOCR = "./tessdata";
    
    @Override
    public DocumentoDTO crearDocumento(DocumentoDTO documento) {
        Documento documentoDTO = new Documento();
        Optional<Documento> revisar = null;
        List<ArchivoDTO> archivoDTO = new ArrayList<>();
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        if(propietario != null){
            if(documento!=null){
                revisar = documentoRepository.nombreAutor(documento.getNombre(), documento.getUsuario());
                if(!revisar.isPresent()){
                    documentoDTO.setNombre(documento.getNombre());
                    documentoDTO.setEtiquetas(documento.getEtiquetas());
                    documentoDTO.setDescripcion(documento.getDescripcion());
                    documentoDTO.setEstado(documento.getEstado());
                    documentoDTO.setArchivo(archivoDTO);
                    documentoDTO.setUsuario(documento.getUsuario());
                    documentoDTO = documentoRepository.save(documentoDTO);
                    return modelMapper.map(documentoDTO, DocumentoDTO.class);
                }
            }
        }
        return null;
    }

    @Override
    public DocumentoDTO editarDocumento(String usuario,DocumentoDTO documento) {
        Optional<Documento> documentoDTO = null;
        Documento auxiliar = new Documento();
        Optional<Documento> revisar = null;
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        if(propietario.getUsuario().matches(usuario) || propietario.getTipoUsuario()){
            if(documento!=null){
                revisar = documentoRepository.findById(documento.getId());
                if(revisar.isPresent()){
                    auxiliar = revisar.get();
                    auxiliar.setNombre(documento.getNombre());
                    auxiliar.setDescripcion(documento.getDescripcion());
                    auxiliar.setEstado(documento.getEstado());
                    auxiliar.setEtiquetas(documento.getEtiquetas());
                    auxiliar = documentoRepository.save(auxiliar);
                    return modelMapper.map(auxiliar, DocumentoDTO.class);
                }
            }
        }
        return null;
    }

    @Override
    public DocumentoDTO eliminarDocumento(DocumentoDTO documento) {
        Optional<Documento> documentoDTO = null;
        Documento auxiliar = new Documento();
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        if(documento!=null){
            documentoDTO = documentoRepository.nombreAutor(documento.getNombre(), documento.getUsuario());
            if(documentoDTO.isPresent()){
                auxiliar = documentoDTO.get();
                if(propietario.getUsuario().matches(auxiliar.getUsuario()) || propietario.getTipoUsuario()){
                    File fichero = new File(origen+documento.getUsuario()+"/"+documento.getNombre());
                    deleteFolder(fichero);
                    documentoRepository.delete(auxiliar);
                    return modelMapper.map(auxiliar, DocumentoDTO.class);
                }
            }
        }
        return null;
    }

    @Override
    public DocumentoDTO guardarArchivo(DocumentoDTO documento, ArchivoDTO archivoDTO, MultipartFile file) throws Exception{
        Optional<Documento> documentoDTO = null;
        Documento auxiliar = new Documento();
        List<ArchivoDTO> archivos = new ArrayList<>();
        String direccion = "";
        String ocr = "";
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        boolean bandera = false;
        if(documento!=null && file!=null){
            documentoDTO = documentoRepository.nombreAutor(documento.getNombre(), documento.getUsuario());
            if(documentoDTO.isPresent()){
                auxiliar = documentoDTO.get();
                if(propietario.getUsuario().matches(auxiliar.getUsuario()) || propietario.getTipoUsuario()){
                    archivos = auxiliar.getArchivo();
                    for(ArchivoDTO fil:archivos){
                        if(fil.getNombreArchivo().matches(archivoDTO.getNombreArchivo())){
                            bandera = true;
                        }
                    }
                    if(!bandera){
                        direccion = "/resources/" + documento.getUsuario()+"/"+documento.getNombre()+"/"+archivoDTO.getNombreArchivo();
                        archivoDTO.setURL(direccion);
                        archivos.add(archivoDTO);
                        auxiliar.setArchivo(archivos);
                        storageService.store(file,documento.getUsuario(),documento.getNombre());
                        if(archivoDTO.isOCR()){
                            ocr = OCRFiles(documento,file);
                        }
                        archivoDTO.setTextoCompleto(ocr);
                        auxiliar = documentoRepository.save(auxiliar);
                        return modelMapper.map(auxiliar, DocumentoDTO.class);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public DocumentoDTO eliminarArchivo(DocumentoDTO documento, String archivo) {
        Optional<Documento> documentoDTO = null;
        Documento auxiliar = new Documento();
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        List<ArchivoDTO> archivos = new ArrayList<>();
        if(documento!=null && !archivo.matches("")){
            documentoDTO = documentoRepository.nombreAutor(documento.getNombre(),documento.getUsuario());
            if(documentoDTO.isPresent()){
                auxiliar = documentoDTO.get();
                archivos = auxiliar.getArchivo();
                if(propietario.getUsuario().matches(auxiliar.getUsuario()) || propietario.getTipoUsuario()){
                    int posicion = 0;
                    int puesto = 0;
                    for(ArchivoDTO arc:archivos){
                        
                        if(arc.getURL().matches("/resources/"+archivos)){
                            puesto = posicion;
                        }
                        posicion++;
                    }
                    archivos.remove(puesto);
                    File fichero = new File(origen+archivo);
                    deleteFolder(fichero);
                    auxiliar.setArchivo(archivos);
                    auxiliar = documentoRepository.save(auxiliar);
                    return modelMapper.map(auxiliar, DocumentoDTO.class);
                }
            }
        }
        return null;
    }

    @Override
    public DocumentoDTO actualizarArchivo(DocumentoDTO documento, String archivo, String nombreArchivo) {
        Optional<Documento> documentoDTO = null;
        MultipartFile file = null;
        Documento auxiliar = new Documento();
        UsuarioDTO propietario = null;
        propietario = usuarioService.buscarUsuarioNombre(documento.getUsuario());
        List<ArchivoDTO> archivos = new ArrayList<>();
        if(documento!=null && !archivo.matches("")){
            documentoDTO = documentoRepository.nombreAutor(documento.getNombre(),documento.getUsuario());
            if(documentoDTO.isPresent()){
                if(propietario.getUsuario().matches(documento.getUsuario()) || propietario.getTipoUsuario()){
                    
                    auxiliar = documentoDTO.get();
                    archivos = auxiliar.getArchivo();
                    for(ArchivoDTO arc:archivos){
                        if(arc.getNombreArchivo().matches(archivo)){
                            arc.setNombreArchivo(nombreArchivo);
                        }
                    }
                    auxiliar.setArchivo(archivos);
                    auxiliar = documentoRepository.save(auxiliar);
                    return modelMapper.map(auxiliar, DocumentoDTO.class);
                }
            }
        }
        return null;
    }

    @Override
    public List<DocumentoDTO> mostrarDocumentos(String usuario,String consulta) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos = documentoRepository.findConsulta(consulta,usuario);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                documentosDTO.add(modelMapper.map(documento,DocumentoDTO.class));
            }
            return documentosDTO;
        }
        return null;
    }

    @Override
    public List<DocumentoDTO> todosLosDocumentos(String usuario) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos = documentoRepository.consultaGeneral(usuario);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                documentosDTO.add(modelMapper.map(documento,DocumentoDTO.class));
            }
            return documentosDTO;
        }
        return null;
    }
    
    @Override
    public List<DocumentoDTO> misDocumentos(String autor) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        UsuarioDTO autorDTO = new UsuarioDTO();
        autorDTO = usuarioService.buscarUsuarioNombre(autor);
        if(autorDTO!=null){
            documentos = documentoRepository.findAutorMisDocumentos(autorDTO.getUsuario());
            if(!documentos.isEmpty())
            {
                for(Documento documento: documentos){
                    documentosDTO.add(modelMapper.map(documento, DocumentoDTO.class));
                }
                return documentosDTO;
            }
        }
        return null;
    }

    @Override
    public List<DocumentoDTO> consultarMisDocumentos(String usuario, String consulta) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos = documentoRepository.findConsultaMisDocumentos(consulta,usuario);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                documentosDTO.add(modelMapper.map(documento,DocumentoDTO.class));
            }
            return documentosDTO;
        }
        return null;
    }
    
    public String OCRFiles(DocumentoDTO documento,MultipartFile file) throws Exception{
        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
        String direccion = origen+documento.getUsuario()+"/"+documento.getNombre()+"/";
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(idiomaOCR);
        String resultado = "";
        String resultadoPDF = "";
        File pdfFile = null;
        tesseract.setLanguage("spa");
        BufferedImage img = null;
        if (!"png".equals(ext) && !"jpg".equals(ext)) {
            if("pdf".equals(ext)){
                pdfFile = OCRFilesPDF(documento, file);
                img = ImageIO.read(new File(direccion+"/"+pdfFile.getName()));
                resultado = tesseract.doOCR(img);
                resultadoPDF+= " " + resultado;
                pdfFile.delete();
                return resultadoPDF;
            }
            return resultado;
        }
        try {
            img = ImageIO.read(file.getInputStream());
            resultado = tesseract.doOCR(img);
        } catch (IOException e) {
                throw new Exception("error al leer el archivo");
        }
        return resultado;
    }
    
    public File OCRFilesPDF(DocumentoDTO documento,MultipartFile file) throws Exception{
        try {
            String sourceDir = origen+documento.getUsuario()+"\\"+documento.getNombre()+"\\"+file.getOriginalFilename();
            String destinationDir = origen+documento.getUsuario()+"\\"+documento.getNombre()+"\\";
            File sourceFile = new File(sourceDir);
            File destinationFile = new File(destinationDir);
            File imagenesPDF = null;
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
            }
            if (sourceFile.exists()) {
                PDDocument document = PDDocument.load(sourceDir);
                @SuppressWarnings("unchecked")
                List<PDPage> list = document.getDocumentCatalog().getAllPages();
                String fileName = sourceFile.getName().replace(".pdf", "");
                int pageNumber = 0;
                BufferedImage image = list.get(pageNumber).convertToImage();
                File outputfile = new File(destinationDir + fileName + "_" + pageNumber + ".png");
                ImageIO.write(image, "png", outputfile);
                pageNumber++;
                imagenesPDF = outputfile;
                document.close();
                return imagenesPDF;
            } else {
                    return null;
            }
        } catch (Exception e) {
            throw new Exception("error al leer el archivo");
        }
    }

    @Override
    public List<Etiquetas> etiquetas()
    {
        List<Etiquetas> lista = new ArrayList<>();
        if(documentoRepository.count()>0){
            MapReduceResults<Etiquetas> results = mongoTemplate.mapReduce("documento",
                    "classpath:map.js",
                    "classpath:reduce.js",
                    Etiquetas.class);
       for(Etiquetas eti:results){
                lista.add(eti);
            }
            return lista;
        }
        Etiquetas etiqueta = new Etiquetas();
        etiqueta.setId("");
        etiqueta.setValue(1);
        lista.add(etiqueta);
        return lista;
    }

    @Override
    public List<DocumentoDTO> consultarEtiquetas(String usuario,List<String> etiquetas) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos=documentoRepository.findEtiqueta(usuario,etiquetas);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                    documentosDTO.add(modelMapper.map(documento, DocumentoDTO.class));
                }
                return documentosDTO;
            }
        return null;
    }

    @Override
    public List<DocumentoDTO> consultarEntreEtiquetas(String usuario,List<String> etiquetas, String consulta) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos=documentoRepository.findEntreEtiquetas(usuario,etiquetas,consulta);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                documentosDTO.add(modelMapper.map(documento, DocumentoDTO.class));
            }
            return documentosDTO;
        }
        return null;
    }
    
    @Override
    public List<DocumentoDTO> consultarEtiquetasMisDocumentos(String usuario,List<String> etiquetas) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos=documentoRepository.findEtiquetaMisDocumentos(usuario,etiquetas);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                    documentosDTO.add(modelMapper.map(documento, DocumentoDTO.class));
                }
                return documentosDTO;
            }
        return null;
    }

    @Override
    public List<DocumentoDTO> consultarEntreEtiquetasMisDocumentos(String usuario,List<String> etiquetas, String consulta) {
        List<Documento> documentos = new ArrayList<>();
        List<DocumentoDTO> documentosDTO = new ArrayList<>();
        documentos=documentoRepository.findEntreEtiquetasMisDocumentos(usuario,etiquetas,consulta);
        if(!documentos.isEmpty()){
            for(Documento documento: documentos){
                documentosDTO.add(modelMapper.map(documento, DocumentoDTO.class));
            }
            return documentosDTO;
        }
        return null;
    }
    
    private void deleteFolder(File fileDel) {
        if(fileDel.isDirectory()){
            if(fileDel.list().length == 0)
                fileDel.delete();
            else{
               for (String temp : fileDel.list()) {
                   File fileDelete = new File(fileDel, temp);
                   deleteFolder(fileDelete);
               }
               if(fileDel.list().length==0)
                   fileDel.delete();  
            }
        }else{
            fileDel.delete();            
        }
    }
    
}
