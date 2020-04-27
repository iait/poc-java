package com.iait.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "provincias")
public class ProvinciaEntity {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "provincias-generator", strategy = GenerationType.TABLE)
    @TableGenerator(name = "provincias-generator", table = "id_gen", pkColumnName = "gen_key", 
            valueColumnName = "gen_value", allocationSize = 1)
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
