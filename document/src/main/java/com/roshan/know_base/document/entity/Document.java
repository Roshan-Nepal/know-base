package com.roshan.know_base.document.entity;

import com.roshan.know_base.common.entity.AuditedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "documents")
@Entity
@Builder
public class Document extends AuditedEntity {
    private String name;
    private String storage;
    @Enumerated(EnumType.STRING)
    private DocumentType type;
    private Long fileSize;
    private String language;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metaData;

    @Version
    private Long version;

}
