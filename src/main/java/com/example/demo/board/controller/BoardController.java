package com.example.demo.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.board.vo.ArticleVO;


public interface BoardController {
	
	public List<ArticleVO> listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String addFormArticle(HttpServletRequest request,HttpServletResponse response) throws Exception;
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest,HttpServletResponse response) throws Exception;
	
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
			                        HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  HttpServletResponse response) throws Exception;
	public ResponseEntity  removeArticle(@RequestParam("articleNO") int articleNO,
                              HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public String replyFormArticle(HttpServletRequest request,HttpServletResponse response) throws Exception;
	public ResponseEntity replyNewArticle(MultipartHttpServletRequest multipartRequest,HttpServletResponse response) throws Exception;
	
}
