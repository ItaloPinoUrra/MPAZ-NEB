package com.mpazneb.mpazneb_api.controller;

import com.mpazneb.mpazneb_api.entity.Actividad;
import com.mpazneb.mpazneb_api.entity.Pregunta;
import com.mpazneb.mpazneb_api.repository.ActividadRepository;
import com.mpazneb.mpazneb_api.repository.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PreguntaController {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    // Listar preguntas de una actividad
    @GetMapping("/actividades/{actividadId}/preguntas")
    public List<Pregunta> listarPorActividad(@PathVariable Long actividadId) {
        return preguntaRepository.findByActividad_IdOrderByOrdenAsc(actividadId);
    }

    // Crear pregunta para una actividad
    @PostMapping("/actividades/{actividadId}/preguntas")
    public ResponseEntity<?> crear(@PathVariable Long actividadId, @RequestBody Pregunta pregunta) {
        return actividadRepository.findById(actividadId)
                .map(actividad -> {
                    pregunta.setId(null);
                    pregunta.setActividad(actividad);
                    if (pregunta.getOrden() == null) {
                        pregunta.setOrden((int) preguntaRepository.findByActividad_IdOrderByOrdenAsc(actividadId).size() + 1);
                    }
                    return ResponseEntity.ok(preguntaRepository.save(pregunta));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar pregunta
    @PutMapping("/preguntas/{id}")
    public ResponseEntity<Pregunta> actualizar(@PathVariable Long id, @RequestBody Pregunta detalles) {
        return preguntaRepository.findById(id)
                .map(p -> {
                    p.setEnunciado(detalles.getEnunciado());
                    p.setTipo(detalles.getTipo());
                    p.setAlternativas(detalles.getAlternativas());
                    p.setRespuestaCorrecta(detalles.getRespuestaCorrecta());
                    p.setRetroalimentacion(detalles.getRetroalimentacion());
                    p.setOrden(detalles.getOrden());
                    return ResponseEntity.ok(preguntaRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar pregunta
    @DeleteMapping("/preguntas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (preguntaRepository.existsById(id)) {
            preguntaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}