package com.example.backend.mapper;

import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ParameterMapper.class)
public interface TagMapper {
    @Mapping(target = "parameters", source = "parameterMasters")
    @Mapping(target = "createdAt", source = "createdAt") // Map createdAt from AbstractDefault
    @Mapping(target = "updatedAt", source = "updatedAt") // Map updatedAt from AbstractDefault
    @Mapping(target = "createdBy", source = "createdBy") // Map createdBy from AbstractDefault
    @Mapping(target = "updatedBy", source = "updatedBy") // Map updatedBy from AbstractDefault
    @Mapping(target = "deletedFlag", source = "deletedFlag")
    TagResponse toTagResponse(Tag tag);
    // Map a Tag entity to a TagResponse DTO.

}
