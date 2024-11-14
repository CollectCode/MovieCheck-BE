package com.movie.moviecheck.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class WrapperClass<E> {
    private E data;
    private String msg;
}
