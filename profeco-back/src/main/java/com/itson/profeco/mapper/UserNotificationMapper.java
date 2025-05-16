package com.itson.profeco.mapper;

import com.itson.profeco.model.UserNotification;
import com.itson.profeco.api.dto.response.NotificationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserNotificationMapper {

    UserNotificationMapper INSTANCE = Mappers.getMapper(UserNotificationMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "read", target = "read") // "isRead" en DTO, "read" en entity (si es así) o al revés
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "link", target = "link")
    NotificationResponse toResponse(UserNotification userNotification);
}
