
package edu.eci.arsw.blueprints.persistence;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 * Repositorio JPA para la entidad Blueprint.
 * Permite consultas personalizadas por autor y nombre.
 */
public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {
    /**
     * Busca todos los blueprints de un autor.
     * @param author Autor
     * @return Set de blueprints
     */
    Set<Blueprint> findByAuthor(String author);

    /**
     * Busca un blueprint por autor y nombre.
     * @param author Autor
     * @param name Nombre del blueprint
     * @return Blueprint encontrado o null si no existe
     */
    Blueprint findByAuthorAndName(String author, String name);
}