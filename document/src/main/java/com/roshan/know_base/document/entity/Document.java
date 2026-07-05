package com.roshan.know_base.document.entity;

import com.roshan.know_base.common.entity.AuditedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

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

    @Column(name = "user_id")
    private UUID userId;

    @Version
    private Long version;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_tags",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

}
