package com.example.demo.board.controller;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.board.service.BoardService;
import com.example.demo.board.vo.ArticleVO;
import com.example.demo.member.vo.MemberVO;

@CrossOrigin("*")
@RestController("boardController")
//@Controller("boardController")
public class BoardControllerImpl implements BoardController {
	private static final String ARTICLE_IMAGE_REPO = "C:\\spring\\board\\article_image";
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;

	public BoardControllerImpl() {
	}

	@Override
	// @RequestMapping(value="/board/listArticles", method = {RequestMethod.GET,
	// RequestMethod.POST})
	// @ResponseBody
	@GetMapping(value = "/board/listArticles")
	@PostMapping(value = "/board/listArticles")
	public List<ArticleVO> listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {

		List<ArticleVO> articlesList = boardService.listArticles();
		System.out.println(articlesList);
		return articlesList;
	}

	/*
	 * @RequestMapping(value = "/board/*Form.do", method = {RequestMethod.GET,
	 * RequestMethod.POST}) private ModelAndView form(HttpServletRequest request,
	 * HttpServletResponse response) throws Exception { String viewName = (String)
	 * request.getAttribute("viewName"); ModelAndView mav = new ModelAndView();
	 * mav.setViewName(viewName); return mav; }
	 */

	@RequestMapping(value = "/board/articleForm.do", method = RequestMethod.GET)
	@Override
	public String addFormArticle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		return viewName;
	}

	// 한 개 이미지 글쓰기
	@Override
	// @RequestMapping(value = "/board/addNewArticle.do", method =
	// RequestMethod.POST)
	// @ResponseBody
	@PostMapping(value = "/board/addNewArticle")
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {

		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {

			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			System.out.printf("%s %s\n", name, value);
			articleMap.put(name, value);
		}

		String imageFileName = upload(multipartRequest);
		// HttpSession session = multipartRequest.getSession();
		// MemberVO memberVO = (MemberVO) session.getAttribute("member");
		// String id = memberVO.getId();
		articleMap.put("parentNO", 0);
		// articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);

		Map<String, String> map = new HashMap<String, String>();
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		try {
			int articleNO = boardService.addNewArticle(articleMap);
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			map.put("message", "새글을 추가했습니다.");
			map.put("path", "/board/list");
			resEnt = new ResponseEntity(map, responseHeaders, HttpStatus.CREATED);
		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();
			map.put("message", "오류가 발생했습니다. 다시 시도해 주세요.");
			map.put("path", "/board/articleForm");
			resEnt = new ResponseEntity(map, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}

	// 한개의 이미지 보여주기
	// @RequestMapping(value="/board/viewArticle" ,method = RequestMethod.GET)
	@GetMapping(value = "/board/viewArticle")
	public ArticleVO viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return boardService.viewArticle(articleNO);
	}

	/*
	 * //다중 이미지 보여주기
	 * 
	 * @RequestMapping(value="/board/viewArticle.do" ,method = RequestMethod.GET)
	 * public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
	 * HttpServletRequest request, HttpServletResponse response) throws Exception{
	 * String viewName = (String)request.getAttribute("viewName"); Map
	 * articleMap=boardService.viewArticle(articleNO); ModelAndView mav = new
	 * ModelAndView(); mav.setViewName(viewName); mav.addObject("articleMap",
	 * articleMap); return mav; }
	 */

	// 한 개 이미지 수정 기능
	@Override
	// @RequestMapping(value="/board/modArticle" ,method = RequestMethod.POST)
	// @ResponseBody
	@PutMapping("/board/modArticle")
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		String imageFileName = upload(multipartRequest);
		articleMap.put("imageFileName", imageFileName);

		String articleNO = (String) articleMap.get("articleNO");
		Map<String, String> map = new HashMap<String, String>();

		System.out.println("===========");
		System.out.println(articleMap);
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		try {
			boardService.modArticle(articleMap);
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);

				String originalFileName = (String) articleMap.get("originalFileName");
				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
				oldFile.delete();
			}
			// message = "<script>";
			// message += " alert('글을 수정했습니다.');";
			// message += "
			// location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			// message +=" </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "글을 수정했습니다.");
			map.put("path", "/board/viewArticle/" + Integer.parseInt(articleNO));

		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();
			// message = "<script>";
			// message += " alert('오류가 발생했습니다.다시 수정해주세요');";
			// message += "
			// location.href='"+multipartRequest.getContextPath()+"/board/viewArticle.do?articleNO="+articleNO+"';";
			// message +=" </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "오류가 발생했습니다.다시 수정해주세요");
			map.put("path", "/board/viewArticle/" + articleNO);
		}
		resEnt = new ResponseEntity(map, responseHeaders, HttpStatus.CREATED);
		return resEnt;
	}

	@Override
	// @RequestMapping(value="/board/removeArticle" ,method = RequestMethod.POST)
	// @ResponseBody
	@DeleteMapping("/board/removeArticle")
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// response.setContentType("application/json; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		System.out.println("articleNO:" + articleNO);
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		Map<String, String> map = new HashMap<String, String>();
		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
			FileUtils.deleteDirectory(destDir);

			// message = "<script>";
			// message += " alert('글을 삭제했습니다.');";
			// message += "
			// location.href='"+request.getContextPath()+"/board/listArticles.do';";
			// message +=" </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "글을 삭제했습니다.");

		} catch (Exception e) {
			// message = "<script>";
			// message += " alert('작업중 오류가 발생했습니다.다시 시도해 주세요.');";
			// message += "
			// location.href='"+request.getContextPath()+"/board/listArticles.do';";
			// message +=" </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "작업중 오류가 발생했습니다.다시 시도해 주세요.");

			e.printStackTrace();
		}
		map.put("path", "/board/list");
		resEnt = new ResponseEntity(map, responseHeaders, HttpStatus.CREATED);
		return resEnt;
	}

	/*
	 * //다중 이미지 글 추가하기
	 * 
	 * @Override
	 * 
	 * @RequestMapping(value="/board/addNewArticle.do" ,method = RequestMethod.POST)
	 * 
	 * @ResponseBody public ResponseEntity addNewArticle(MultipartHttpServletRequest
	 * multipartRequest, HttpServletResponse response) throws Exception {
	 * multipartRequest.setCharacterEncoding("utf-8"); String imageFileName=null;
	 * 
	 * Map articleMap = new HashMap(); Enumeration
	 * enu=multipartRequest.getParameterNames(); while(enu.hasMoreElements()){
	 * String name=(String)enu.nextElement(); String
	 * value=multipartRequest.getParameter(name); articleMap.put(name,value); }
	 * 
	 * //로그인 시 세션에 저장된 회원 정보에서 글쓴이 아이디를 얻어와서 Map에 저장합니다. HttpSession session =
	 * multipartRequest.getSession(); MemberVO memberVO = (MemberVO)
	 * session.getAttribute("member"); String id = memberVO.getId();
	 * articleMap.put("id",id); articleMap.put("parentNO", 0);
	 * 
	 * List<String> fileList =upload(multipartRequest); List<ImageVO> imageFileList
	 * = new ArrayList<ImageVO>(); if(fileList!= null && fileList.size()!=0) {
	 * for(String fileName : fileList) { ImageVO imageVO = new ImageVO();
	 * imageVO.setImageFileName(fileName); imageFileList.add(imageVO); }
	 * articleMap.put("imageFileList", imageFileList); } String message;
	 * ResponseEntity resEnt=null; HttpHeaders responseHeaders = new HttpHeaders();
	 * responseHeaders.add("Content-Type", "text/html; charset=utf-8"); try { int
	 * articleNO = boardService.addNewArticle(articleMap); if(imageFileList!=null &&
	 * imageFileList.size()!=0) { for(ImageVO imageVO:imageFileList) { imageFileName
	 * = imageVO.getImageFileName(); File srcFile = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName); File destDir = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+articleNO); //destDir.mkdirs();
	 * FileUtils.moveFileToDirectory(srcFile, destDir,true); } }
	 * 
	 * message = "<script>"; message += " alert('새글을 추가했습니다.');"; message +=
	 * " location.href='"+multipartRequest.getContextPath()
	 * +"/board/listArticles.do'; "; message +=" </script>"; resEnt = new
	 * ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	 * 
	 * 
	 * }catch(Exception e) { if(imageFileList!=null && imageFileList.size()!=0) {
	 * for(ImageVO imageVO:imageFileList) { imageFileName =
	 * imageVO.getImageFileName(); File srcFile = new
	 * File(ARTICLE_IMAGE_REPO+"\\"+"temp"+"\\"+imageFileName); srcFile.delete(); }
	 * }
	 * 
	 * 
	 * message = " <script>"; message +=" alert('오류가 발생했습니다. 다시 시도해 주세요');');";
	 * message +=" location.href='"+multipartRequest.getContextPath()
	 * +"/board/articleForm.do'; "; message +=" </script>"; resEnt = new
	 * ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
	 * e.printStackTrace(); } return resEnt; }
	 * 
	 */

	// 한개 이미지 업로드하기
	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		String imageFileName = null;
		Iterator<String> fileNames = multipartRequest.getFileNames();

		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName = mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
			if (mFile.getSize() != 0) { // File Null Check
				if (!file.exists()) { // 경로상에 파일이 존재하지 않을 경우
					file.getParentFile().mkdirs(); // 경로에 해당하는 디렉토리들을 생성
					mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName)); // 임시로 저장된
																											// multipartFile을
																											// 실제 파일로 전송
				}
			}

		}
		return imageFileName;
	}

	/*
	 * //다중 이미지 업로드하기 private List<String> upload(MultipartHttpServletRequest
	 * multipartRequest) throws Exception{ List<String> fileList= new
	 * ArrayList<String>(); Iterator<String> fileNames =
	 * multipartRequest.getFileNames(); while(fileNames.hasNext()){ String fileName
	 * = fileNames.next(); MultipartFile mFile = multipartRequest.getFile(fileName);
	 * String originalFileName=mFile.getOriginalFilename();
	 * fileList.add(originalFileName); File file = new File(ARTICLE_IMAGE_REPO
	 * +"\\"+"temp"+"\\" + fileName); if(mFile.getSize()!=0){ //File Null Check
	 * if(!file.exists()){ //경로상에 파일이 존재하지 않을 경우 file.getParentFile().mkdirs();
	 * //경로에 해당하는 디렉토리들을 생성 mFile.transferTo(new File(ARTICLE_IMAGE_REPO
	 * +"\\"+"temp"+ "\\"+originalFileName)); //임시로 저장된 multipartFile을 실제 파일로 전송 } }
	 * } return fileList; }
	 */

	@Override
	@RequestMapping(value = "/board/replyForm", method = RequestMethod.POST)
	public String replyFormArticle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		int parentNO = Integer.parseInt(request.getParameter("parentNO"));
		HttpSession session = request.getSession();
		session.setAttribute("parentNO", parentNO);
		return viewName;
	}

	@Override
	// @RequestMapping(value="/board/addReply", method=RequestMethod.POST)
	@PostMapping("/board/addReply")
	public ResponseEntity replyNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		String imageFileName = upload(multipartRequest);
		// HttpSession session = multipartRequest.getSession();
		// MemberVO memberVO = (MemberVO) session.getAttribute("member");
		// String id = memberVO.getId();
		// articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);

		// int parentNO = Integer.parseInt(session.getAttribute("parentNO").toString());
		// session.removeAttribute("parentNO");
		// articleMap.put("parentNO", parentNO);

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=utf-8");
		Map<String, String> map = new HashMap<String, String>();
		try {
			int articleNO = boardService.replyNewArticle(articleMap);
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}

			// message = "<script>";
			// message += " alert('새글을 추가했습니다.');";
			/// message += " location.href='" + multipartRequest.getContextPath() +
			// "/board/listArticles.do'; ";
			// message += " </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "새 답변을 추가했습니다.");
			map.put("path", "/board/list");
		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();

			// message = " <script>";
			// message += " alert('오류가 발생했습니다. 다시 시도해 주세요');');";
			// message += " location.href='" + multipartRequest.getContextPath() +
			// "/board/replyForm.do'; ";
			// message += " </script>";
			// resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			map.put("message", "오류가 발생했습니다. 다시 시도해 주세요");
			map.put("path", "/board/replyForm");
			e.printStackTrace();
		}
		resEnt = new ResponseEntity(map, responseHeaders, HttpStatus.CREATED);
		return resEnt;
	}

}// end class
