package com.iait.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.iait.util.HashKeyGenerator;

@Entity
@Table(name = "provincias")
public class ProvinciaEntity {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "provincia-generator")
    @GenericGenerator(
            name = "provincia-generator", 
            strategy = "com.iait.util.HashKeyGenerator",
            parameters = { 
                    @Parameter(name = HashKeyGenerator.COMPOSITE_KEY, value = "false"),
                    @Parameter(name = HashKeyGenerator.ID_FIELD, value = "id"),
                    @Parameter(name = HashKeyGenerator.USE_HASH, value = "true")})
    private Long id;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    public ProvinciaEntity() {}
    
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
        if (!(obj instanceof ProvinciaEntity)) {
            return false;
        }
        ProvinciaEntity other = (ProvinciaEntity) obj;
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
        return "TestEntity [id=" + id + ", nombre=" + nombre + "]";
    }
}
