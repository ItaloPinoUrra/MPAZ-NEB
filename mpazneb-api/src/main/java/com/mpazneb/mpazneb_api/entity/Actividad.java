package com.mpazneb.mpazneb_api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String modulo; // LECTURA o MATEMATICA

    private Integer curso; // 1, 2, 3, 4

    @Column(name = "seccion", length = 2)
    private String seccion; // "A", "B", "C" — null o vacío = todas las secciones

    private Integer dificultad; // 1, 2, 3

    private String instrucciones;

    private String estado; // DISPONIBLE, DESACTIVADA, BORRADOR

    private String archivoNombre;  // "lectura_patito.pdf"
    private String archivoRuta;    // "uploads/lectura_patito_1234.pdf" (ruta física)

    // Constructores
    public Actividad() {}

    public Actividad(String nombre, String modulo, Integer curso, String seccion,
                     Integer dificultad, String instrucciones, String estado) {
        this.nombre = nombre;
        this.modulo = modulo;
        this.curso = curso;
        this.seccion = seccion;
        this.dificultad = dificultad;
        this.instrucciones = instrucciones;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }

    public Integer getCurso() { return curso; }
    public void setCurso(Integer curso) { this.curso = curso; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    public Integer getDificultad() { return dificultad; }
    public void setDificultad(Integer dificultad) { this.dificultad = dificultad; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getArchivoNombre() { return archivoNombre; }
    public void setArchivoNombre(String archivoNombre) { this.archivoNombre = archivoNombre; }

    public String getArchivoRuta() { return archivoRuta; }
    public void setArchivoRuta(String archivoRuta) { this.archivoRuta = archivoRuta; }

    // Campo derivado (no se guarda en BD, solo se envía en el JSON de respuesta)
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getNombreCurso() {
        if (seccion == null || seccion.isBlank()) {
            return curso + "° Básico";
        }
        return curso + "°" + seccion.toUpperCase();
    }
}