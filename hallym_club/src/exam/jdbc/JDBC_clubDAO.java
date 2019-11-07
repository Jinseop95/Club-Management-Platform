package exam.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import clubMember.clubMemberVo;
import exam.jdbc.ClubVO;

public class JDBC_clubDAO {

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

	public JDBC_clubDAO() {

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
			System.out.println(e + "=> error");
		}

	} // db_close

	public ArrayList<ClubVO> getClublist(String gb_cd, String search, String at_cd, int pageNumber) {

	      ArrayList<ClubVO> list = new ArrayList<ClubVO>();
	      String SQL = "";
	      // Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;

	      int row_count = 0;
	      try {
	         // "SELECT SQL_CALC_FOUND_ROWS * FROM CLUB WHERE CLUB_GB_CD LIKE ?
	         // AND CLUB_NM LIKE ?"
	         // + " AND CLUB_AT_CD LIKE ? ORDER BY CLUB_NM ASC LIMIT " +
	         // (pageNumber-1) * 4 +"," +4;
	         SQL = 
					"SELECT SQL_CALC_FOUND_ROWS * FROM"
							+ "( SELECT A.*, IF(B.STAFF_CD = '004001',B.NM, ''), IF(B.STAFF_CD = '004001',B.PHONE_NO, '') "
							+ "FROM CLUB AS A LEFT JOIN CLUB_MEMBER AS B " + "ON A.CLUB_ID = B.CLUB_ID "
							+ "WHERE A.CLUB_GB_CD LIKE ? AND A.CLUB_NM LIKE ? AND A.CLUB_AT_CD LIKE ? "
							+ "ORDER BY B.STAFF_CD IS NULL ASC, STAFF_CD ASC)X GROUP BY CLUB_ID LIMIT " + (pageNumber-1) * 4 +"," +4;
	         
	         
	         pstmt = con.prepareStatement(SQL);
	         pstmt.setString(1, "%" + gb_cd + "%");
	         pstmt.setString(2, "%" + search + "%");
	         pstmt.setString(3, "%" + at_cd + "%");

	         rs = pstmt.executeQuery();

	         while (rs.next()) {
	            ClubVO vo = new ClubVO();
	            row_count++;

	            vo.setClub_id(rs.getInt(1));
	            vo.setClub_nm(rs.getString(2));
	            vo.setClub_gb_cd(rs.getString(3));
	            vo.setClub_at_cd(rs.getString(4));
	            vo.setCnt(rs.getInt(5));
	            vo.setClub_aim(rs.getString(6));
	            vo.setClub_active(rs.getString(7));
	            vo.setClub_room(rs.getString(8));
	            vo.setOpen_dt(rs.getString(9));
	            vo.setIntro_cont(rs.getString(10));
	            vo.setIntro_file_nm(rs.getString(11));
	            vo.setIntro_file_path(rs.getString(12));
	            vo.setInput_id(rs.getString(14));
	            vo.setInput_ip(rs.getString(15));
	            vo.setInput_date(rs.getString(16));
	            vo.setPrefer(rs.getInt(20));
	            vo.setStaff_nm(rs.getString(21));
	            vo.setStaff_phone(rs.getString(22));
	            vo.setRow_count(row_count);
	            list.add(vo);
	         }

	         Statement st2 = con.createStatement();
	         ResultSet rs2 = st2.executeQuery("SELECT FOUND_ROWS()");

	         while (rs2.next()) {
	            row_count = rs2.getInt(1);
	            list.get(0).setRow_count(row_count);
	         }
	      } catch (Exception e) {

	      } finally {
	         db_close();
	      }
	      return list;
	   }
	
	   //우수 동아리 선정
	   
	   public ArrayList<ClubVO> getTopClub(String gb_cd, String at_cd) {

	      ArrayList<ClubVO> list = new ArrayList<ClubVO>();
	      String SQL = "";
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      
	      int limit_cnt = 3;

	      try {
	         if(at_cd.equals(""))
	            limit_cnt = 5;
	      
	         SQL = "SELECT * FROM (SELECT A.*, IF(B.STAFF_CD = '004001',B.NM, ''), IF(B.STAFF_CD = '004001',B.PHONE_NO, '') FROM CLUB AS A "
	               + "LEFT JOIN CLUB_MEMBER AS B " + "ON A.CLUB_ID = B.CLUB_ID "
	               + "WHERE A.CLUB_GB_CD LIKE ? AND A.CLUB_AT_CD LIKE ?"
	               + "ORDER BY B.STAFF_CD IS NULL ASC, STAFF_CD ASC)X "
	               + "GROUP BY CLUB_ID ORDER BY PREFER DESC, CLUB_CNT DESC LIMIT " + limit_cnt;

	         
	                  
	         pstmt = con.prepareStatement(SQL);
	         pstmt.setString(1, "%" + gb_cd + "%");
	         pstmt.setString(2, "%" + at_cd + "%");

	         rs = pstmt.executeQuery();

	         while (rs.next()) {
	            ClubVO vo = new ClubVO();

	            vo.setClub_id(rs.getInt(1));
	            vo.setClub_nm(rs.getString(2));
	            vo.setClub_gb_cd(rs.getString(3));
	            vo.setClub_at_cd(rs.getString(4));
	            vo.setCnt(rs.getInt(5));
	            vo.setClub_aim(rs.getString(6));
	            vo.setClub_active(rs.getString(7));
	            vo.setClub_room(rs.getString(8));
	            vo.setOpen_dt(rs.getString(9));
	            vo.setIntro_cont(rs.getString(10));
	            vo.setIntro_file_nm(rs.getString(11));
	            vo.setIntro_file_path(rs.getString(12));
	            vo.setInput_id(rs.getString(14));
	            vo.setInput_ip(rs.getString(15));
	            vo.setInput_date(rs.getString(16));
	            vo.setPrefer(rs.getInt(20));
	            vo.setStaff_nm(rs.getString(21));
	            vo.setStaff_phone(rs.getString(22));
	            list.add(vo);
	         }

	         
	      } catch (Exception e) {

	      } finally {
	         db_close();
	      }
	      return list;
	   }

	public String getProfessor(int club_id) {

		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String prof_name = "";
		try {
			SQL = "SELECT * FROM CLUB_PROF WHERE CLUB_ID = ? AND PROG_GB_CD = '006001'";

			pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, club_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				prof_name = rs.getString(3);
			}
		} catch (Exception e) {
		} finally {
			db_close();
		}
		return prof_name;
	}



	public ArrayList<ClubVO> getboardwriteClub(String student_id) {

		ArrayList<ClubVO> clubList = null;
		String SQL = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SQL = "SELECT * FROM CLUB WHERE CLUB_ID = ANY(SELECT CLUB_ID FROM CLUB_MEMBER WHERE STUDENT_ID = ? AND JOIN_CD = 008001)";

			pstmt = con.prepareStatement(SQL);
			pstmt.setString(1, student_id);

			rs = pstmt.executeQuery();

			clubList = new ArrayList<ClubVO>();

			while (rs.next()) {
				ClubVO vo = new ClubVO();
				vo.setClub_id(rs.getInt(1));
				vo.setClub_nm(rs.getString(2));
				clubList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("database fail");
		} finally {
			db_close();
		}
		return clubList;
	}

	public int addsub_prefer(int club_id, String state) {
		String SQL = "";

		if (state.equals("0")) {
			SQL = "UPDATE club SET prefer=prefer+1 where club_id=" + club_id;
		}

		else
			SQL = "UPDATE club SET prefer=prefer-1 where club_id=" + club_id;

		try {

			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
			pstmt.executeUpdate();

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}

		return -1;
	}

	public String[] recommend_dong(String AT_CD) {

		String[] recommend = new String[3];
		String SQL = "select club_nm from club where CLUB_AT_CD=" + AT_CD + " order by prefer desc limit 3;";
		ResultSet rs = null;
		int i = 0;
		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				recommend[i] = rs.getString(1);
				i++;
			}

			if (i != 3)
				return null;

			return recommend;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			db_close();
		}

		return null;
	}

	public int createClub(ClubVO CV, String student_id, String fileName, String fileSaveName) {

		String SQL = "INSERT INTO club(CLUB_ID, CLUB_NM, CLUB_GB_CD, CLUB_AT_CD, CLUB_CNT, CLUB_AIM, CLUB_ACTIVE, CLUB_ROOM, "
				+ "OPEN_DT, INTRO_FILE_NM, INTRO_SAVE_FILE_NM, PREFER) VALUES((SELECT ifnull(MAX(CLUB_ID),0)+1 FROM CLUB AS C),?,?,?,?,?,?,?,?,?,?,0)";
		
		String SQL2 = "INSERT INTO CLUB_MEMBER(CLUB_ID, STUDENT_ID, STAFF_CD, JOIN_DT, JOIN_CD) VALUES "
				+ "((SELECT ifnull(MAX(CLUB_ID),0) FROM CLUB AS C),?,'004001',?,'008001')";
		try {

			if (CV.getClub_aim() == null) {
				CV.setClub_aim("");
			}
			if (CV.getClub_active() == null) {
				CV.setClub_active("");
			}
			if (CV.getClub_room() == null) {
				CV.setClub_room("");
			}
			if (CV.getOpen_dt() == null) {
				CV.setOpen_dt("");
			}
			
			if(fileName == null){
				fileName = "";
			}
			if(fileSaveName == null){
				fileSaveName = "";
			}

			PreparedStatement pstmt = con.prepareStatement(SQL);
			
			pstmt.setString(1, CV.getClub_nm());
			pstmt.setString(2, CV.getClub_gb_cd());
			pstmt.setString(3, CV.getClub_at_cd());
			pstmt.setInt(4,1);
			pstmt.setString(5, CV.getClub_aim());
			pstmt.setString(6, CV.getClub_active());
			pstmt.setString(7, CV.getClub_room());
			pstmt.setString(8, CV.getOpen_dt());
			pstmt.setString(9, fileName);
			pstmt.setString(10, fileSaveName);
			
			System.out.println("여기"+ pstmt);
		    pstmt.executeUpdate();
			
			
			pstmt = con.prepareStatement(SQL2);
				
			pstmt.setString(1, student_id);
			
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
			String today = sdf.format(dt);
			
			pstmt.setString(2, today);
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}

		return -1; // 데이터베이스 오류

	}
	

	public String getIntro_FilePath(String clubNM) {
		String SQL = "select INTRO_FILE_PATH from club where CLUB_NM='" + clubNM + "'";
		PreparedStatement pstmt;
		String Intro_FilePath = "";

		try {
			pstmt = (PreparedStatement) con.prepareStatement(SQL);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Intro_FilePath = rs.getString(1);
				return Intro_FilePath;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	}

	public ArrayList<String[]> getItems(int club_id) {
		String sql = "SELECT ITEM_NM,ITEM_CONT,TOT_CNT from item where CLUB_ID=" + club_id + " ORDER BY ITEM_NM";
		ArrayList<String[]> allClubItem = new ArrayList<>();

		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String clubItem[] = new String[3];
				clubItem[0] = rs.getString(1);
				clubItem[1] = rs.getString(2);
				clubItem[2] = rs.getString(3);
				allClubItem.add(clubItem);
			}
			return allClubItem;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	}
	
	   // intro, 동아리 조회
	   public ArrayList<ClubVO> getClubIntro(String student_id) {

	      ArrayList<ClubVO> clubList = null;
	      String SQL = "";
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      try {
	         if (student_id == "") {
	            SQL = "SELECT C.CLUB_ID, C.CLUB_NM, C.INTRO_FILE_NM, C.CLUB_AIM, C.CLUB_ACTIVE, S.SO_NM, D.SO_NM FROM CLUB AS C "
	                  + "JOIN SO_CD AS S ON C.CLUB_GB_CD = S.SO_CD JOIN SO_CD AS D ON C.CLUB_AT_CD = D.SO_CD "+
	                  "ORDER BY C.PREFER DESC, C.CLUB_CNT DESC LIMIT 6";

	            pstmt = con.prepareStatement(SQL);

	            rs = pstmt.executeQuery();

	            clubList = new ArrayList<ClubVO>();

	            while (rs.next()) {
	               ClubVO vo = new ClubVO();
	               vo.setClub_id(rs.getInt(1));
	               vo.setClub_nm(rs.getString(2));
	               vo.setIntro_file_nm(rs.getString(3));
	               vo.setClub_aim(rs.getString(4));
	               vo.setClub_active(rs.getString(5));
	               vo.setClub_gb_cd(rs.getString(6));
	               vo.setClub_at_cd(rs.getString(7));
	               clubList.add(vo);
	            }

	         } else {
	            SQL = 
	                  
	            "SELECT A.CLUB_ID, A.CLUB_NM, A.INTRO_FILE_NM, IF(S.SO_NM IS NULL, '회원', S.SO_NM), GB.SO_NM, AT.SO_NM " +
	            "FROM CLUB AS A JOIN CLUB_MEMBER AS B ON A.CLUB_ID = B.CLUB_ID " + 
	            "LEFT JOIN SO_CD AS S ON S.SO_CD = B.STAFF_CD " +
	            "JOIN SO_CD AS GB ON A.CLUB_GB_CD = GB.SO_CD JOIN SO_CD AS AT ON A.CLUB_AT_CD = AT.SO_CD " +
	            "WHERE STUDENT_ID = ? AND JOIN_CD = '008001' ORDER BY B.STAFF_CD IS NULL, B.STAFF_CD " ;
	          
	            
	            
	            pstmt = con.prepareStatement(SQL);
	            pstmt.setString(1, student_id);

	            rs = pstmt.executeQuery();

	            clubList = new ArrayList<ClubVO>();

	            while (rs.next()) {
	               ClubVO vo = new ClubVO();
	               vo.setClub_id(rs.getInt(1));
	               vo.setClub_nm(rs.getString(2));
	               vo.setIntro_file_nm(rs.getString(3));
	               vo.setStaff_cd(rs.getString(4));
	               vo.setClub_gb_cd(rs.getString(5));
	               vo.setClub_at_cd(rs.getString(6));
	               clubList.add(vo);
	            }
	         }

	      } catch (Exception e) {
	         System.out.println("database fail");
	      } finally {
	         db_close();
	      }
	      return clubList;
	   }
	   
	public String getOpen_Dt(int club_id) {
		String sql = "SELECT OPEN_DT from club where CLUB_ID=" + club_id + ";";
		String open_dt = "";

		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				open_dt = rs.getString(1);
			}
			return open_dt;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	} // 동아리 개설일 가져오기

	public String getMaster(int club_id) {
		String sql = "SELECT NM from club_member where CLUB_ID=" + club_id + " and STAFF_CD =" + "004001" + ";";
		String open_dt = "";

		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				open_dt = rs.getString(1);
			}
			return open_dt;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	} // 동아리 개설일 가져오기

	public String getClubNMs(int club_id) {
		String SQL = "SELECT CLUB_NM FROM club where club_id=" + club_id;
		String answer = "";
		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				answer = rs.getString(1);
			}

			return answer;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;// 디비 오류

	}

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
		}finally {
			db_close();
		}
		return answer;// 디비 오류
	}

	public ArrayList<String> getMyJoinClubList(String user_id) {
		ArrayList<String> allMyClub = new ArrayList<>();
		String sql = "SELECT CLUB_ID from club_member where STUDENT_ID = " + user_id + " and JOIN_CD = 008001;";
		
		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String clubNM = getClubNMs(rs.getInt(1));
				allMyClub.add(clubNM);
			}
			return allMyClub;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	}
	
	public ArrayList<String> getMyWaitClubList(String user_id) {
		ArrayList<String> allMyClub = new ArrayList<>();
		String sql = "SELECT CLUB_ID from club_member where STUDENT_ID = " + user_id + " and JOIN_CD = 008003;";
		
		try {
			PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String clubNM = getClubNMs(rs.getInt(1));
				allMyClub.add(clubNM);
			}
			return allMyClub;

		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db_close();
		}
		return null;
	}
	
	public int getJoin_cd(String username,String clubNM) {
	       String SQL = "SELECT STAFF_CD FROM club_member where STUDENT_ID="+username+" AND CLUB_ID=( SELECT CLUB_ID from club where CLUB_NM ='"+clubNM+"')";

	       try {
		         PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(SQL);
		         ResultSet rs = pstmt.executeQuery();
		         
		         if(rs.next()) {
		            if(rs.getString(1).equals("004001") || rs.getString(1).equals("004002"))
		               return 0;
		         }
		         
		         return 1;
		      } catch (Exception e) {
		         e.printStackTrace();
		      }finally {
					db_close();
				}
		      return -1;//디비 오류
		      
	      }
	
	// 1005 추가
	public ArrayList<ClubVO> getClubInfo(int club_id) {

		ArrayList<ClubVO> clubList = null;
		String SQL = "";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SQL = "SELECT CLUB_ID, CLUB_NM, CLUB_GB_CD, CLUB_AT_CD, CLUB_CNT, CLUB_AIM, CLUB_ACTIVE, CLUB_ROOM, OPEN_DT, INTRO_FILE_NM, INTRO_FILE_PATH "
					+ "FROM CLUB WHERE CLUB_ID = " + club_id ;
		

			pstmt = con.prepareStatement(SQL);

			rs = pstmt.executeQuery();

			clubList = new ArrayList<ClubVO>();

			while (rs.next()) {
				ClubVO vo = new ClubVO();
				vo.setClub_id(rs.getInt(1));
				vo.setClub_nm(rs.getString(2));
				vo.setClub_gb_cd(rs.getString(3));
				vo.setClub_at_cd(rs.getString(4));
				vo.setCnt(rs.getInt(5));
				vo.setClub_aim(rs.getString(6));
				vo.setClub_active(rs.getString(7));
				vo.setClub_room(rs.getString(8));
				vo.setOpen_dt(rs.getString(9));
				vo.setIntro_file_nm(rs.getString(10));
				vo.setIntro_file_path(rs.getString(11));
				
				clubList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("database fail");
		} finally {
			db_close();
		}
		return clubList;
	}
	//1005 추가
	public int updateClub(ClubVO CV) {

		String SQL = 	"UPDATE CLUB SET CLUB_GB_CD = ? , CLUB_AT_CD = ?, CLUB_CNT = ?, CLUB_AIM = ?, CLUB_ACTIVE = ?, "
				+ " CLUB_ROOM = ?, OPEN_DT = ? ";	
		
//		"UPDATE CLUB SET CLUB_NM = ?, CLUB_GB_CD = ? , CLUB_AT_CD = ?, CLUB_CNT = ?, CLUB_AIM = ?, CLUB_ACTIVE = ?, "
//		+ " CLUB_ROOM = ?, OPEN_DT = ?, INTRO_FILE_NM = ?, INTRO_FILE_PATH = ? WHERE CLUB_ID = ?";
		
		try {

			if (CV.getClub_aim() == null) {
				CV.setClub_aim("");
			}
			if (CV.getClub_active() == null) {
				CV.setClub_active("");
			}
			if (CV.getClub_room() == null) {
				CV.setClub_room("");
			}
			if (CV.getOpen_dt() == null) {
				CV.setOpen_dt("");
			}

			if (CV.getIntro_file_nm() != null) {
				SQL = SQL + ", INTRO_FILE_NM = " + CV.getIntro_file_nm();
			}
			if (CV.getIntro_file_path() != null) {
				SQL = SQL + ", INTRO_FILE_PATH = " + CV.getIntro_file_path();
			}

			SQL = SQL + " WHERE CLUB_ID = "  + CV.getClub_id();
			
			PreparedStatement pstmt = con.prepareStatement(SQL);

			pstmt.setString(1, CV.getClub_gb_cd());
			pstmt.setString(2, CV.getClub_at_cd());
			pstmt.setInt(3, CV.getCnt());
			pstmt.setString(4, CV.getClub_aim());
			pstmt.setString(5, CV.getClub_active());
			pstmt.setString(6, CV.getClub_room());
			pstmt.setString(7, CV.getOpen_dt());
			
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}

		return -1; // 데이터베이스 오류

	}
	public int addStar(int club_id, String student_id) {
		String SQL = "UPDATE CLUB_MEMBER SET STAR = 'Y' WHERE CLUB_ID = ? AND STUDENT_ID = ? ";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, club_id);
			pstmt.setString(2, student_id);
			
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int deleteStar(int club_id, String student_id) {
		String SQL = "UPDATE CLUB_MEMBER SET STAR = 'N' WHERE CLUB_ID = ? AND STUDENT_ID = ? ";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, club_id);
			pstmt.setString(2, student_id);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return -1; // 데이터베이스 오류
	}
	public String getStar(int club_id, String student_id) {
		String SQL = "SELECT STAR FROM CLUB_MEMBER WHERE CLUB_ID = ? AND STUDENT_ID = ? ";
		String str = "N";
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, club_id);
			pstmt.setString(2, student_id);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				str = rs.getString(1);
			}else if(rs.wasNull()){
				str = "M";
			}else{
				str = "N";
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return null; // 데이터베이스 오류
	}
	public int getStarCnt(int club_id) {
		String SQL = "SELECT COUNT(STAR) FROM CLUB_MEMBER WHERE CLUB_ID = ? AND STAR = 'Y' ";
		int cnt = 0;
		try {
			PreparedStatement pstmt = con.prepareStatement(SQL);
			pstmt.setInt(1, club_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()){
				cnt = rs.getInt(1);
			}else{
				
			}
			return cnt;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return -1; // 데이터베이스 오류
	}
}
