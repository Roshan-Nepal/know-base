package com.roshan.know_base.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class SoftDeletableEntity extends AuditedEntity{
    @Column(name = "deleted_by")
    private String deletedBy;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void softDelete(String username){
        if(isDeleted()){
            return;
        }
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }

    public void restore(){
        this.deletedAt = null;
        this.deletedBy = null;
    }

    public boolean isDeleted(){
        return deletedAt != null;
    }
}
