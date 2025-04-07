package com.care.boot.ticket;

import com.care.boot.member.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service

package com.care.boot.util;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class EmailService {

    private final SesClient sesClient;

    public EmailService() {
        this.sesClient = SesClient.builder()
                .region(Region.AP_NORTHEAST_2) // 서울 리전
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // 환경변수 기반 인증
                .build();
    }

    public void sendEmail(String toAddress, String subject, String htmlBody) {
        try {
            Destination destination = Destination.builder()
                    .toAddresses(toAddress)
                    .build();

            Content contentSubject = Content.builder()
                    .data(subject)
                    .charset("UTF-8")
                    .build();

            Content contentBody = Content.builder()
                    .data(htmlBody)
                    .charset("UTF-8")
                    .build();

            Body body = Body.builder()
                    .html(contentBody)
                    .build();

            Message message = Message.builder()
                    .subject(contentSubject)
                    .body(body)
                    .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                    .destination(destination)
                    .message(message)
                    .source("your_verified_email@example.com") // ✅ SES에서 인증된 이메일
                    .build();

            sesClient.sendEmail(emailRequest);

            System.out.println("✅ 이메일 전송 성공: " + toAddress);

        } catch (SesException e) {
            System.err.println("❌ 이메일 전송 실패: " + e.awsErrorDetails().errorMessage());
        }
    }
}


