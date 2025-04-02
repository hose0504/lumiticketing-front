package com.care.boot.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;

import com.care.boot.ticket.TicketService;
import com.care.boot.ticket.TicketHolderDTO;

@Service
public class MemberService {
    @Autowired private IMemberMapper mapper;
    @Autowired private HttpSession session;

    @Autowired
    private TicketService ticketService; // ✅ 티켓 서비스 의존성 주입

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

    public boolean reserveTicket(String id) {
        MemberDTO member = mapper.getMemberById(id);
        if (member == null) return false;

        // ✅ TicketService에서 모든 로직 위임 처리
        return ticketService.reserve(member);
    }
}
