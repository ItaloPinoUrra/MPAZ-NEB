package com.mpazneb.mpazneb_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String alias;

    @NotBlank
    private String clave; // Para el MVP se guarda como SHA-256

    @NotBlank
    private String rol; // ADMIN, ASISTENTE, ESTUDIANTE

    private String estado = "ACTIVO";

    public Usuario() {}

    public Usuario(String alias, String clave, String rol) {
        this.alias = alias;
        this.clave = clave;
        this.rol = rol;
        this.estado = "ACTIVO";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}