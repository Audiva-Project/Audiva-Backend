package com.example.audiva.service;

import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.entity.Song;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongMapper songMapper;

//  Song management
    public List<Song> getAllSong() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id"+ id));
    }

    public Song createSong (SongRequest request) {
        if(songRepository.existsByTitle(request.getTitle())){
            throw new AppException(ErrorCode.SONG_EXISTED);
        }

        Song song = songMapper.toSong(request);

        return  songRepository.save(song);
    }

    public Song updateSong (Long id, SongRequest request) {
        Song existSong = getSongById(id);
        songMapper.updateSongFromRequest(request, existSong);
        return songRepository.save(existSong);
    }

    public void deleteSong (Long id) {
        songRepository.deleteById(id);
    }
}
