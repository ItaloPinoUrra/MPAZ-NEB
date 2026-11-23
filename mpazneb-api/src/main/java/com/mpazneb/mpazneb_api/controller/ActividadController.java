package com.mpazneb.mpazneb_api.controller;

import com.mpazneb.mpazneb_api.entity.Actividad;
import com.mpazneb.mpazneb_api.repository.ActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/actividades")
public class ActividadController {

    @Autowired
    private ActividadRepository actividadRepository;

    // ==================== CRUD ====================

    @GetMapping
    public List<Actividad> listar() {
        return actividadRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actividad> obtenerPorId(@PathVariable Long id) {
        return actividadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Actividad crear(@RequestBody Actividad actividad) {
        actividad.setId(null);
        return actividadRepository.save(actividad);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Actividad> actualizar(@PathVariable Long id,
                                                @RequestBody Actividad detalles) {
        return actividadRepository.findById(id)
                .map(actividad -> {
                    actividad.setNombre(detalles.getNombre());
                    actividad.setModulo(detalles.getModulo());
                    actividad.setCurso(detalles.getCurso());
                    actividad.setSeccion(detalles.getSeccion());
                    actividad.setDificultad(detalles.getDificultad());
                    actividad.setInstrucciones(detalles.getInstrucciones());
                    actividad.setEstado(detalles.getEstado());
                    return ResponseEntity.ok(actividadRepository.save(actividad));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (actividadRepository.existsById(id)) {
            actividadRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ==================== ARCHIVOS ====================

    @PostMapping("/{id}/archivo")
    public ResponseEntity<?> subirArchivo(@PathVariable Long id,
                                          @RequestParam("archivo") MultipartFile archivo) {
        try {
            Actividad actividad = actividadRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));

            if (archivo.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Archivo vacío"));
            }

            String contentType = archivo.getContentType();
            if (contentType == null ||
                    !(contentType.equals("application/pdf")
                            || contentType.equals("image/jpeg")
                            || contentType.equals("image/png"))) {
                return ResponseEntity.badRequest().body(Map.of("error", "Solo se aceptan PDF, JPG o PNG"));
            }

            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String original = archivo.getOriginalFilename() != null ? archivo.getOriginalFilename() : "archivo";
            String extension = original.contains(".") ? original.substring(original.lastIndexOf(".")) : "";
            String nombreGuardado = UUID.randomUUID() + extension;

            Path destino = uploadDir.resolve(nombreGuardado);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            actividad.setArchivoNombre(original);
            actividad.setArchivoRuta("uploads/" + nombreGuardado);
            actividadRepository.save(actividad);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Archivo subido correctamente",
                    "archivoNombre", original,
                    "url", "http://10.0.2.2:8080/uploads/" + nombreGuardado
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/archivo")
    public ResponseEntity<Resource> verArchivo(@PathVariable Long id) {
        try {
            Actividad actividad = actividadRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Actividad no encontrada"));

            if (actividad.getArchivoRuta() == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esta actividad no tiene archivo");
            }

            Path ruta = Paths.get(actividad.getArchivoRuta());
            Resource resource = new UrlResource(ruta.toUri());

            if (!resource.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo no encontrado en disco");
            }

            String contentType = Files.probeContentType(ruta);
            if (contentType == null) contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + actividad.getArchivoNombre() + "\"")
                    .body(resource);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}