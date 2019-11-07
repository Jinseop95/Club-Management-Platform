package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class BbsDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	Statement st;

	public BbsDAO() {

		try {

			String dbURL = "jdbc:mysql://localhost:3306/mydb";

			String dbID = "root";

			String dbPassword = "2865";

			Class.forName("com.mysql.jdbc.Driver");

			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void db_close() {

		try {

			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (st != null)
				st.close();

		} catch (SQLException e) {
			System.out.println(e + "=> 데이터베이스오류");
		}

	} // db_close

	public int getNext() {

		String SQL = "SELECT BOARD_NO FROM board ORDER BY BOARD_NO DESC";

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				return rs.getInt(1) + 1;

			}

			return 1;
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;
	}

	public int write(Bbs bbs, String INPUT_ID) {
		String SQL = "INSERT INTO board (CLUB_ID, BOARD_NO,board_cd,TITLE,CONTENTS,INPUT_ID,INPUT_DATE,bbsAvailable)"
				+ "VALUES (?,?,?,?,?,?,?,?)";
	
		if(bbs.getStart_date()!=null || bbs.getEnd_date() != null){
			SQL = "INSERT INTO board (CLUB_ID, BOARD_NO,board_cd,TITLE,CONTENTS,INPUT_ID,INPUT_DATE,bbsAvailable,START_DATE,END_DATE)"
					+ "VALUES (?,?,?,?,?,?,?,?,'" + bbs.getStart_date() +"','" + bbs.getEnd_date() +"')";
		}
		java.util.Date dt = new java.util.Date();

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String today = sdf.format(dt);

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, bbs.getClub_id());
			pstmt.setInt(2, getNext());
			pstmt.setString(3, bbs.getBoard_cd());
			pstmt.setString(4, bbs.getTITLE());
			pstmt.setString(5, bbs.getCONTENTS());
			pstmt.setString(6, INPUT_ID);
			pstmt.setString(7, today);
			pstmt.setInt(8, 1);

			return pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;

	}

	public int getClubIds(String club_NM) {
		String SQL = "SELECT CLUB_ID FROM club where CLUB_NM='" + club_NM + "'";
		int answer = -1;
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				answer = rs.getInt(1);
			}

			return answer;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return answer;// 디비 오류
	}

	// 추가(JS)   ,  howmany, getNext 사용 여부 확인
	public ArrayList<Bbs> getclub_search(int club_id, String board_cd, int pageNumber, String condition) {

		String SQL = 
		"SELECT SQL_CALC_FOUND_ROWS * FROM BOARD WHERE BBSAVAILABLE = 1 AND TITLE LIKE ? "+
		"AND CLUB_ID = ? AND BOARD_CD = ? ORDER BY BOARD_NO DESC LIMIT " + (pageNumber - 1) * 10 + "," + 10;
		
		ArrayList<Bbs> list = new ArrayList<Bbs>();

	      int row_count = 0;

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);
	
			pstmt.setString(1, "%" + condition + "%");
			pstmt.setInt(2, club_id);
			pstmt.setString(3, board_cd);
						
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Bbs bbs = new Bbs();
				int hit = rs.getInt(6);
				bbs.setClub_id(rs.getInt(1));
				bbs.setBOARD_NO(rs.getInt(2));
				bbs.setBoard_cd(rs.getString(3));
				bbs.setTITLE(rs.getString(4));
				bbs.setCONTENTS(rs.getString(5));
				bbs.setOPEN_CNT(hit);
				bbs.setINPUT_ID(rs.getString(7));
				bbs.setINPUT_DATE(rs.getString(9));
				bbs.setBbsAvailable(rs.getInt(13));
				list.add(bbs);
				hit++;
			}
			Statement st2 = conn.createStatement();
	        ResultSet rs2 = st2.executeQuery("SELECT FOUND_ROWS()");

	         while (rs2.next()) {
	            row_count = rs2.getInt(1);
	            list.get(0).setRow_count(row_count);
	         }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return list;
	}

	public boolean nextPage(int pageNumber) {

		String SQL = "SELECT * FROM board WHERE BOARD_NO < ? AND bbsAvailable=1";
		/* String SQL = "SELECT * FROM board"; */

		ArrayList<Bbs> list = new ArrayList<Bbs>();

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, getNext() - (pageNumber - 1) * 8);

			rs = pstmt.executeQuery();

			if (rs.next()) {

				return true;

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return false;

	}
