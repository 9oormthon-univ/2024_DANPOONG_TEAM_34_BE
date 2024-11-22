package com.goormthom.danpoong.reboot.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.goormthom.danpoong.reboot.constant.Constants;
import com.goormthom.danpoong.reboot.exception.CommonException;
import com.goormthom.danpoong.reboot.exception.ErrorCode;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷

    // S3 파일 업로드
    public String upload(MultipartFile multipartFile) {
        // MultipartFile -> File
        File convertFile;
        try {
            convertFile = convert(multipartFile)
                    .orElseThrow(() -> new CommonException(ErrorCode.FILE_CONVERT_ERROR)); // 파일을 변환할 수 없으면 에러
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        // S3에 저장할 파일명
        String fileName = Constants.dirName + UUID.randomUUID() + "_" + convertFile.getName();

        // S3에 파일 업로드
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, convertFile).withCannedAcl(
                CannedAccessControlList.PublicRead));

        String uploadImageUrl = amazonS3Client.getUrl(bucket, fileName).toString();

        // 로컬 파일 삭제
        convertFile.delete();

        return uploadImageUrl;
    }

    // 파일 convert 후 로컬에 업로드
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());


        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }
}
