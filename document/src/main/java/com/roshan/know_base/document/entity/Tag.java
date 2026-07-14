package com.roshan.know_base.document.entity;

import com.roshan.know_base.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "tags")
@Entity
public class Tag extends BaseEntity {

    private String name;

    @Column(name = "user_id")
    private UUID userId;
}
