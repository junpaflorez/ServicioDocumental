/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.service;

import mongo.proyect.servicioDocumental.dto.UsuarioDTO;

/**
 *
 * @author junpa
 */
public interface UsuarioService {
    
    UsuarioDTO buscarUsuario(UsuarioDTO usuario);
    
    boolean tipoUsuario(UsuarioDTO usuario);
    
    UsuarioDTO crearUsuario(UsuarioDTO usuario);

    boolean cambiarTipoUsuario(UsuarioDTO usuario);
    
    UsuarioDTO cambiarPasswordUsuario(UsuarioDTO usuario);
    
    UsuarioDTO buscarUsuarioNombre(String nombreUsuario);
    
}
