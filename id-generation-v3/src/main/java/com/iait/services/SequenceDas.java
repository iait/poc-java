package com.iait.services;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.iait.entities.SequenceEntity;
import com.iait.repositories.SequenceRepository;

@Service
public class SequenceDas {
    
    private static final Logger LOG = LoggerFactory.getLogger(SequenceDas.class);

    @Autowired private SequenceRepository sequenceRepository;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long nextValue(String id, long delay) {
        
        SequenceEntity sequenceEntity = sequenceRepository.findById(id)
                .orElse(new SequenceEntity(id, 0L));
        
        sequenceEntity.setValor(sequenceEntity.getValor() + 1L);
        sequenceRepository.save(sequenceEntity);
        
        if (delay > 0) {
            try {
                LOG.info("X-DELAY {} - Comenzando ....", delay);
                TimeUnit.SECONDS.sleep(delay);
                LOG.info("X-DELAY {} - Terminado!!!", delay);
            } catch (InterruptedException ex) {
                LOG.error("DELAY ERROR: {}", ex);
            } 
        }
        
        return sequenceEntity.getValor();
    }
}
