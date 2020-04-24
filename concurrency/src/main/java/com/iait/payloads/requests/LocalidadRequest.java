package com.iait.payloads.requests;

public class LocalidadRequest {
    
    private String nombre;
    private Long provinciaId;
    
    public LocalidadRequest() {}
    
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
