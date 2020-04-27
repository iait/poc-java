package com.iait.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iait.entities.SequenceEntity;

public interface SequenceRepository extends JpaRepository<SequenceEntity, String> {

}
