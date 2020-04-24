package com.iait.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.iait.entities.LocalidadEntity;
import com.iait.entities.ProvinciaEntity;
import com.iait.entities.pk.LocalidadPkEntity;

public interface LocalidadRepository extends 
        JpaRepository<LocalidadEntity, LocalidadPkEntity>, 
        QuerydslPredicateExecutor<LocalidadEntity> {
    
    @Query("SELECT MAX(e.id) FROM LocalidadEntity e WHERE provincia = :provincia")
    public Optional<Long> getMax(@Param("provincia") ProvinciaEntity provincia);
}
