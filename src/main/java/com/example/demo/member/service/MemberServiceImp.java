package com.example.demo.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.member.dao.MemberDAO;
import com.example.demo.member.vo.MemberVO;


@Service
public class MemberServiceImp implements MemberService {
	
	@Autowired
	private MemberDAO memberDAO;
	
	public MemberServiceImp() {
		
	}

	@Override
	public List listMembers() throws DataAccessException {
		return null;
	}

	@Override
	public int addMember(MemberVO memberVO) throws DataAccessException {
		return 0;
	}

	@Override
	public int removeMember(String id) throws DataAccessException {
		return 0;
	}

	@Override
	public MemberVO login(MemberVO memberVO) throws Exception {
		
		return memberDAO.loginById(memberVO);
	}
	
}
