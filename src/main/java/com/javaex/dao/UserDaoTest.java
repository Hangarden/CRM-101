package com.javaex.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

import com.javaex.vo.UserVo;

public class UserDaoTest {
	
	
	

    @Test
    public void testInsert() {
        UserVo user = new UserVo("TestUser", "test@example.com", "testpassword", "Female");
        UserDao dao = new UserDaoImpl();
        int count = dao.insert(user);
        assertEquals(1, count);
    }
    
    @Test
    public void testUpdateWithPassword() {
    	//비밀번호 변경 시
        UserVo user = new UserVo(21, "NewName", "test@example.com", "", "Male");
        UserDao dao = new UserDaoImpl();
        int count = dao.update(user);
        assertEquals(1, count);
        
        //비밀번호 변경하지 않을 시
        user = new UserVo(21, "NewName", "test@example.com", "5678", "Male");
        dao = new UserDaoImpl();
        count = dao.update(user);
        assertEquals(1, count);
    }
    
	@Test
	public void testGetUserEmailPassword() {
		String email = "test@example.com";
	    String password = "testpassword";
        UserDao dao = new UserDaoImpl();
	    UserVo user = dao.getUser(email, password);
	    assertNotNull(user);
	}

	@Test
	public void testGetUserInt() {
        int userNo = 1;
        UserDao dao = new UserDaoImpl();
        UserVo user = dao.getUser(userNo);
        assertNotNull(user);
        assertEquals(userNo, user.getNo());
	}

	@Test
	public void testIdCheck() {
        // 존재하는 이메일 일때
        String existingEmail = "test@example.com";
        UserDao dao = new UserDaoImpl();
        String resultExists = dao.idCheck(existingEmail);
        assertEquals("false", resultExists);

        // 존재하지 않는 이메일 일때
        String newEmail = "newuser@example.com";
        String resultNotExists = dao.idCheck(newEmail);
        assertEquals("true", resultNotExists);
	}

}
