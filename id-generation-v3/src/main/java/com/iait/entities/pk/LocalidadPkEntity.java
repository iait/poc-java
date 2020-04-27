package com.iait.entities.pk;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LocalidadPkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "provincia_id", nullable = false)
    private Long provinciaId;
    
    @Column(name = "id", nullable = false)
    private Long localidadId;
    
    public LocalidadPkEntity() {
    }

    public Long getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Long provinciaId) {
        this.provinciaId = provinciaId;
    }

    public Long getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(Long localidadId) {
        this.localidadId = localidadId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((localidadId == null) ? 0 : localidadId.hashCode());
        result = prime * result + ((provinciaId == null) ? 0 : provinciaId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocalidadPkEntity)) {
            return false;
        }
        LocalidadPkEntity other = (LocalidadPkEntity) obj;
        if (localidadId == null) {
            if (other.localidadId != null) {
                return false;
            }
        } else if (!localidadId.equals(other.localidadId)) {
            return false;
        }
        if (provinciaId == null) {
            if (other.provinciaId != null) {
                return false;
            }
        } else if (!provinciaId.equals(other.provinciaId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LocalidadPkEntity [provinciaId=" + provinciaId + ", localidadId=" + localidadId 
                + "]";
    }
    
}
