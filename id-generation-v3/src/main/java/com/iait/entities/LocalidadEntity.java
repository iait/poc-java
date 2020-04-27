package com.iait.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.iait.entities.pk.LocalidadPkEntity;
import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;

@Entity
@Table(name = "localidades")
public class LocalidadEntity {
    
    @EmbeddedId
    @QueryType(PropertyType.NONE)
    private LocalidadPkEntity pk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provincia_id", referencedColumnName = "id", 
            nullable = false, insertable = false, updatable = false)
    private ProvinciaEntity provincia;
    
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    public LocalidadEntity() {
        this.pk = new LocalidadPkEntity();
    }
    
    public LocalidadEntity(ProvinciaEntity provincia, Long id) {
        this();
        setProvincia(provincia);
        setId(id);
    }
    
    public LocalidadPkEntity getPk() {
        return pk;
    }

    public ProvinciaEntity getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaEntity provincia) {
        this.provincia = provincia;
        this.pk.setProvinciaId(provincia.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        this.pk.setLocalidadId(id);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
        if (pk == null) {
            if (other.pk != null) {
                return false;
            }
        } else if (!pk.equals(other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LocalidadEntity [pk=" + pk + ", nombre=" + nombre + "]";
    }
}
