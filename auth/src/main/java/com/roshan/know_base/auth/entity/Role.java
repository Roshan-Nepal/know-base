package com.roshan.know_base.auth.entity;

import com.roshan.know_base.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Entity
public class Role extends BaseEntity {
    private String name;
}
