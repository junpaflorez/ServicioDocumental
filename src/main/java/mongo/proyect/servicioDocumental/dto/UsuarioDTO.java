/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.dto;

import org.bson.types.ObjectId;

/**
 *
 * @author junpa
 */
public class UsuarioDTO {
    private ObjectId id;
    
    private String nombre;
    private String usuario;
    private String password;
    private boolean tipoUsuario;

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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(boolean tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
}
