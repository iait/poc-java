package com.iait.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.iait.entities.ProvinciaEntity;

public interface ProvinciaRepository extends 
        JpaRepository<ProvinciaEntity, Long>, QuerydslPredicateExecutor<ProvinciaEntity> {
    
}
