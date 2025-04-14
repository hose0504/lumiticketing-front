package com.care.boot.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.care.boot.ticket.TicketService;
import com.care.boot.ticket.TicketHolderDTO;

import jakarta.servlet.http.HttpSession;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.personalizeruntime.PersonalizeRuntimeClient;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsRequest;
import software.amazon.awssdk.services.personalizeruntime.model.GetRecommendationsResponse;
import software.amazon.awssdk.services.personalizeruntime.model.PredictedItem;

@Service
public class MemberService {
    @Autowired private IMemberMapper mapper;
    @Autowired private HttpSession session;

    @Autowired
    private TicketService ticketService; // ✅ 티켓 서비스 의존성 주입

    // 🎯 VIP 승격 로직
    public String upgradeToVIP(String sessionId) {
        System.out.println("===== [DEBUG] VIP 승격 요청 시작 =====");
        System.out.println("sessionId: " + sessionId);

        MemberDTO member = mapper.login(sessionId);
        if (member == null) {
            System.out.println("🚨 회원 정보 없음!");
            return "회원 정보가 존재하지 않습니다.";
        }

        if ("VIP".equals(member.getMembership())) {
            System.out.println("⚠ 이미 VIP 회원임!");
            return "이미 VIP 회원입니다.";
        }

        int vipCount = mapper.countVIPMembers();
        System.out.println("현재 VIP 회원 수: " + vipCount);

        if (vipCount >= 100) {
            System.out.println("❌ VIP 최대 회원 수 초과!");
            return "VIP 회원이 최대 100명을 초과할 수 없습니다.";
        }

        int result = mapper.promoteToVIP(sessionId, vipCount + 1);
        if (result > 0) {
            mapper.removeFromRegular(sessionId);
            System.out.println("✅ VIP 승격 완료!");
            return "VIP 승격 완료!";
        }

        System.out.println("🚨 VIP 승격 실패!");
        return "VIP 승격에 실패했습니다.";
    }

    // 🎟 예매 요청
    public boolean reserveTicket(String id) {
        MemberDTO member = mapper.getMemberById(id);
        if (member == null) return false;

        // ✅ TicketService에서 모든 로직 위임 처리
        return ticketService.reserve(member);
    }

    // 💡 Personalize로 추천 아이템 ID 가져오기
    public List<String> getRecommendationsForUser(String userId) {
        List<String> recommendedItems = new ArrayList<>();

        try {
            PersonalizeRuntimeClient personalizeClient = PersonalizeRuntimeClient.builder()
                    .region(Region.AP_NORTHEAST_2)
                    .build();

            GetRecommendationsRequest request = GetRecommendationsRequest.builder()
                    .campaignArn("arn:aws:personalize:ap-northeast-2:841162676104:campaign/lumi-realtime-campaign")
                    .userId(userId)
                    .numResults(2)
                    .build();

            GetRecommendationsResponse response = personalizeClient.getRecommendations(request);

            for (PredictedItem item : response.itemList()) {
                recommendedItems.add(item.itemId());
            }

        } catch (Exception e) {
            System.out.println("🔴 Personalize 추천 실패: " + e.getMessage());
        }

        return recommendedItems;
    }

    // 💡 추천 콘서트 하나 랜덤 선택해서 모델에 담기
    public String recommendationForUser(String userId, Model model) {
        List<String> all = getRecommendationsForUser(userId);
        if (all != null && !all.isEmpty()) {
            int randIdx = new Random().nextInt(all.size());
            String picked = all.get(randIdx);
            model.addAttribute("recommendation", picked);
        }
        return "추천 완료";
    }
}
