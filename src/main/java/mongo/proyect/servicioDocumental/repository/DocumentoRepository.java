/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongo.proyect.servicioDocumental.repository;

import java.util.List;
import java.util.Optional;
import mongo.proyect.servicioDocumental.entity.Documento;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends MongoRepository<Documento, ObjectId>{
    
    @Query("{usuario:'?0'}")
    List<Documento> findAutorMisDocumentos(String usuario);
    
    @Query("{$or:[ {$and:[{etiquetas: { $in: [?1]}},{estado:true}]} , {$and:[{etiquetas: {$in: [?1]}} , {usuario:?0}]}]}")
    List<Documento> findEtiqueta(String usuario,List<String> etiquetas);
    
    @Query("{$or:[ {$and:[{etiquetas: { $in: [?1]}},{estado:true},{nombre:{$regex: ?2,$options:'i'}}]} , {$and:[{etiquetas: {$in: [?1]}} , {usuario:?0},{nombre:{$regex: ?2,$options:'i'}}]}]}")
    List<Documento> findEntreEtiquetas(String usuario,List<String> etiquetas, String consulta);
    
    @Query("{nombre:'?0',usuario:'?1'}")
    Optional<Documento> nombreAutor( String nombreDocumento, String usuario);
    
    @Query("{$or:[{ $and:[{nombre:{$regex: ?0,$options:'i'}},{estado:true}]},{ $and:[{usuario:{$regex: ?0,$options:'i'}},{estado:true}]},{usuario:?1}]}")
    List<Documento> findConsulta(String consulta, String usuario);

    @Query("{$and:[{nombre:{$regex: ?0,$options:'i'}},{usuario:?1}]}")
    List<Documento> findConsultaMisDocumentos(String consulta, String usuario);
        
    @Query("{$or:[{'estado':true},{'usuario':'?0'}]}")
    List<Documento> consultaGeneral(String autor);
    
    @Query("{$and:[{etiquetas: { $in: [?1]}},{usuario:?0}]}")
    List<Documento> findEtiquetaMisDocumentos(String usuario,List<String> etiquetas);
    
    @Query("{$and:[{etiquetas: { $in: [?1]}},{usuario:?0},{nombre:{$regex: ?2,$options:'i'}}]}")
    List<Documento> findEntreEtiquetasMisDocumentos(String usuario,List<String> etiquetas, String consulta);
}
