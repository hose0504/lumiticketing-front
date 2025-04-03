package com.care.boot.member;

import jakarta.servlet.http.HttpSession;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.care.boot.ticket.TicketService;
import com.care.boot.ticket.ConcertDTO;
import com.care.boot.member.MemberDTO;

@Controller
public class MemberController {
    @Autowired private MemberService memberService;
    @Autowired private TicketService ticketService;
    @Autowired private HttpSession session;

    // 🔓 로그아웃
    @RequestMapping("logout")
    public String logout(RedirectAttributes ra, HttpSession session) {
        session.invalidate();
        ra.addFlashAttribute("logoutMessage", "로그아웃되었습니다!");
        return "redirect:https://login.lumiticketing.click/boot/index";
    }

    // 💳 VIP 결제 페이지
    @RequestMapping("vipPayment")
    public String vipPayment(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }
        return "member/vipPayment";
    }

    // 🎫 티켓 예매 페이지 (드롭다운 있는 화면)
    @RequestMapping("ticketing")
    public String ticketing(HttpSession session, RedirectAttributes redirect, Model model) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        List<ConcertDTO> concertList = ticketService.getAllConcerts();
        model.addAttribute("concertList", concertList);

        return "member/ticketing";
    }

    // ✅ 예매 처리
    @PostMapping("/reserveTicket")
    public String reserveTicket(@RequestParam("concertId") int concertId,
                                HttpSession session,
                                RedirectAttributes redirect) {

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        String id = loginUser.getId();
        boolean success = ticketService.reserveTicket(concertId, id);

        if (success) {
            redirect.addFlashAttribute("msg", "🎉 예매 성공!");
        } else {
            redirect.addFlashAttribute("msg", "❌ 예매 실패! 좌석이 부족합니다.");
        }

        return "redirect:/ticketing";
    }

    // 💳 VIP 결제 처리
    @PostMapping("vipPaymentProc")
    public String vipPaymentProc(RedirectAttributes ra) {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null) {
            ra.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:login";
        }

        String msg = memberService.upgradeToVIP(sessionId);
        if (msg.equals("VIP 승격 완료!")) {
            session.invalidate();
            ra.addFlashAttribute("vipUpgradeMessage", "🎉 VIP로 승격되었습니다!");
            return "redirect:https://login.lumiticketing.click/boot/index";
        }

        ra.addFlashAttribute("msg", "VIP 승격 실패!");
        return "member/vipPayment";
    }
}
