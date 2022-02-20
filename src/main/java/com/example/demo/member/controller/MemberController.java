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

import com.example.demo.member.service.MemberService;
import com.example.demo.member.vo.MemberVO;

@CrossOrigin("*")
//@CrossOrigin(origins = "http://localhost:3000") //해당 리액트 포트 번호.
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	public MemberController() {
		
	}
	
	@PostMapping("/member/login")
	public  Map<String, Object> loginCheck(@RequestBody MemberVO memberDTO ) throws Exception {
		System.out.printf("%s %s \n", memberDTO.getId(), memberDTO.getPwd());
		MemberVO dto = memberService.login(memberDTO);
		boolean success =  dto != null ? true : false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		System.out.println("==================");
		
		map.put("userId", memberDTO.getId());
		map.put("success", success);
		if(success) {		  
		    map.put("name", dto.getName());
		}		
		System.out.println(map);   
		return map;
	}
}
