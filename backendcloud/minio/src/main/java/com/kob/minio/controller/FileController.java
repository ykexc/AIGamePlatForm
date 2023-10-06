package com.kob.minio.controller;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author mqz
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final MinioClient minioClient;

    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        String name = UUID.randomUUID().toString();
        PutObjectArgs args = PutObjectArgs.
                builder().
                bucket("test").
                object("upload/" + name).
                stream(inputStream, file.getSize(), -1).
                build();
        minioClient.putObject(args);
        inputStream.close();
        return name;
    }

    @GetMapping("file/{name}")
    public void file(@PathVariable("name") String name, HttpServletResponse response) throws Exception {
        GetObjectResponse inputStream = minioClient.getObject(GetObjectArgs.builder().bucket("test").object("upload/" + name).build());
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }

}
