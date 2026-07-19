package com.roshan.know_base.document.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "document_contents")
@Getter
@Setter
@NoArgsConstructor
public class DocumentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "document_id")
    private Document document;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
