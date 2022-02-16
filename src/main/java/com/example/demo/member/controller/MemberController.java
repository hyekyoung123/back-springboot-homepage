package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.member.dto.MemberDTO;
import com.example.demo.member.service.MemberService;

@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:3000") //해당 리액트 포트 번호.
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	public MemberController() {
		
	}
	
	@GetMapping("/member/login")
	public  Map<String, Boolean> loginCheck(@RequestBody MemberDTO memberDTO ) throws Exception {
		boolean check = memberService.login(memberDTO) != null ? true : false;
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("login", check);
		return map;
	}
}
