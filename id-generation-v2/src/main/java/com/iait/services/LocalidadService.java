package com.iait.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iait.entities.LocalidadEntity;
import com.iait.entities.ProvinciaEntity;
import com.iait.entities.QLocalidadEntity;
import com.iait.entities.pk.LocalidadPkEntity;
import com.iait.repositories.LocalidadRepository;
import com.iait.repositories.ProvinciaRepository;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service
public class LocalidadService {
    
    private static final Logger LOG = LoggerFactory.getLogger(LocalidadService.class);
    
    @Autowired
    private LocalidadRepository repository;
    
    @Autowired
    private ProvinciaRepository provinciaRepository;
    
    @Transactional(readOnly = true)
    public Optional<LocalidadEntity> findById(LocalidadPkEntity pk) {
        return repository.findById(pk);
    }
    
    @Transactional(readOnly = true)
    public List<LocalidadEntity> findAll(
            Function<QLocalidadEntity, BooleanExpression> queryCallback) {
        QLocalidadEntity q = QLocalidadEntity.localidadEntity;
        BooleanExpression exp = queryCallback.apply(q);
        return (List<LocalidadEntity>) repository.findAll(exp);
    }
    
    @Transactional
    public LocalidadEntity create(Long provinciaId, String nombre, long delay) 
            throws InterruptedException {
        
        LOG.info("Guardando localidad {}", nombre);
        
        ProvinciaEntity provincia = provinciaRepository.findById(provinciaId).orElseThrow(
                () -> new RuntimeException("No se encontró la provincia con id " + provinciaId));
        
        if (delay > 0) {
            try {
                LOG.info("X-DELAY {} - Comenzando ....", delay);
                TimeUnit.SECONDS.sleep(delay);
                LOG.info("X-DELAY {} - Terminado!!!", delay);
            } catch (InterruptedException ex) {
                LOG.error("DELAY ERROR: {}", ex);
            } 
        }
        
        LocalidadEntity entity = new LocalidadEntity();
        entity.setNombre(nombre);
        
        entity.setProvincia(provincia);
        
        return repository.save(entity);
    }
    
}
