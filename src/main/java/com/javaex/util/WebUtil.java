package com.javaex.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {
	public static String ipAddress="jdbc:oracle:thin:@172.20.40.73:1521:xe";
	
	public static void forward(HttpServletRequest request, HttpServletResponse response, String path) 
			throws ServletException, IOException {
		
		RequestDispatcher rd = request.getRequestDispatcher(path);
		rd.forward(request, response);
	}
	
	public static void redirect(HttpServletRequest request, HttpServletResponse response,String url)
			throws IOException {
		response.sendRedirect(url);
	}
}


