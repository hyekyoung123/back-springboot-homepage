package com.example.demo.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/member/login")
	public  Map<String, Object> loginCheck(@RequestBody MemberDTO memberDTO ) throws Exception {
		System.out.printf("%s %s \n", memberDTO.getId(), memberDTO.getPwd());
		MemberDTO dto = memberService.login(memberDTO);
		boolean check =  dto != null ? true : false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("==================");
		System.out.println(map);
		map.put("userId", memberDTO.getId());
		map.put("check", check);
		if(check) {		  
		    map.put("name", dto.getName());
		}		
		   
		return map;
	}
}
