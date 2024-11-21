package com.movie.moviecheck.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtil {
    
    // Base64로 변환해주는 메서드
    public static String encodeImageToBase64(String userProfilePath) {
        // 입력 경로 검증
        if (userProfilePath == null || userProfilePath.isEmpty()) {
            return null; // 경로가 없거나 비어있으면 null 반환
        }
    
        // 절대 경로로 변환
        String imagePath = new File("src/main/resources/static" + userProfilePath).getAbsolutePath();
        File imageFile = new File(imagePath);
    
        // 이미지 파일 존재 여부 확인
        if (!imageFile.exists()) {
            System.err.println("파일이 존재하지 않음: " + imagePath);
            return null;
        }
    
        // Base64로 인코딩
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] imageBytes = fis.readAllBytes();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
}

