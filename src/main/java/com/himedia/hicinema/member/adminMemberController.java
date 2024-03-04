package com.himedia.hicinema.member;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor  
public class adminMemberController {

	private final MemberService memberService;


	 @GetMapping("/member/memberlist")
	 public String memberlist(Model model, @RequestParam(value="page", defaultValue="0") int page) {
	    Page<Member> paging = this.memberService.getList(page);
	        model.addAttribute("paging", paging);
	        return "admin/member/member_list";
	    }
	
	
}
