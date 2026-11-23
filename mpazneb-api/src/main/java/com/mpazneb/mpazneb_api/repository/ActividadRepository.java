package com.mpazneb.mpazneb_api.repository;

import com.mpazneb.mpazneb_api.entity.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
}