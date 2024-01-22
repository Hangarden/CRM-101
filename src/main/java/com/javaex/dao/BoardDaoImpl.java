package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.util.WebUtil;
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
	public List<BoardVo> getSubList(int nowPage,int pagePerNum) {
		
		// 0. import java.sql.*;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<BoardVo> list = new ArrayList<BoardVo>();
			int startNum = (nowPage-1)*pagePerNum+1;
			System.out.println("startRowNum: "+ startNum);
			
			try {
				conn = getConnection();

				// 3. SQL문 준비 / 바인딩 / 실행
				String query = "SELECT c.*\r\n"
						+ "FROM (\r\n"
						+ "    SELECT ROWNUM AS rn, b.*\r\n"
						+ "    FROM (\r\n"
						+ "        SELECT b.no, b.title, b.hit, TO_CHAR(b.reg_date, 'YY-MM-DD HH24:MI') AS reg_date, b.user_no, u.name \r\n"
						+ "        FROM board b, users u \r\n"
						+ "        WHERE b.user_no = u.no\r\n"
						+ "        ORDER BY no DESC\r\n"
						+ "    ) b\r\n"
						+ ") c\r\n"
						+ "WHERE c.rn BETWEEN ? AND ?";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1,startNum);
				pstmt.setInt(2,startNum + pagePerNum-1);
				System.out.println("endRowNum: "+ (startNum + pagePerNum-1));
				
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
	
	public int getTotalRecord(String keyField,String keyWord) {
		
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int no = 0;
		
		try {
			conn = getConnection();
			String query="";
			
			// 3. SQL문 준비 / 바인딩 / 실행
			if (keyWord != null && keyWord!="") {
				query = "SELECT COUNT(no)\r\n"
						+ "FROM BOARD b\r\n"
						+ "WHERE ? LIKE ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "%" + keyField + "%");
				pstmt.setString(2, "%" + keyWord + "%");
			}
			else {
				query = "SELECT COUNT(no)\r\n"
						+ "FROM BOARD b ";
				pstmt = conn.prepareStatement(query);
			}

			rs = pstmt.executeQuery();
			
			// 4.결과처리
			if (rs.next()) {
				no = rs.getInt(1);
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
		System.out.println("totalRecord: "+no);
		return no;
	}
	
	public BoardVo getBoard(int no) {

		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo boardVo = null;
		
		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "select b.no, b.title, b.content, b.hit, b.reg_date, b.user_no, u.name "
					     + "from board b, users u "
					     + "where b.user_no = u.no "
					     + "and b.no = ?";
			
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				String title = rs.getString("title");
				String content = rs.getString("content");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				String userName = rs.getString("name");
				
				boardVo = new BoardVo(no, title, content, hit, regDate, userNo, userName);
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
		System.out.println(boardVo);
		return boardVo;

	}
	
	public int insert(BoardVo vo) {
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();
		  
		  System.out.println("vo.userNo : ["+vo.getUserNo()+"]");
      System.out.println("vo.title : ["+vo.getTitle()+"]");
      System.out.println("vo.content : ["+vo.getContent()+"]");
      
			// 3. SQL문 준비 / 바인딩 / 실행
//			String query = "insert into board values (seq_board_no.nextval, ?, ?, 0, sysdate, ?)";
      		String query = "insert into board (no, title, content, hit, reg_date, user_no) values (seq_board_no.nextval, ?, ?, 0, sysdate, ?)";
      		pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getUserNo());
			
			
      
			count = pstmt.executeUpdate();

			// 4.결과처리
			System.out.println(count + "건 등록");

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
		// 0. import java.sql.*;
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
		  conn = getConnection();

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "update board set title = ?, content = ? where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setInt(3, vo.getNo());

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
		  	
//		  String optionStr="";
//		  if(option.equals("author")) {
//			  optionStr 
//		  }else if(option.equals("post_date")) {
//			  optionStr
//		  }else if(option.equals("title")) {
//			  optionStr 
//		  }else if(option.equals("content")) {
//			  optionStr 
//		  }
//		  이러고 하나의 쿼리문에 ?로 삽입, 그리고 page나누는 조건 추가할 수도 있음
				  
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
