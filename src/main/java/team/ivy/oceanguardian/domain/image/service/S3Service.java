package team.ivy.oceanguardian.domain.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.IOException;
//import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.ivy.oceanguardian.domain.image.converter.WebpConverter;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String type, String serialNumber) throws IOException {
        // 파일 이름 생성
        String fileName = type + serialNumber;

        // WebP로 변환된 파일 생성
        File webpFile = WebpConverter.convertToWebp(file, fileName);

        try {
            // ObjectMetadata 생성
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/webp"); // ContentType을 'image/webp'로 설정

            amazonS3.putObject(new PutObjectRequest(bucketName, webpFile.getName(), webpFile)
                .withCannedAcl(CannedAccessControlList.PublicRead)
                .withMetadata(metadata));

            log.info("s3에 업로드 성공: {}", webpFile.getName());

            return amazonS3.getUrl(bucketName, fileName).toString(); // 업로드 된 파일의 URL 반환
        } finally {
            webpFile.delete();  //변환된 임시 webpFile 삭제
        }
    }

    public void deleteFile(String fileName) {
        try {
            amazonS3.deleteObject(bucketName, fileName);
            log.info("s3에서 파일 삭제 성공: {}", fileName);
        } catch (AmazonServiceException e) {
            log.error("파일 삭제 실패: {}", e.getMessage(), e);
            throw e;
        }
    }
}
