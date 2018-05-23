package com.dgit.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dgit.util.MediaUtils;
import com.dgit.util.UploadFileUtils;

@Controller
public class UploadController {
	private final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	private String innerUploadPath ="resources/upload";
	
	
	/*servlet-context.xml안에 id*/
	@Resource(name="uploadPath")
	private String outerUploadPath;
	
	@RequestMapping(value="innerUpload", method=RequestMethod.GET)
	public String innerUploadForm(){
		return "innerUploadForm";
	}
	
	@RequestMapping(value="innerUpload", method=RequestMethod.POST)
	public String innerUploadResult(String writer,MultipartFile file,HttpServletRequest request,Model model) throws IOException{
		logger.info("writer : " +writer);
		logger.info("file : " +file.getOriginalFilename());
		
		String root_path = request.getRealPath("/");
		logger.info("root_path : "+root_path);
		
		File dirPath = new File(root_path +"/"+innerUploadPath);
		if(dirPath.exists() == false){
			dirPath.mkdirs();
		}
		
		UUID uid = UUID.randomUUID(); //중복되지 않는 고유한 키값을 설정할 때 사용
		String saveName = uid.toString() +"_"+ file.getOriginalFilename();
		File target = new File(root_path +"/"+innerUploadPath+"/"+saveName);
		FileCopyUtils.copy(file.getBytes(), target);
		
		model.addAttribute("writer",writer);
		model.addAttribute("filePath",innerUploadPath+"/"+saveName);
		
		return "innerUploadResult";
	}
	
	@RequestMapping(value="innerMultiUpload", method=RequestMethod.GET)
	public String innerMultiUploadForm(){
		return "innerMultiUploadForm";
	}
	
	@RequestMapping(value="innerMultiUpload", method=RequestMethod.POST)
	public String innerMultiUploadResult(String writer,List<MultipartFile> files,HttpServletRequest request,Model model) throws IOException{
		logger.info("writer : "+writer);
		
		for(MultipartFile file:files){
			logger.info("filename : "+file.getOriginalFilename());
			logger.info("fileSize : "+file.getSize());
		}
		
		String root_path = request.getRealPath("/");
		logger.info("root_path : "+root_path);
		
		File dirPath = new File(root_path +"/"+innerUploadPath);
		if(dirPath.exists() == false){
			dirPath.mkdirs();
		}
		
		ArrayList<String> pathList = new ArrayList<>();
		for(MultipartFile file:files){
			UUID uid = UUID.randomUUID(); //중복되지 않는 고유한 키값을 설정할 때 사용
			String saveName = uid.toString() +"_"+ file.getOriginalFilename();
																	/*savaName에서 이름을 임의로 바꿀수있음*/				
			File target = new File(root_path +"/"+innerUploadPath+"/"+saveName);
			FileCopyUtils.copy(file.getBytes(), target);
			
			pathList.add(saveName);
		}
		model.addAttribute("writer",writer);
		model.addAttribute("listPath",pathList);
		return "innerMultiUploadResult";
	}
	
	/*외부경로*/
	@RequestMapping(value="outerUpload", method=RequestMethod.GET)
	public String outerUploadForm(){
		return "UploadForm";
	}
	
	@RequestMapping(value="outerUpload", method=RequestMethod.POST)
	public String outerUploadResult(String writer,MultipartFile file,Model model) throws IOException{
		logger.info("writer : " +writer);
		logger.info("file : " +file.getOriginalFilename());
		
		
		/*파일만들어줘야함*/
		/*File dirPath = new File("c:/zzz");
		if(dirPath.exists() == false){
			dirPath.mkdirs();
		}
		
		File dirPath = new File("c:/zzz/upload");
		if(dirPath.exists() == false){
			dirPath.mkdirs();
		}*/
		logger.info("outerUploadPath : "+ outerUploadPath);
		
		UUID uid = UUID.randomUUID(); //중복되지 않는 고유한 키값을 설정할 때 사용
		String saveName = uid.toString() +"_"+ file.getOriginalFilename();
		File target = new File(outerUploadPath+"/"+saveName);
		FileCopyUtils.copy(file.getBytes(), target);
		model.addAttribute("writer",writer);
		model.addAttribute("file",saveName);
		
		return "UploadResult";
	}
	
