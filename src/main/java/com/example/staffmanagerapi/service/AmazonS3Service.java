package com.example.staffmanagerapi.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.staffmanagerapi.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class AmazonS3Service {


    private final AmazonS3 amazonS3;

    public AmazonS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public S3Object download(String bucketName, String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }


    public void upload(MultipartFile multipartFile, String bucketName, String fileName) throws IOException {

        if (multipartFile.isEmpty()) throw new IllegalStateException("Cannot upload empty file");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        amazonS3.putObject(bucketName, fileName, multipartFile.getInputStream(), metadata);

    }

    public boolean bucketNotExistOrEmpty(String bucketName) {
        return !amazonS3.doesBucketExist(bucketName) ||
                amazonS3.listObjects(bucketName).getObjectSummaries().isEmpty();
    }

    public boolean doesFileExists(String bucketName, String fileName){
        return amazonS3.doesObjectExist(bucketName, fileName);
    }


}
