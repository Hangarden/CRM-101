package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.util.WebUtil;
import com.javaex.vo.AttachmentVo;
import com.javaex.vo.BoardVo;

public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
//      String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
      String dburl = WebUtil.ipAddress;
      
      conn = DriverManager.getConnection(dburl, "webdb", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  
	public List<BoardVo> getList() {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.hit, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, b.user_no, u.name "
					     + " from board b, users u "
					     + " where b.user_no = u.no "
					     + " order by no desc";
			
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}
		
		return list;

	}

	
	public BoardVo getBoard(int no) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    BoardVo boardVo = null;

	    try {
	        conn = getConnection();

	        String query = "SELECT b.no, b.title, b.content, b.hit, b.reg_date, b.user_no,  b.filesize, b.filename, u.name " +
	                       "FROM board b, users u " +
	                       "WHERE b.user_no = u.no AND b.no = ?";

	        pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, no);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String title = rs.getString("title");
	            String content = rs.getString("content");
	            int hit = rs.getInt("hit");
	            String regDate = rs.getString("reg_date");
	            int userNo = rs.getInt("user_no");
	            int filesize = rs.getInt("filesize");
	            String filename = rs.getString("filename");
	            String userName = rs.getString("name");
	            boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName,filesize, filename);

	        }
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    } finally {
	        try {
	            if (rs != null) {
	                rs.close();
	            }
	            if (pstmt != null) {
	                pstmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("error:" + e);
	        }
	    }
	    return boardVo;
	}
	
	public int insert(BoardVo vo) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int count = 0;

	    try {
	        conn = getConnection();
	        String query = "INSERT INTO board (no, title, content, hit, reg_date, user_no, filesize, filename) " +
	                       "VALUES (seq_board_no.nextval, ?, ?, 0, sysdate, ?, ?, ?)";
	        pstmt = conn.prepareStatement(query);

	        pstmt.setString(1, vo.getTitle());
	        pstmt.setString(2, vo.getContent());
	        pstmt.setInt(3, vo.getUserNo());
	        pstmt.setLong(4, vo.getFilesize()); // 파일 크기를 설정합니다.
	        pstmt.setString(5, vo.getFilename()); // 파일 이름 또는 경로를 설정합니다.

	        count = pstmt.executeUpdate();

	        System.out.println(count + "건 등록");

	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    } finally {
	        try {
	            if (pstmt != null) {
	                pstmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("error:" + e);
	        }
	    }

	    return count;
	}

//	public int insert(BoardVo vo) {
//		// 0. import java.sql.*;
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		int count = 0;
//
//		try {
//		  conn = getConnection();
//		  
//		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
//      System.out.println("vo.title : ["+vo.getTitle()+"]");
//      System.out.println("vo.content : ["+vo.getContent()+"]");
//      
//			// 3. SQL문 준비 / 바인딩 / 실행
////			String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?)";
//      		String query = "insert into board (no, title, content, hit, reg_date, user_no) values (seq_board_no.nextval, ?, ?, 0, sysdate, ?)";
//      		pstmt = conn.prepareStatement(query);
//
//			pstmt.setString(1, vo.getTitle());
//			pstmt.setString(2, vo.getContent());
//			pstmt.setInt(3, vo.getUserNo());
//			
//			
//      
//			count = pstmt.executeUpdate();
//
//			// 4.결과처리
//			System.out.println(count + "건 등록");
//
//		} catch (SQLException e) {
//			System.out.println("error:" + e);
//		} finally {
//			// 5. 자원정리
//			try {
//				if (pstmt != null) {
//					pstmt.close();
//				}
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException e) {
//				System.out.println("error:" + e);
//			}
//
//		}
//
//		return count;
//	}
	
	
	public int delete(int no) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "delete from board where no = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	public int update(BoardVo vo) {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    int count = 0;

	    try {
	        conn = getConnection();

	        // 첨부파일 정보를 포함한 게시글 업데이트
	        String query = "UPDATE board SET title = ?, content = ?, filename = ?, filesize = ? WHERE no = ?";
	        pstmt = conn.prepareStatement(query);

	        pstmt.setString(1, vo.getTitle());
	        pstmt.setString(2, vo.getContent());
	        pstmt.setString(3, vo.getFilename()); // 파일 이름
	        pstmt.setLong(4, vo.getFilesize()); // 파일 크기
	        pstmt.setInt(5, vo.getNo());

	        count = pstmt.executeUpdate();

	        System.out.println(count + "건 수정");

	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    } finally {
	        try {
	            if (pstmt != null) {
	                pstmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("error:" + e);
	        }
	    }

	    return count;
	}

	@Override
	public List<BoardVo> search(String str, String option) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();


		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
		  String query = "";
		  
		  if(option.equals("author")) {
			  query = "SELECT b.no, b.TITLE, b.HIT, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, u.no as user_no, u.name " +
		               "FROM BOARD b, USERS u " +
		               "WHERE u.NAME LIKE ? " +
		               "AND b.USER_NO = u.NO";
		  }else if(option.equals("post_date")) {
			  query = "SELECT b.no, b.TITLE, b.HIT, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, u.no as user_no, u.name " +
		               "FROM BOARD b, USERS u " +
		               "WHERE b.reg_date LIKE ? " +
		               "AND b.USER_NO = u.NO";
		  }else if(option.equals("title")) {
			  query = "SELECT b.no, b.TITLE, b.HIT, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, u.no as user_no, u.name " +
		               "FROM BOARD b, USERS u " +
		               "WHERE b.title LIKE ? " +
		               "AND b.USER_NO = u.NO";
		  }else if(option.equals("content")) {
			  query = "SELECT b.no, b.TITLE, b.HIT, to_char(b.reg_date, 'YY-MM-DD HH24:MI') as reg_date, u.no as user_no, u.name " +
		               "FROM BOARD b, USERS u " +
		               "WHERE b.content LIKE ? " +
		               "AND b.USER_NO = u.NO";
		  }
			
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1,"%" + str + "%");

			rs = pstmt.executeQuery();

			// 4.결과처리
			
			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				BoardVo vo2 = new BoardVo(no, title, hit, regDate, userNo, userName);
				list.add(vo2);
			}
			

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return list;
	}

	@Override
	public int hitUp(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = " UPDATE board b\r\n "
					+ " SET b.HIT = ?\r\n "
					+ " WHERE b.NO = ? ";
			
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, vo.getHit());
			pstmt.setInt(2, vo.getNo());

			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			// 5. 자원정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

		}

		return count;
	}
	
	
	
}
