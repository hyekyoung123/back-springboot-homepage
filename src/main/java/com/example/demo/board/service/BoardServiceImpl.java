package com.example.demo.board.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileSystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.board.dao.BoardDAO;
import com.example.demo.board.vo.ArticleVO;



@Service("boardService")
@Transactional(propagation = Propagation.REQUIRED)
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardDAO boardDAO;
	
	public BoardServiceImpl() {
	}
	
	@Override
	public List<ArticleVO> listArticles() throws Exception {
		List<ArticleVO> articleList = boardDAO.selectAllArticlesList();
		return articleList;
	}

	//단일 이미지 추가하기
		@Override
		public int addNewArticle(Map articleMap) throws Exception{
			int articleNO = boardDAO.selectNewArticleNO();
			articleMap.put("articleNO", articleNO);
			boardDAO.insertNewArticle(articleMap);
			return articleNO;			
		}
		
		 //다중 이미지 추가하기
		/*
		@Override
		public int addNewArticle(Map articleMap) throws Exception{
			int articleNO = boardDAO.insertNewArticle(articleMap);
			articleMap.put("articleNO", articleNO);
			boardDAO.insertNewImage(articleMap);
			return articleNO;
		}
		*/
		/*
		//다중 파일 보이기
		@Override
		public Map viewArticle(int articleNO) throws Exception {
			Map articleMap = new HashMap();
			ArticleVO articleVO = boardDAO.selectArticle(articleNO);
			List<ImageVO> imageFileList = boardDAO.selectImageFileList(articleNO);
			articleMap.put("article", articleVO);
			articleMap.put("imageFileList", imageFileList);
			return articleMap;
		}
	   */
		
		
		 //단일 파일 보이기
		@Override
		public ArticleVO viewArticle(int articleNO) throws Exception {
			ArticleVO articleVO = boardDAO.selectArticle(articleNO);			
			System.out.println(articleVO);
			return articleVO;
		}
		
		
		@Override
		public void modArticle(Map articleMap) throws Exception {
			boardDAO.updateArticle(articleMap);
		}
		
		@Override
		public void removeArticle(int articleNO) throws Exception {
			boardDAO.deleteArticle(articleNO);
		}
		
		
		//답글 쓰기
		@Override
		public int replyNewArticle(Map articleMap) throws Exception{
			return boardDAO.insertReplyArticle(articleMap);
		}
	 
}
