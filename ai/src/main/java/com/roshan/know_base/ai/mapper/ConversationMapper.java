package com.roshan.know_base.ai.mapper;

import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.entity.Conversation;
import com.roshan.know_base.common.config.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface ConversationMapper {
    ConversationResponse toResponse(Conversation conversation);
}
