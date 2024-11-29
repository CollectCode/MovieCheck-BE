package com.movie.moviecheck.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.movie.moviecheck.controller.WrapperClass;
import com.movie.moviecheck.converter.GenreConvertor;
import com.movie.moviecheck.converter.UserConvertor;
import com.movie.moviecheck.dto.GenreDto;
import com.movie.moviecheck.dto.UserDto;
import com.movie.moviecheck.embedded.UserGenreId;
import com.movie.moviecheck.model.Genre;
import com.movie.moviecheck.model.User;
import com.movie.moviecheck.model.UserGenre;
import com.movie.moviecheck.repository.UserGenreRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGenreService {

    private final UserService userService;
    private final UserConvertor userConvertor;
    private final UserGenreRepository userGenreRepository;
    private final GenreConvertor genreConvertor;
    private final GenreService genreService;

    // 사용자 선호 장르 가져오기
    public ResponseEntity<WrapperClass<List<GenreDto>>> getLikeGenre(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        UserDto user = userConvertor.convertToDto(userService.findByKey(userKey));
        String msg = "장르 Get에 성공하셨습니다!";
        List<GenreDto> genres = getUserGenres(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new WrapperClass<>(genres, msg));
    }

    // 사용자 선호 장르 가져오기
    public ResponseEntity<WrapperClass<List<GenreDto>>> getLikeGenre(Integer userKey) {
        UserDto user = userConvertor.convertToDto(userService.findByKey(userKey));
        String msg = "장르 Get에 성공하셨습니다!";
        List<GenreDto> genres = getUserGenres(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new WrapperClass<>(genres, msg));
    }

    // 사용자가 수정한 장르 적용하기
    public ResponseEntity<WrapperClass<ArrayList<GenreDto>>> setLikeGenre(HttpServletRequest request, GenreDto[] genreDto)  {
        HttpSession session = request.getSession(false);
        Integer userKey = (Integer) session.getAttribute("userKey");
        String msg = "장르 설정에 실패하셨습니다...";

        // 반환할 장르 Dto 배열을 미리 만들기
        ArrayList<GenreDto> genreDtos = new ArrayList<>();

        // 사용자 정보가 유효하지 않을 시
        if (userKey == null) {
            msg = "사용자 정보가 유효하지 않습니다.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(new WrapperClass<>(null, msg));
        }
        
        // userKey로 user찾기
        User user = userService.findByKey(userKey);

        // 사용자가 존재하지 않을 시
        if (user == null) {
            msg = "사용자를 찾을 수 없습니다.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new WrapperClass<>(null, msg));
        }

        // 사용자가 저장해놓은 장르를 전부 지우고 요청받은 장르로 변경
        if (true) {
            deleteUserGenres(user);
            for (GenreDto dto : genreDto) {
                Genre genre = genreConvertor.converToEntity(dto);
                Optional<Genre> gen = genreService.getGenreByName(genre.getGenreName());
                if (gen.isPresent()) {
                    genre = gen.get();
                } else {
                    msg = "장르 '" + genre.getGenreName() + "' 를 찾을 수 없습니다.";
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body(new WrapperClass<>(genreDtos, msg));
                }
                if (setUserGenres(user, genre)) {
                    GenreDto newDto = new GenreDto();
                    newDto.setGenreName(genre.getGenreName());
                    genreDtos.add(newDto);
                }
            }
            msg = "장르 설정에 성공하셨습니다!";
                return ResponseEntity.status(HttpStatus.OK)
                                    .body(new WrapperClass<>(genreDtos, msg));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new WrapperClass<>(genreDtos, msg));
    }

    // 유저가 설정한 장르 적용하기
    public boolean setUserGenres(User user, Genre genre) {
        if(user == null) {
            return false;
        }
        UserGenreId userGenreid = new UserGenreId(user.getUserKey(), genre.getGenreKey());
        UserGenre userGenre = new UserGenre(userGenreid, user, genre);
        userGenreRepository.save(userGenre);

        return true;
    }
    
    // 해당하는 유저의 선호장르를 가져오기
    public List<GenreDto> getUserGenres(UserDto user) {
        List<UserGenre> userGenres = userGenreRepository.findByUser_UserKey(user.getUserKey());
        List<GenreDto> genreDTOs = new ArrayList<>();
        
        for (UserGenre userGenre : userGenres) {
            Genre genre = userGenre.getGenre();
            if (genre != null) {
                genreDTOs.add(new GenreDto(genre.getGenreKey(), genre.getGenreName()));
            }
        }
        return genreDTOs;
    }

    // 유저가 설정해놓은 장르 삭제하기
    public void deleteUserGenres(User user)    {
        userGenreRepository.deleteByUser_UserKey(user.getUserKey());
    }
}
