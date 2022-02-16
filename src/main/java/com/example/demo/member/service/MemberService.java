package com.example.demo.member.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.member.dto.MemberDTO;

public interface MemberService {
	 public List listMembers() throws DataAccessException;
	 public int addMember(MemberDTO memberVO) throws DataAccessException;
	 public int removeMember(String id) throws DataAccessException;
	 public MemberDTO login(MemberDTO memberVO) throws Exception;
}
