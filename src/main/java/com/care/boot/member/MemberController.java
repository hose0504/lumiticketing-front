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

    // 해외화 로그아웃
    @RequestMapping("logout")
    public String logout(RedirectAttributes ra, HttpSession session) {
        session.invalidate();
        ra.addFlashAttribute("logoutMessage", "\ub85c\uadf8\uc544\uc6c3\ub418\uc5c8\uc2b5\ub2c8\ub2e4!");
        return "redirect:https://login.lumiticketing.click/boot/index";
    }

    // VIP 결제 페이지
    @RequestMapping("vipPayment")
    public String vipPayment(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "\ub85c\uadf8\uc778 \ud6c4 \uc774용\ud574주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }
        return "member/vipPayment";
    }

    // 티켓 예매 페이지
    @RequestMapping("ticketing")
    public String ticketing(HttpSession session, RedirectAttributes redirect, Model model) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "\ub85c\uadf8\uc778 \ud6c4 \uc774용\ud574주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        List<ConcertDTO> concertList = ticketService.getAllConcerts();
        model.addAttribute("concertList", concertList);

        return "member/ticketing";
    }

    // 예매 처리
    @PostMapping("/reserveTicket")
    public String reserveTicket(@RequestParam("concertId") int concertId,
                                HttpSession session,
                                RedirectAttributes redirect) {

        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "\ub85c\uadf8\uc778 \ud6c4 \uc774용\ud574주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        // id 드림으로 DB에서 정보 재확인 (필요시)
        String id = loginUser.getId();
        boolean success = ticketService.reserveTicket(concertId, id);

        if (success) {
            redirect.addFlashAttribute("msg", "\ud83c\udf89 \uc608\ub9e4 \uc131공!");
        } else {
            redirect.addFlashAttribute("msg", "\u274c \uc608\ub9e4 \uc2e4패! \uc88c석이 \ubd80족합니다.");
        }

        return "redirect:/ticketing";
    }

    // VIP 결제 처리
    @PostMapping("vipPaymentProc")
    public String vipPaymentProc(RedirectAttributes ra) {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null) {
            ra.addFlashAttribute("msg", "\ub85c\uadf8\uc778이 \ud544요\ud569니다.");
            return "redirect:login";
        }

        String msg = memberService.upgradeToVIP(sessionId);
        if (msg.equals("VIP \uc2b9격 \uc644료!")) {
            session.invalidate();
            ra.addFlashAttribute("vipUpgradeMessage", "\ud83c\udf89 VIP\ub85c \uc2b9격\ub418\uc5c8습\ub2c8다!");
            return "redirect:https://login.lumiticketing.click/boot/index";
        }

        ra.addFlashAttribute("msg", "VIP \uc2b9격 \uc2e4패!");
        return "member/vipPayment";
    }
}
