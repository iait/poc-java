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

import com.iait.entities.QLocalidadEntity;
import com.iait.entities.LocalidadEntity;
import com.iait.entities.ProvinciaEntity;
import com.iait.repositories.LocalidadRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Service
public class LocalidadService {
    
    private static final Logger LOG = LoggerFactory.getLogger(LocalidadService.class);
    
    @Autowired
    private LocalidadRepository repository;
    
    @Autowired
    private ProvinciaService provinciaService;
    
    @PersistenceContext
    private EntityManager em;
    
    @Transactional(readOnly = true)
    public Optional<LocalidadEntity> buscarPorId(Long id) {
        return repository.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<LocalidadEntity> buscarPorProvinciaId(Long provinciaId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QLocalidadEntity q = QLocalidadEntity.localidadEntity;
        return queryFactory.selectFrom(q).where(q.provincia.id.eq(provinciaId)).fetch();
    }
    
    @Transactional(readOnly = true)
    public List<LocalidadEntity> buscar() {
        return repository.findAll();
    }
    
    @Transactional
    public LocalidadEntity alta(Long provinciaId, String nombre) {
        
        LOG.info("Guardando localidad {}", nombre);
        
        LocalidadEntity entity = new LocalidadEntity();
        entity.setNombre(nombre);
        ProvinciaEntity provincia = provinciaService.buscarPorId(provinciaId).orElseThrow(
                () -> new RuntimeException("No se encontró la provincia con id " + provinciaId));
        entity.setProvincia(provincia);
        
        return repository.save(entity);
    }
    
    @Transactional
    public Optional<LocalidadEntity> eliminar(Long id) {
        
        LOG.info("Eliminando localidad {}", id);
        
        Optional<LocalidadEntity> localidad = buscarPorId(id);
        if (localidad.isPresent()) {
            repository.delete(localidad.get());
        }
        
        return localidad;
    }
}
