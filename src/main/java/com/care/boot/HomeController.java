package com.care.boot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.care.boot.member.MemberDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
    @RequestMapping("index")
    public String index(HttpSession session, RedirectAttributes redirect) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");

        if (loginUser == null) {
            redirect.addFlashAttribute("msg", "로그인 후 이용해주세요!");
            return "redirect:https://login.lumiticketing.click/boot/login";
        }

        return "jsp/index";   // 너희 index.jsp 경로 확인 필요
    }
	
	@RequestMapping("header")
	public String header() {
		return "default/header";
	}
	@RequestMapping("main")
	public String main() {
		return "default/main";
	}
	@RequestMapping("footer")
	public String footer() {
		return "default/footer";
	}
}
