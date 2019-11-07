package item;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import budget.BudgetVO;

public class itemDAO {
	Connection con;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;

	// MySQL
	String driverName = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/mydb";

	/*
	 * //ORACLE String driverName="oracle.jdbc.driver.OracleDriver"; String url =
	 * " jdbc:oracle:thin:@localhost:1521:ORCL";
	 */
	String id = "root";
	String pwd = "2865";

	public itemDAO() {

		try {

			Class.forName(driverName);

			con = DriverManager.getConnection(url, id, pwd);

		} catch (ClassNotFoundException e) {

			System.out.println(e + "=> database fail");

		} catch (SQLException e) {

			System.out.println(e + "=> database fail");
		}
	}// JDBC_clubDAO()

	public void db_close() {

		try {

			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (st != null)
				st.close();

		} catch (SQLException e) {
			System.out.println(e + "=> 데이터베이스오류");
		}

	} // db_close

	public int getClubIds(String club_NM) {
		String SQL = "SELECT CLUB_ID FROM club where CLUB_NM='" + club_NM + "'";
		int answer = -1;
		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				answer = rs.getInt(1);
			}

			return answer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return answer;// 디비 오류
	}
	
	  public String getClubNMs(int club_id) {
	       String SQL = "SELECT CLUB_NM FROM club where club_id="+club_id;
	       String answer="";
	         try {
	            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
	            ResultSet rs = pstmt.executeQuery();
	            
	            if(rs.next()) {
	                answer = rs.getString(1);
	            }
	            
	            return answer;
	         } catch (Exception e) {
	            e.printStackTrace();
	         }
	         return null;//디비 오류
	         
	      }

	public int modItme(int club_Id, ArrayList<itemVo> list) {
//		int club_Id,String Item_nm,int Tot_cnt,String Item_cont
		String SQL = "Delete from item where CLUB_ID="+club_Id;
		int answer = -1;
		try {
			PreparedStatement pstmt1 = (PreparedStatement) con.prepareStatement(SQL);
			pstmt1.executeUpdate();

			int totalElements = list.size();// arrayList의 요소의 갯수를 구한다.
			for (int index = 0; index < totalElements; index++) {
				SQL = "INSERT INTO item(CLUB_ID,SEQ_NO,ITEM_NM,ITEM_CONT,TOT_CNT) VALUES(?,?,?,?,?)";
				PreparedStatement pstmt2 = (PreparedStatement) con.prepareStatement(SQL);
				pstmt2.setInt(1, club_Id);
				pstmt2.setInt(2, index);
				pstmt2.setString(3, list.get(index).getITEM_NM());
				pstmt2.setString(4, list.get(index).getITEM_CONT());
				pstmt2.setInt(5, list.get(index).getTOT_CNT());
				pstmt2.executeUpdate();
			}

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return answer;// 디비 오류
	}
	
}
