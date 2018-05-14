package com.dgit.ex02;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dgit.domain.ReplyVO;
import com.dgit.persistence.ReplyDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
public class ReplyDAOTest {

	@Autowired
	ReplyDAO dao;
	
	//@Test
	public void testlist() throws Exception{
		dao.list(3);
		
	}
	
	//@Test
	public void testCreate() throws Exception{
		ReplyVO vo  = new ReplyVO();	
		vo.setBno(3);
		vo.setReplytext("저두요");
		vo.setReplyer("김김2");
		
		dao.create(vo);
	}
	
	//@Test
	public void testUpdate() throws Exception{
		ReplyVO vo  = new ReplyVO();	
		vo.setRno(1);
		vo.setReplytext("저두요2222");
		dao.update(vo);
	}
	
	@Test
	public void testDelete() throws Exception{
		dao.delete(1);
	}
}
