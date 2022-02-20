package com.example.demo.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.demo.member.vo.MemberVO;


@Repository
@Mapper
public interface MemberDAO {
	 public List selectAllMemberList() throws DataAccessException;
	 public int insertMember(MemberVO memberDTO) throws DataAccessException ;
	
	 public int deleteMember(String id) throws DataAccessException;
	 public MemberVO loginById(MemberVO memberDTO) throws DataAccessException;
}
