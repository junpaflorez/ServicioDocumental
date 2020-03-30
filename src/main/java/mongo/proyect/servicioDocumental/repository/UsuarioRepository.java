/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.repository;

import java.util.Optional;
import mongo.proyect.servicioDocumental.entity.Usuario;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author junpa
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, ObjectId> {
    
    @Query("{usuario:'?0'},{ unique: true }")
    Optional<Usuario> findByUsuario( String Nombre);
    
}
