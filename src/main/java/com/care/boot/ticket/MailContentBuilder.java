package com.care.boot.ticket;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;

public class MailContentBuilder {

    // 템플릿 파일 경로를 받아 내용을 읽고, 변수들을 치환함
    public static String build(String templatePath, Map<String, String> values) {
        try {
            // 템플릿 파일 읽기
            String content = Files.readString(Paths.get(templatePath), StandardCharsets.UTF_8);

            // ${key} 형태의 변수 치환
            for (Map.Entry<String, String> entry : values.entrySet()) {
                content = content.replace("${" + entry.getKey() + "}", entry.getValue());
            }

            return content;

        } catch (IOException e) {
            e.printStackTrace();
            return "메일 본문 생성 중 오류가 발생했습니다.";
        }
    }
}

