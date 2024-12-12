package com.example.backend.mapper;

import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ParameterMapper.class)
public interface TagMapper {
    @Mapping(target = "parameters", source = "parameterMasters")
    TagResponse toTagResponse(Tag tag);
    // Map a Tag entity to a TagResponse DTO.

}
