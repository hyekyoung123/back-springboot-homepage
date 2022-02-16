package com.example.demo.member.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.example.demo.member.dto.MemberDTO;


@Repository
@Mapper
public interface MemberDAO {
	 public List selectAllMemberList() throws DataAccessException;
	 public int insertMember(MemberDTO memberDTO) throws DataAccessException ;
	
	 public int deleteMember(String id) throws DataAccessException;
	 public MemberDTO loginById(MemberDTO memberDTO) throws DataAccessException;
}
