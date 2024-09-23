package team.ivy.oceanguardian.domain.image.utils;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {

    public static File convertMultipartFileToFile(MultipartFile file) throws IOException {
        // 임시 파일 생성 (파일명 중복 방지를 위해 UUID 사용)
        File convFile = new File(System.getProperty("java.io.tmpdir") + File.separator + file.getOriginalFilename());
        file.transferTo(convFile); // MultipartFile을 File로 변환
        return convFile;
    }
}