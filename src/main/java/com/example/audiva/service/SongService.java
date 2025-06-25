package com.example.audiva.service;

import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Song;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private StorageService storageService;

//  Song management
    public List<Song> getAllSong() {
        return songRepository.findAll();
    }

    public SongResponse getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        return songMapper.toSongResponse(song);
    }

    public Song createSong (SongRequest request) throws IOException {
        MultipartFile audio = request.getAudioFile();
        MultipartFile thumbnail = request.getThumbnailFile();

        String audioPath = storageService.uploadFile(audio);
        String thumbnailPath = storageService.uploadFile(thumbnail);

        Song song = Song.builder()
                .title(request.getTitle())
                .genre(request.getGenre())
                .duration(request.getDuration())
                .audioUrl(audioPath)
                .thumbnailUrl(thumbnailPath)
                .build();

        return songRepository.save(song);
    }

    public Song updateSong (Long id, SongRequest request) {
        Song existSong = songRepository.getSongById(id);
        songMapper.updateSongFromRequest(request, existSong);
        return songRepository.save(existSong);
    }

    public void deleteSong (Long id) {
        songRepository.deleteById(id);
    }
}
