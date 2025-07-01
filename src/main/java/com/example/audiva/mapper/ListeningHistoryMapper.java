package com.example.audiva.mapper;

import com.example.audiva.dto.response.ListeningHistoryResponse;
import com.example.audiva.entity.ListeningHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SongMapper.class)
public interface ListeningHistoryMapper {

    @Mapping(source = "song", target = "song")
    ListeningHistoryResponse toResponse(ListeningHistory history);
}
