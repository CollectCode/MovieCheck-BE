package com.movie.moviecheck.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    public static String encodeImageToBase64(String imagePath) {
        File imageFile = new File(imagePath);
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] imageBytes = fis.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 이미지 읽기에 실패한 경우 null 반환
        }
    }
}
