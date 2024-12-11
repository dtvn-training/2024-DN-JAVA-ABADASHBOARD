package com.example.backend.mapper;


import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring", uses = ParameterMapper.class)
public interface TagMapper {

    @Mapping(target = "parameters", source = "parameters")
    TagResponse toTagResponse(Tag tag);

    List<TagResponse> toTagResponseList(List<Tag> tags);
}
