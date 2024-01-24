package com.javaex.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import com.javaex.vo.GuestbookVo;

public class GuestbookDaoTest { 
	
	private GuestbookDao dao;
	
	 @Before
	    public void setUp() {
		 dao = new GuestbookDaoImpl();
    }
	    
	@Test
	public void testGetList() {
		// Given from setUp
		this.setUp();
		
	    // When
	    List<GuestbookVo> list = dao.getList();

	    // Then
	    assertNotNull(list);
	    assertTrue(list.size() > 0); 
	}

	@Test
	public void testInsert() { 
	    // Given
	    this.setUp();
		GuestbookVo guestbook = new GuestbookVo("John Doe", "123", "Test content");
	    
	    // When
	    int count = dao.insert(guestbook);

	    // Then
	    assertEquals(1, count);
	}

	@Test
	public void testDelete() { 
	    // Given
	    this.setUp();
	    GuestbookVo guestbook = new GuestbookVo(1, "John Doe", "123", "Test content", "2024-01-24");

	    // When
	    int count = dao.delete(guestbook);
	    
	    // Then
	    assertEquals(0, count);
	}

}
