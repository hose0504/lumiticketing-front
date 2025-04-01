package com.care.boot.member;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MemberController {
    @Autowired private MemberService service;  // ✅ 중복 제거
    @Autowired private HttpSession session;




    @RequestMapping("logout")
    public String logout(RedirectAttributes ra, HttpSession session) {
        session.invalidate(); // 세션 무효화 (로그아웃)
        
        // 로그아웃 메시지를 Flash Attribute로 저장
        ra.addFlashAttribute("logoutMessage", "로그아웃되었습니다!");

        // 홈(index.jsp)으로 이동
        return "redirect:https://login.lumiticketing.click/boot/index";
    }

    @RequestMapping("vipPayment")
    public String vipPayment(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        return "member/vipPayment";
    }

    
    @RequestMapping("ticketingPayment")
    public String ticketingPayment(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        return "member/ticketingPayment";
    }

    
    @PostMapping("ticketingPaymentProc")
    public String ticketingPaymentProc(HttpSession session, RedirectAttributes redirect, Model model) {
    	MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

    	if (loginUser == null) {
    	    redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
    	    return "redirect:/login";
    	}

    	String id = loginUser.getId();


        boolean success = memberService.reserveTicket(id); // ✅ 티켓 예매 로직 실행

        // ✅ 예매 결과 메시지 저장 및 리다이렉트
        if (success) {
            redirect.addFlashAttribute("msg", "🎉 예매 성공!");
        } else {
            redirect.addFlashAttribute("msg", "❌ 예매 실패!");
        }

        return "redirect:/ticketing"; // ✅ 티켓 예매 결과 후 다시 티켓 페이지로 이동
    }



    @PostMapping("vipPaymentProc")
    public String vipPaymentProc(RedirectAttributes ra) {
        String sessionId = (String) session.getAttribute("id");
        if (sessionId == null) {
            ra.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:login";
        }

        String msg = service.upgradeToVIP(sessionId);
        if (msg.equals("VIP 승격 완료!")) {
            session.invalidate();
            ra.addFlashAttribute("vipUpgradeMessage", "🎉 VIP로 승격되었습니다!");  // ✅ 🔥 Flash Attribute 추가
            return "redirect:https://login.lumiticketing.click/boot/index";
        }

        // VIP 승격 실패 시 다시 VIP 결제 페이지로 이동
        ra.addFlashAttribute("msg", "VIP 승격 실패!");;
        return "member/vipPayment";
       }
    
    @RequestMapping("ticketing")
    public String ticketing(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        return "member/ticketing";
    }


    
    @Autowired
    private MemberService memberService; // ✅ 인스턴스 변수로 선언
    
    @PostMapping("/reserveTicket")
    public String reserveTicket(HttpSession session, RedirectAttributes redirect, Model model) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        return "member/ticketingPayment";
    }

    





}
