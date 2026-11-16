
package com.example.blogdb.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnityWebGLController {

    @GetMapping("/unity-game/Build/{filename}")
    public ResponseEntity<Resource> getUnityFile(
            @PathVariable String filename) throws Exception {

        MediaType contentType;
        boolean isGzip = true;

        // 요청한 파일에 따라 Content-Type 설정
        switch (filename) {

            case "UnityWebGL.loader.js":
                contentType = MediaType.parseMediaType(
                        "application/javascript");
                isGzip = false;
                break;

            case "UnityWebGL.framework.js.gz":
                contentType = MediaType.parseMediaType(
                        "application/javascript");
                break;

            case "UnityWebGL.data.gz":
                contentType = MediaType.APPLICATION_OCTET_STREAM;
                break;

            case "UnityWebGL.wasm.gz":
                contentType = MediaType.parseMediaType(
                        "application/wasm");
                break;

            default:
                return ResponseEntity.notFound().build();
        }

        // Unity 파일 가져오기
        Resource resource = new ClassPathResource(
                "static/unity-game/Build/" + filename);

        // 파일 존재 여부 확인
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // HTTP 응답 헤더 설정
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(contentType);

        // gzip 파일인 경우에만 압축 방식 설정
        if (isGzip) {
            headers.set("Content-Encoding", "gzip");
        }

        headers.setContentLength(resource.contentLength());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
