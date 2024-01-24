package com.javaex.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 파일 이름 파라미터 받기
        String filename = request.getParameter("filename");

        // 파일이 저장된 디렉토리 경로
        String filePath = "/javastudy/workspace/mysite/src/main/webapp/WEB-INF/uploadfile";
        //String filePath = "/Users/User/git/CRM-101_3/src/main/webapp/WEB-INF/uploadfile";
      

        // 파일 객체 생성
        File file = new File(filePath + File.separator + filename);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // MIME 타입 설정
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // 파일 전송
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }
}