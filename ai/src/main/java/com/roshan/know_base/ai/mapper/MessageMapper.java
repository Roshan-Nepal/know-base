package com.roshan.know_base.ai.mapper;

import com.roshan.know_base.ai.dto.MessageResponse;
import com.roshan.know_base.ai.entity.Message;
import com.roshan.know_base.common.config.CentralMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CentralMapperConfig.class)
public interface MessageMapper {
    MessageResponse toResponse(Message message);
}
