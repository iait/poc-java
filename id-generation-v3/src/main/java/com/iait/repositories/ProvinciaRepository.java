package com.iait.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.iait.entities.ProvinciaEntity;

public interface ProvinciaRepository extends 
        JpaRepository<ProvinciaEntity, Long>, QuerydslPredicateExecutor<ProvinciaEntity> {
    
    @Query("SELECT MAX(e.id) FROM ProvinciaEntity e")
    public Optional<Long> getMax();
    
}
