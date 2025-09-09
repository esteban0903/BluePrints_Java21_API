package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface BlueprintRepository extends JpaRepository<Blueprint, Long> {
    Set<Blueprint> findByAuthor(String author);
    Blueprint findByAuthorAndName(String author, String name);
}