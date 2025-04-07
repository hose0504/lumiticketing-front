package com.care.boot.ticket;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class MailContentBuilder {

    public static String build(String templatePath, Map<String, String> values) {
        try {
            // static 폴더의 mail.html 읽기
        	ClassPathResource resource = new ClassPathResource("static/" + templatePath);
        	String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);


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

