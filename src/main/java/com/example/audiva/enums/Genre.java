package com.example.audiva.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Genre {
    POP,
    ROCK,
    HIPHOP,
    JAZZ,
    EDM,
    CLASSICAL,
    COUNTRY,
    RNB,
    Ballad,
    INDIE,
    RAP,
    OTHER;

    @JsonCreator
    public static Genre fromString(String value) {
        return Genre.valueOf(value.toUpperCase());
    }
}
