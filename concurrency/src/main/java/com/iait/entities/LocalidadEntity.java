package com.iait.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "localidades")
public class LocalidadEntity {
    
    @Id @Column(name = "id")
    @GenericGenerator(name = "localidades_generator", 
            strategy = "com.iait.generators.CustomGenerator")
    @GeneratedValue(generator = "localidades_generator")
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    private ProvinciaEntity provincia;
    
    public LocalidadEntity() {}
    
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
    
    public ProvinciaEntity getProvincia() {
        return provincia;
    }
    
    public void setProvincia(ProvinciaEntity provincia) {
        this.provincia = provincia;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalidadEntity)) {
            return false;
        }
        LocalidadEntity other = (LocalidadEntity) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LocalidadEntity [id=" + id + ", nombre=" + nombre + ", provinciaId=" 
                + provincia.getId() + "]";
    }
}
