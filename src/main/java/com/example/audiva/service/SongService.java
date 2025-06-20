package com.example.audiva.service;

import com.example.audiva.entity.Song;
import com.example.audiva.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

//  Song management
    public List<Song> getAllSong() {
        return songRepository.findAll();
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id"+ id));
    }

    public Song createSong (Song song) {
        return  songRepository.save(song);
    }

//  Còn 1 số thuộc tính chưa update
    public Song updateSong (Long id, Song updateSong) {
        Song existSong = getSongById(id);
        existSong.setTitle(updateSong.getTitle());
        //existSong.setArtist(updateSong.getArtist());
        existSong.setGenre(updateSong.getGenre());
        existSong.setDuration(updateSong.getDuration());
        //existSong.setAudioUrl(updateSong.getAudioUrl());
        return songRepository.save(existSong);
    }

    public void deleteSong (Long id) {
        songRepository.deleteById(id);
    }
}
