package com.roshan.know_base.document.mapper;

import com.roshan.know_base.common.config.CentralMapperConfig;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public interface DocumentMapper {

    @Mapping(target = "metaData", source = "document.metaData")
    @Mapping(target = "content", source = "text")
    DocumentResponse toResponse(Document document, String text);
    @Mapping(target = "metaData", source = "metaData")
    DocumentResponse toResponse(Document document);
}
