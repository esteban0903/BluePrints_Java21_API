package edu.eci.arsw.blueprints.filters;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import edu.eci.arsw.blueprints.model.Blueprint;

/**
 * Filtro por defecto: retorna el blueprint sin modificaciones.
 * Útil como referencia o para desactivar la filtración.
 * Perfil Spring: "identity"
 */
@Component
@Profile("identity")
public class IdentityFilter implements BlueprintsFilter {
    @Override
    public Blueprint apply(Blueprint bp) { return bp; }
}
