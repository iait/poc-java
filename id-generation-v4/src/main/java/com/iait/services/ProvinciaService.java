package com.iait.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iait.entities.ProvinciaEntity;
import com.iait.repositories.ProvinciaRepository;

@Service
public class ProvinciaService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaService.class);
    
    @Autowired
    private ProvinciaRepository repository;
    
    @Transactional(readOnly = true)
    public List<ProvinciaEntity> findAll() {
        return repository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<ProvinciaEntity> findById(Long id) {
        return repository.findById(id);
    }
    
    public ProvinciaEntity create(String nombre, long delay) throws InterruptedException {
        
        LOG.info("Guardando provincia {}", nombre);
        
        ProvinciaEntity entity = new ProvinciaEntity();
        entity.setNombre(nombre);
        
        return repository.save(entity);
    }
    
}
