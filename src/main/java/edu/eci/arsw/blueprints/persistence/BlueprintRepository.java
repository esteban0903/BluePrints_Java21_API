package edu.eci.arsw.blueprints.persistence;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.eci.arsw.blueprints.model.Blueprint;

public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {
    Set<Blueprint> findByAuthor(String author);
    Blueprint findByAuthorAndName(String author, String name);

    
}