package edu.eci.arsw.blueprints.controllers;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/blueprints")
public class BlueprintsAPIController {

    private final BlueprintsServices services;

    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }


        @Operation(summary = "Obtiene todos los planos", description = "Devuelve todos los blueprints almacenados")
        @ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = edu.eci.arsw.blueprints.model.Blueprint.class)
            )
        )
    public ResponseEntity<ApiResponseDTO<Set<Blueprint>>> getAll() {
        Set<Blueprint> blueprints = services.getAllBlueprints();
        var response = new ApiResponseDTO<>(200, "execute ok", blueprints);
        return ResponseEntity.ok(response);
    }

    // GET /blueprints/{author}
    @Operation(
    summary = "Obtiene los planos por autor",
    description = "Devuelve todos los blueprints de un autor específico"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Consulta exitosa",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = edu.eci.arsw.blueprints.model.Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Autor no encontrado"
    )
    @GetMapping("/{author}")
    public ResponseEntity<?> byAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> blueprints = services.getBlueprintsByAuthor(author);
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(200, "execute ok", blueprints);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // GET /blueprints/{author}/{bpname}
        @Operation(
            summary = "Obtiene un plano por autor y nombre",
            description = "Devuelve un blueprint específico dado el autor y el nombre"
        )
        @ApiResponse(
            responseCode = "200",
            description = "Consulta exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = edu.eci.arsw.blueprints.model.Blueprint.class)
            )
        )
        @ApiResponse(
            responseCode = "404",
            description = "Plano no encontrado"
        )
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponseDTO<Blueprint>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = services.getBlueprint(author, bpname);
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(200, "execute ok", blueprint);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // POST /blueprints
        @Operation(
            summary = "Crea un nuevo plano",
            description = "Agrega un nuevo blueprint al sistema"
        )
        @ApiResponse(
            responseCode = "201",
            description = "Plano creado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = edu.eci.arsw.blueprints.model.Blueprint.class)
            )
        )
        @ApiResponse(
            responseCode = "400",
            description = "Error de persistencia"
        )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Blueprint>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(201, "created", bp);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BlueprintPersistenceException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(400, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // PUT /blueprints/{author}/{bpname}/points
    @Operation(
        summary = "Agrega un punto a un plano existente",
        description = "Añade un nuevo punto a un blueprint específico"
    )
    @ApiResponse(
        responseCode = "202",
        description = "Punto agregado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = edu.eci.arsw.blueprints.model.Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plano no encontrado"
    )
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO(202, "point added", null);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO(404, e.getMessage() , null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}
