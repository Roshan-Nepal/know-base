package com.roshan.know_base.document.mapper;

import com.roshan.know_base.common.config.CentralMapperConfig;
import com.roshan.know_base.document.dto.DocumentDetailResponse;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.dto.TagResponse;
import com.roshan.know_base.document.entity.Document;
import com.roshan.know_base.document.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = CentralMapperConfig.class)
public interface DocumentMapper {

    @Mapping(target = "content", source = "text")
    @Mapping(target = "tags", source = "document.tags", qualifiedByName = "mapTagNames")
    DocumentDetailResponse toResponse(Document document, String text);
    @Mapping(target = "tags", source = "document.tags", qualifiedByName = "mapTagNames")
    DocumentResponse toResponse(Document document);

    @Named("mapTagNames")
    default Set<TagResponse> mapTags(Set<Tag> tags){
        if(tags == null){
            return Collections.emptySet();
        }
        return tags.stream()
                .map(t -> new TagResponse(t.getId(), t.getName()))
                .collect(Collectors.toSet());
    }
}
