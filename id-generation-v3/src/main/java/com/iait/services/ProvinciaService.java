package com.iait.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iait.entities.ProvinciaEntity;
import com.iait.entities.QProvinciaEntity;
import com.iait.repositories.ProvinciaRepository;
import com.iait.util.SerializationUtils;

@Service
public class ProvinciaService {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaService.class);
    
    @Autowired
    private ProvinciaRepository repository;
    
    @Autowired 
    private SequenceDas sequenceDas;
    
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
        
        QProvinciaEntity q = QProvinciaEntity.provinciaEntity;
        String seqId = SerializationUtils.genKey(q);
        LOG.info("BUSCANDO PARA PROVINCIA-ID (HASH) {}", seqId);
        
        long id = sequenceDas.nextValue(seqId, delay);
        
        ProvinciaEntity entity = new ProvinciaEntity();
        entity.setNombre(nombre);
        
        entity.setId(id);
        LOG.info("MAX ID CALCULADO: {} PARA {}", id, nombre);
        
        return repository.save(entity);
    }
    
}