// 삭제
	public int howmanyPage(int pageNumber) {

		String SQL = "SELECT * FROM board WHERE bbsAvailable=1";

		ArrayList<Bbs> list = new ArrayList<Bbs>();
		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			rs = pstmt.executeQuery();
			int num = 0;
			while (rs.next()) {
				num++;
			}
			return num;

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;

	}

	public int cnt_update(int BOARD_NO) {

		String SQL = "UPDATE board set open_cnt = open_cnt+1 where board_no=? ";

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, BOARD_NO);

			return pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;

	}

	public Bbs getBbs(int BOARD_NO, String BOARD_CD) {

		int i = cnt_update(BOARD_NO);

		String SQL = "SELECT * FROM board WHERE BOARD_NO = ?";

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, BOARD_NO);

			rs = pstmt.executeQuery();

			// rs2=pstmt2.executeQuery();

			if (rs.next()) {
				Bbs bbs = new Bbs();
				int hit = rs.getInt(6);
				bbs.setClub_id(rs.getInt(1));
				bbs.setBOARD_NO(rs.getInt(2));	
				bbs.setBoard_cd(BOARD_CD);
				bbs.setTITLE(rs.getString(4));
				bbs.setCONTENTS(rs.getString(5));
				bbs.setOPEN_CNT(hit);
				bbs.setINPUT_ID(rs.getString(7));
				bbs.setINPUT_DATE(rs.getString(9));
				bbs.setBbsAvailable(rs.getInt(13));
				bbs.setStart_date(rs.getString(14));
				bbs.setEnd_date(rs.getString(15));
				return bbs;

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return null;

	}

	public int update(Bbs bbs) {
		String SQL = "UPDATE board SET TITLE = ?, CONTENTS = ? WHERE BOARD_NO = ?";
		
		if(bbs.getStart_date() != null || bbs.getEnd_date() != null){
			SQL = "UPDATE board SET TITLE = ?, CONTENTS = ?, START_DATE = '" + 
					bbs.getStart_date() + "', END_DATE = '" + bbs.getEnd_date() + "' WHERE BOARD_NO = ?";
		}

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, bbs.getTITLE());
			pstmt.setString(2, bbs.getCONTENTS());
			pstmt.setInt(3, bbs.getBOARD_NO());

			return pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;

	}

	public int delete(int BOARD_NO) {

		String SQL = "UPDATE board SET bbsAvailable = 0 WHERE BOARD_NO = ?";

		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, BOARD_NO);

			return pstmt.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return -1;

	}

	// 추가 , 메인화면 공지사항 4개 가져오기
	public ArrayList<Bbs> get_intro(int club_id, String board_cd) {

		String SQL = "SELECT TITLE, BOARD_NO, INPUT_DATE FROM BOARD WHERE CLUB_ID = ? AND BOARD_CD = ? AND BBSAVAILABLE = 1 "
				+ " ORDER BY BOARD_NO DESC LIMIT 6";
		ArrayList<Bbs> list = null;
		try {

			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setInt(1, club_id);
			pstmt.setString(2, board_cd);
			
			rs = pstmt.executeQuery();
			list = new ArrayList<Bbs>();

			while (rs.next()) {
				Bbs vo = new Bbs();
				vo.setTITLE(rs.getString(1));
				vo.setBOARD_NO(rs.getInt(2));
				vo.setINPUT_DATE(rs.getString(3).substring(0,10));
				list.add(vo);

			}

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}

		return list;

	}
	

	public String getCalendar(int club_id) {
		String jsonInfo = null;
		JSONArray personArray = new JSONArray();
		JSONObject personInfo = new JSONObject();
		
		String sql = "select title, start_date, end_date, board_no, club_id from board where club_id=? and start_date is not null "
				+ " and bbsAvailable = 1";
		try{
			
			PreparedStatement pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, club_id);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				personInfo = new JSONObject();
				personInfo.put("title", rs.getString(1));
				personInfo.put("start", rs.getString(2).substring(0,10));
				personInfo.put("end", rs.getString(3).substring(0,10)+" 04:00");
				personInfo.put("url", "myview.jsp?BOARD_NO="+ rs.getInt(4) +"&club_id="+ rs.getInt(5) + "&board_cd=007004");
				
				personArray.add(personInfo);
				//2019-10-15 00:30:00.0
			}
			
			 jsonInfo = personArray.toJSONString();
		}catch (Exception e) {

			e.printStackTrace();

		} finally {
			db_close();
		}
		return jsonInfo;
	}

}