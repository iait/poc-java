package com.iait.payloads.requests;

public class LocalidadRequest {

    private Long provinciaId;
    private String nombre;
    
    public LocalidadRequest() {}
    
    public Long getProvinciaId() {
        return provinciaId;
    }
    
    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
