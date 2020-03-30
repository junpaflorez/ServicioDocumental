/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.service;

import java.util.List;
import mongo.proyect.servicioDocumental.dto.ArchivoDTO;
import mongo.proyect.servicioDocumental.dto.DocumentoDTO;
import mongo.proyect.servicioDocumental.entity.Etiquetas;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author junpa
 */
public interface DocumentoService {
    
    DocumentoDTO crearDocumento(DocumentoDTO documento);
    
    DocumentoDTO editarDocumento(String usuario,DocumentoDTO documento);
    
    DocumentoDTO eliminarDocumento(DocumentoDTO documento);
    
    DocumentoDTO guardarArchivo(DocumentoDTO documento, ArchivoDTO archivoDTO, MultipartFile file) throws Exception;
    
    DocumentoDTO eliminarArchivo(DocumentoDTO documento,String archivo);
    
    DocumentoDTO actualizarArchivo(DocumentoDTO documento,String archivo, String nombreArchivo);
    
    List<DocumentoDTO> mostrarDocumentos(String usuario,String consulta);
    
    List<DocumentoDTO> misDocumentos(String autor);
    
    List<Etiquetas> etiquetas();
    
    List<DocumentoDTO> consultarEtiquetas(String usuario,List<String> etiquetas);
    
    List<DocumentoDTO> consultarEntreEtiquetas(String usuario,List<String> etiquetas, String consulta);
    
    List<DocumentoDTO> consultarMisDocumentos(String usuario, String consulta);
    
    List<DocumentoDTO> todosLosDocumentos(String usuario);
    
    List<DocumentoDTO> consultarEtiquetasMisDocumentos(String usuario,List<String> etiquetas);
    
    List<DocumentoDTO> consultarEntreEtiquetasMisDocumentos(String usuario,List<String> etiquetas, String consulta);
    
}