	/*카페24는 내부업로드*/
	/*실무는 외무업로드*/
	/*@RestController가 아닐때*/
	/*화면전환이 아닐때  @ResponseBody*/
	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String filename)throws Exception{
		ResponseEntity<byte[]> entity = null;
		InputStream in = null;
		
		logger.info("[displayFile] filename" + filename);
		try{
			String format = filename.substring(filename.lastIndexOf(".")+1);
			MediaType mType = MediaUtils.getMediaType(format);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mType);
			
			in = new FileInputStream(outerUploadPath+"/"+filename);
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in),headers,HttpStatus.CREATED);
			
		}catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}finally {
			in.close();
		}
		return entity;
	}
	
	
	@RequestMapping(value="dragUpload", method=RequestMethod.GET)
	public String dragUpload(){
		return "uploadDragForm";
	}
	
	
	@ResponseBody
	@RequestMapping(value="dragUpload", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> dragUploadResult(String writer,List<MultipartFile> files) throws IOException{
		logger.info("writer : " +writer);
		for(MultipartFile file:files){
			logger.info("file : " +file.getOriginalFilename());
		}
		ResponseEntity<Map<String,Object>> entity = null;
		
		try{
			
			List<String> list = new ArrayList<>();
			for(MultipartFile file:files){
				UUID uid = UUID.randomUUID(); //중복되지 않는 고유한 키값을 설정할 때 사용
				String saveName = uid.toString() +"_"+ file.getOriginalFilename();
				File target = new File(outerUploadPath+"/"+saveName);
				FileCopyUtils.copy(file.getBytes(), target);		
				list.add(saveName);
			}	
			Map<String,Object> map = new HashMap<>();
			map.put("result", "success");
			map.put("listFile", list);
			entity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			Map<String,Object> map = new HashMap<>();
			map.put("result", "fail");
			entity = new ResponseEntity<Map<String,Object>>(map,HttpStatus.BAD_REQUEST);
		}
			
		return entity;
	}
	
	
	@RequestMapping(value="previewUpload", method=RequestMethod.GET)
	public String previewUpload(){
		return "previewForm";
	}
	
	@RequestMapping(value="previewUpload", method=RequestMethod.POST)
	public String previewUploadResult(String writer, MultipartFile file,Model model) throws IOException{
		logger.info("writer : " +writer);
		logger.info("file : "+file.getOriginalFilename());
		
		/*UUID uid = UUID.randomUUID(); //중복되지 않는 고유한 키값을 설정할 때 사용
		String saveName = uid.toString() +"_"+ file.getOriginalFilename();
		File target = new File(outerUploadPath+"/"+saveName);
		FileCopyUtils.copy(file.getBytes(), target);*/
		String filePath = UploadFileUtils.uploadFile(outerUploadPath, file.getOriginalFilename(), file.getBytes());
			
		model.addAttribute("writer",writer);
		model.addAttribute("file",filePath);
		
		return "previewResult";
	}
	
	@RequestMapping(value="deleteFile", method=RequestMethod.GET)
	public String deleteFile(String filename){
		logger.info("[deleteFile] filename: : " +filename);
		
		//원본, 썸네일 2가지 파일 삭제
		/*String formatName = filename.substring(filename.lastIndexOf(".")+1);
		logger.info("[deleteFile] formatName: : " +formatName);
		
		MediaType mType =MediaUtils.getMediaType(formatName);
		
		if(mType !=null){
			String front = filename.substring(0, 12);
			logger.info("[deleteFile] front: : " +front);
			String end = filename.substring(14);
			logger.info("[deleteFile] end: : " +end);
			new File(outerUploadPath + (front+end).replace('/',File.separatorChar)).delete();
		}
			new File(outerUploadPath+ filename.replace('/', File.separatorChar)).delete();*/
		UploadFileUtils.deleteFile(outerUploadPath, filename);
		return "deleteResult";
	}
}
