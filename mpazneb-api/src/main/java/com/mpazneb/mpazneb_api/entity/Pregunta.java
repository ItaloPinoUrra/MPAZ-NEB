package com.mpazneb.mpazneb_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actividad_id", nullable = false)
    @JsonIgnore
    private Actividad actividad;

    @Column(nullable = false)
    private String enunciado;

    // "ALTERNATIVA" o "DESARROLLO"
    @Column(nullable = false)
    private String tipo = "ALTERNATIVA";

    // Alternativas separadas por "|"  Ej: "Rojo|Verde|Azul|Amarillo"
    @Column(length = 1000)
    private String alternativas;

    @Column(nullable = false)
    private String respuestaCorrecta;

    @Column(length = 1000)
    private String retroalimentacion;

    private Integer orden = 1;

    public Pregunta() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getAlternativas() { return alternativas; }
    public void setAlternativas(String alternativas) { this.alternativas = alternativas; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    // Campo calculado para el JSON de respuesta
    @Transient
    public Long getActividadId() {
        return actividad != null ? actividad.getId() : null;
    }
}