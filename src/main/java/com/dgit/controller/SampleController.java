package com.dgit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dgit.domain.SampleVO;

@RequestMapping("/sample")
@RestController
public class SampleController {
	private static final Logger logger = LoggerFactory.getLogger(SampleController.class);
	
	/*@RequestMapping("/hello") get/post공동사용*/
	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String sayHello(){ //ex02/sample/hello
		return "hello";
	}
	
	@RequestMapping("/sendVO")
	public SampleVO sendVO(){
		SampleVO vo = new SampleVO();
		vo.setFirstName("김");
		vo.setLastName("매미");
		vo.setMno(123);
		return vo;
	}
	@RequestMapping("/sendList")
	public List<SampleVO> sendList(){
		List<SampleVO> list = new ArrayList<>();
		
		for(int i =0;i<10;i++){
		SampleVO vo = new SampleVO();
			vo.setFirstName("김");
			vo.setLastName("매미"+i);
			vo.setMno(i);
			list.add(vo);
		}
		return list;
	}
	
	@RequestMapping("/sendMap")
	public Map<Integer,SampleVO> sendMap(){
		Map<Integer,SampleVO> map = new HashMap<>();
		
		for(int i =1; i<=10;i++){
			SampleVO vo = new SampleVO();
			vo.setFirstName("김");
			vo.setLastName("매미"+i);
			vo.setMno(i);
			map.put(i, vo);
		}	
		return map;
	}
	
	@RequestMapping("/sendErrorAuth")
	public ResponseEntity<Void> sendListAuth(){
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping("/sendFail")
	public ResponseEntity<String> sendFail(){
		
		return new ResponseEntity<String>("failed",HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping("/sendErrorList")
	public ResponseEntity<List<SampleVO>> sendErrorList(){
		List<SampleVO> list = new ArrayList<>();
				
			for(int i =0;i<10;i++){
				SampleVO vo = new SampleVO();
					vo.setFirstName("김");
					vo.setLastName("매미"+i);
					vo.setMno(i);
					list.add(vo);
			}
		return new ResponseEntity<List<SampleVO>>(list,HttpStatus.NOT_FOUND);
	}
}
