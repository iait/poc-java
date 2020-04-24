package com.iait.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    @Autowired
    private LocalidadService localidadService;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public Optional<ProvinciaEntity> buscarPorId(Long id) {
        return repository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<ProvinciaEntity> buscar() {
        return repository.findAll();
    }
    
    @Transactional
    public ProvinciaEntity alta(String nombre) {
        
        LOG.info("Guardando provincia {}", nombre);
        
        ProvinciaEntity entity = new ProvinciaEntity();
        entity.setNombre(nombre);
        
        return repository.save(entity);
    }
    
    @Transactional
    public Optional<ProvinciaEntity> borrar(Long id) {
        
        LOG.info("Eliminando provincia {}", id);
        
        Optional<ProvinciaEntity> provincia = buscarPorId(id);
        if (provincia.isPresent()) {
            if (!localidadService.buscarPorProvinciaId(id).isEmpty()) {
                throw new RuntimeException(
                        "No se puede eliminar la provincia porque existen localidades que la "
                        + "referencian");
            }
            repository.delete(provincia.get());
        }
        
        return provincia;
    }
}
