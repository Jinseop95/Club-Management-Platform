package Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import com.mysql.jdbc.PreparedStatement;

public class StudentDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public StudentDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost/mydb";
			String dbID = "root";
			String dbPassword = "2865";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public int login(String userID, String userPassword) {
		String SQL = "select PASSWORD from student where STUDENT_ID= ?";
		try {
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(userPassword)) {
					return 1;// 로그인성공
				} else
					return 0;// 비밀번호 불 일치
			}
			return -1;// 아이디 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2;// 데이터베이스의 오류
	}

	public String[] getState(String username) {
		String state[];

		String SQL = "select state from student where STUDENT_ID=" + username;
		String dongList = "";

		try {
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dongList = rs.getString(1);
			}

			state = dongList.split(",");

			return state;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;// 데이터베이스의 오류
	}

	public int writeState(String username, int club_id) {
		String SQL = "SELECT state from student where STUDENT_ID=" + username;
		String nextSQL = "UPDATE student SET state=? where STUDENT_ID=" + username;
		String origin = "";

		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			pstmt = (PreparedStatement) conn.prepareStatement(nextSQL);
			
			if (rs.next()) {
				origin = rs.getString(1);
				pstmt.setString(1,origin+","+club_id);
			}else{
				pstmt.setString(1,""+club_id);
			}
			
			

			pstmt.executeUpdate();

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// 디비 오류
	}

	public int removeState(String username, int club_id) {
		String SQL = "SELECT state from student where STUDENT_ID=" + username;
		String nextSQL = "UPDATE student SET state=? where STUDENT_ID=" + username;
		String origin = "";
		String clubId = Integer.toString(club_id);

		String newList[] = null;

		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				origin = rs.getString(1);
			}

			newList = origin.split(",");
			origin = "";

			int count=0;
            for(int i=0; i<newList.length; i++) {
               
               if(!newList[i].equals(clubId)) {
                  if(count==0)
                     origin+=newList[i];
                  else 
                     origin+=","+newList[i];
                  
                  count=1;
               }
               
               
            }
			pstmt = (PreparedStatement) conn.prepareStatement(nextSQL);

			pstmt.setString(1, origin);

			pstmt.executeUpdate();

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// 디비 오류
	}
}
