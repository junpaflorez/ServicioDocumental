/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.dto;

import com.mongodb.gridfs.GridFSFile;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author junpa
 */
public class DocumentoDTO {
    private ObjectId id;
    private String nombre;
    private List<String> etiquetas;
    private String descripcion;
    private boolean estado;
    private List<ArchivoDTO> archivo;
    
    private String usuario;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<ArchivoDTO> getArchivo() {
        return archivo;
    }

    public void setArchivo(List<ArchivoDTO> archivo) {
        this.archivo = archivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    
}
