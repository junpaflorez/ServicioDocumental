/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.service.impl;

import java.util.Optional;
import mongo.proyect.servicioDocumental.dto.UsuarioDTO;
import mongo.proyect.servicioDocumental.entity.Usuario;
import mongo.proyect.servicioDocumental.repository.UsuarioRepository;
import mongo.proyect.servicioDocumental.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author junpa
 */
@Service
public class DefaultUsuarioService implements UsuarioService{
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ModelMapper modelMapper;
    
    @Override
    public UsuarioDTO buscarUsuario(UsuarioDTO usuario) {
        
        Optional<Usuario> usuarioOp = null;
        Usuario auxiliar = null;
        UsuarioDTO usuarioDTO = null;
        usuarioOp = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(usuarioOp.isPresent()){            
            auxiliar = usuarioOp.get();
            if(auxiliar.getPassword().matches(usuario.getPassword())){                
                usuarioDTO = modelMapper.map(auxiliar, UsuarioDTO.class);
            }
        }
        return usuarioDTO;
    }

    @Override
    public boolean tipoUsuario(UsuarioDTO usuario) {
        
        Optional<Usuario> usuarioOp = null;
        UsuarioDTO usuarioDTO = null;
        Usuario auxiliar = null;
        usuarioOp = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(usuarioOp.isPresent()){
            auxiliar = usuarioOp.get();
            usuarioDTO = modelMapper.map(auxiliar,UsuarioDTO.class);
            return usuarioDTO.getTipoUsuario();
        }
        return false;
    }

    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuario) {
        
        Optional<Usuario> usuarioOp = null;
        UsuarioDTO usuarioDTO = null;
        Usuario auxiliar = new Usuario();
        usuarioOp = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(!usuarioOp.isPresent()){
            auxiliar.setNombre(usuario.getNombre());
            auxiliar.setPassword(usuario.getPassword());
            auxiliar.setUsuario(usuario.getUsuario());
            auxiliar.setTipoUsuario(usuario.getTipoUsuario());
            auxiliar = usuarioRepository.save(auxiliar);
            return modelMapper.map(auxiliar, UsuarioDTO.class);
        }
        return null;
    }

    @Override
    public boolean cambiarTipoUsuario(UsuarioDTO usuario) {
        
        Optional<Usuario> usuarioOp = null;
        UsuarioDTO usuarioDTO = null;
        Usuario auxiliar = null;
        usuarioOp = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(usuarioOp.isPresent()){
           auxiliar = usuarioOp.get();
           if(auxiliar.getPassword().matches(usuario.getPassword())){
                auxiliar.setTipoUsuario(true);
                auxiliar = usuarioRepository.save(auxiliar);
                return true;
           }
        }
        return false;
    }

    @Override
    public UsuarioDTO cambiarPasswordUsuario(UsuarioDTO usuario) {
        
        Optional<Usuario> usuarioOp = null;
        UsuarioDTO usuarioDTO = null;
        Usuario auxiliar = null;
        usuarioOp = usuarioRepository.findByUsuario(usuario.getUsuario());
        if(!usuarioOp.isPresent()){
            auxiliar.setPassword(usuario.getPassword());
            auxiliar = usuarioRepository.save(auxiliar);
            return modelMapper.map(auxiliar, UsuarioDTO.class);
        }
        return null;
    }

    @Override
    public UsuarioDTO buscarUsuarioNombre(String nombreUsuario) {
        
        Optional<Usuario> usuario = null;
        if(!nombreUsuario.matches("")){
            usuario = usuarioRepository.findByUsuario(nombreUsuario);
            if(usuario.isPresent()){
                return modelMapper.map(usuario.get(), UsuarioDTO.class);
            }
        }
        return null;
    }
    
}
