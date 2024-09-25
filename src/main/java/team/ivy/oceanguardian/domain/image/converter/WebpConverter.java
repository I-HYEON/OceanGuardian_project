package team.ivy.oceanguardian.domain.image.converter;


import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.image.utils.FileUtils;

public class WebpConverter {

    // WebP로 손실 압축 변환
    public static File convertToWebp(MultipartFile multipartFile, String fileName) throws IOException {
        File originalFile = FileUtils.convertMultipartFileToFile(multipartFile);

        try {
            return ImmutableImage.loader()
                .fromFile(originalFile) // 변환된 File 객체로부터 이미지 로드
                .output(WebpWriter.DEFAULT, new File(System.getProperty("java.io.tmpdir") + File.separator + fileName + ".webp")); // WebP 파일로 변환
        } finally {
            originalFile.delete(); // 임시 파일 삭제
        }
    }

    // WebP로 무손실 압축 변환
    public static File convertToWebpWithLossless(MultipartFile multipartFile, String fileName) throws IOException {
        File originalFile = FileUtils.convertMultipartFileToFile(multipartFile);

        try {
            return ImmutableImage.loader()
                .fromFile(originalFile) // 변환된 File 객체로부터 이미지 로드
                .output(WebpWriter.DEFAULT.withLossless(), new File(System.getProperty("java.io.tmpdir") + File.separator + fileName + ".webp")); // 무손실 WebP 파일로 변환
        } finally {
            originalFile.delete(); // 임시 파일 삭제
        }
    }
}