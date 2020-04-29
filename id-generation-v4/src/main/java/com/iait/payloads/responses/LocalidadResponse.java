package com.iait.payloads.responses;

import com.iait.entities.LocalidadEntity;

public class LocalidadResponse {
    
    private Long id;
    private String nombre;
    private Long provinciaId;
    
    public LocalidadResponse(LocalidadEntity entity) {
        id = entity.getId();
        nombre = entity.getNombre();
        provinciaId = entity.getProvincia().getId();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Long getProvinciaId() {
        return provinciaId;
    }
    
    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }
}
