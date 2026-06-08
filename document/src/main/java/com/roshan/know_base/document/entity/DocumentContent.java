package com.roshan.know_base.document.entity;

import com.roshan.know_base.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "document_contents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentContent extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
