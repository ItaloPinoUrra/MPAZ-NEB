package com.mpazneb.mpazneb_api.repository;

import com.mpazneb.mpazneb_api.entity.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {

    // Opción 1: nombre con guion bajo (recomendada)
    List<Pregunta> findByActividad_IdOrderByOrdenAsc(Long actividadId);

    // Opción 2 (más explícita): @Query con JPQL
    @Query("SELECT p FROM Pregunta p WHERE p.actividad.id = :actividadId ORDER BY p.orden ASC")
    List<Pregunta> buscarPorActividad(@Param("actividadId") Long actividadId);
}