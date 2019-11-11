package clubMember;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

import exam.jdbc.ClubVO;

public class clubMemberDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	Statement st;

	public clubMemberDAO() {
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

	// 동아리 가입
	public int writeMember(clubMemberVo CM) {
		String SQL = "INSERT INTO club_member(CLUB_ID,STUDENT_ID,NM,MAJOR,GRADE,GENDER_CD,PHONE_NO,ADDRESS,EMAIL,JOIN_CD,BIRTH_DT,plan,hope) VALUES(?,?,?,?,?,?,?,?,?,'008003',?,?,?)";
        

		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);

			pstmt.setInt(1, CM.getCLUB_ID());
			pstmt.setString(2, CM.getSTUDENT_ID());
			pstmt.setString(3, CM.getNM());
			pstmt.setString(4, CM.getMAJOR());
			pstmt.setString(5, CM.getGRADE());
			pstmt.setString(6, CM.getGENDER_CD());
			pstmt.setString(7, CM.getPHONE_NO());
			pstmt.setString(8, CM.getADDRESS());
			pstmt.setString(9, CM.getEMAIL());
			pstmt.setString(10, CM.getBIRTH_DT());
			pstmt.setString(11, CM.getPlan());
			pstmt.setString(12, CM.getHope());

			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}

		return -1; // 데이터베이스 오류

	}

	public ArrayList<clubMemberVo> club_getMember(int CLUB_ID, String join_cd) {

		ArrayList<clubMemberVo> clubList = null;
		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SQL = "SELECT * FROM CLUB_MEMBER WHERE CLUB_ID = ? AND JOIN_CD = ? ORDER BY ISNULL(STAFF_CD) ASC, STAFF_CD ASC";
			
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, CLUB_ID);
			pstmt.setString(2, join_cd);

			rs = pstmt.executeQuery();

			clubList = new ArrayList<clubMemberVo>();

			while (rs.next()) {
				clubMemberVo vo = new clubMemberVo();
				vo.setCLUB_ID(rs.getInt(1));
				vo.setSTUDENT_ID(rs.getString(2));
				vo.setNM(rs.getString(3));
				vo.setMAJOR(rs.getString(4));
				vo.setGRADE(rs.getString(5));
				vo.setGENDER_CD(rs.getString(6));
				vo.setSTAFF_CD(rs.getString(7));
				vo.setPHONE_NO(rs.getString(8));
				vo.setADDRESS(rs.getString(10));
				vo.setEMAIL(rs.getString(12));
				vo.setJoin_dt(rs.getString(13));
				vo.setBIRTH_DT(rs.getString(14));
				vo.setJOIN_CD(rs.getString(16));
				vo.setPlan(rs.getString(23));
				vo.setHope(rs.getString(24));

				clubList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("database fail");
		} finally {
			db_close();
		}
		return clubList;
	}
	//메소드 오버로딩
	public ArrayList<clubMemberVo> getMember(int CLUB_ID, String join_cd,String category, String search, int pageNumber) {

		ArrayList<clubMemberVo> clubList = null;
		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			
			SQL = "SELECT SQL_CALC_FOUND_ROWS * FROM CLUB_MEMBER WHERE CLUB_ID = ? AND JOIN_CD = ? AND " + category + " LIKE ? ORDER BY "
					+ "ISNULL(STAFF_CD) ASC, STAFF_CD ASC LIMIT " + (pageNumber-1) * 10 +"," +10;
			
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, CLUB_ID);
			pstmt.setString(2, join_cd);
			pstmt.setString(3, "%" + search + "%");
			rs = pstmt.executeQuery();

			clubList = new ArrayList<clubMemberVo>();

			while (rs.next()) {
				clubMemberVo vo = new clubMemberVo();
				vo.setCLUB_ID(rs.getInt(1));
				vo.setSTUDENT_ID(rs.getString(2));
				vo.setNM(rs.getString(3));
				vo.setMAJOR(rs.getString(4));
				vo.setGRADE(rs.getString(5));
				vo.setGENDER_CD(rs.getString(6));
				vo.setSTAFF_CD(rs.getString(7));
				vo.setPHONE_NO(rs.getString(8));
				vo.setADDRESS(rs.getString(10));
				vo.setEMAIL(rs.getString(12));
				vo.setJoin_dt(rs.getString(13));
				vo.setBIRTH_DT(rs.getString(14));
				vo.setJOIN_CD(rs.getString(16));
				vo.setPlan(rs.getString(23));
				vo.setHope(rs.getString(24));
				clubList.add(vo);
			}
			Statement st2 = conn.createStatement();
    		ResultSet rs2 = st2.executeQuery("SELECT FOUND_ROWS()");
		
    		while(rs2.next()){
        		clubList.get(0).setRow_count(rs2.getInt(1));
    		}
    		
		} catch (Exception e) {
			
		} finally {
			db_close();
		}
		return clubList;
	}
	
	public ArrayList<clubMemberVo> getMember(int CLUB_ID, String join_cd,String category, String search) {

		ArrayList<clubMemberVo> clubList = null;
		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
			
			SQL = "SELECT * FROM CLUB_MEMBER WHERE CLUB_ID = ? AND JOIN_CD = ? AND " + category + " LIKE ? ORDER BY "
					+ "ISNULL(STAFF_CD) ASC, STAFF_CD ASC";
			
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, CLUB_ID);
			pstmt.setString(2, join_cd);
			pstmt.setString(3, "%" + search + "%");
			rs = pstmt.executeQuery();

			clubList = new ArrayList<clubMemberVo>();

			while (rs.next()) {
				clubMemberVo vo = new clubMemberVo();
				vo.setCLUB_ID(rs.getInt(1));
				vo.setSTUDENT_ID(rs.getString(2));
				vo.setNM(rs.getString(3));
				vo.setMAJOR(rs.getString(4));
				vo.setGRADE(rs.getString(5));
				vo.setGENDER_CD(rs.getString(6));
				vo.setSTAFF_CD(rs.getString(7));
				vo.setPHONE_NO(rs.getString(8));
				vo.setADDRESS(rs.getString(10));
				vo.setEMAIL(rs.getString(12));
				vo.setJoin_dt(rs.getString(13));
				vo.setBIRTH_DT(rs.getString(14));
				vo.setJOIN_CD(rs.getString(16));
				vo.setPlan(rs.getString(23));
				vo.setHope(rs.getString(24));
				clubList.add(vo);
			}
			    		
		} catch (Exception e) {
			
		} finally {
			db_close();
		}
		return clubList;
	}
	
	
	public ArrayList<clubMemberVo> getMember(int CLUB_ID, String student_id) {
		ArrayList<clubMemberVo> clubList = null;
		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SQL = "SELECT * FROM CLUB_MEMBER WHERE CLUB_ID = ? AND STUDENT_ID = ?";
			
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, CLUB_ID);
			pstmt.setString(2, student_id);

			rs = pstmt.executeQuery();

			clubList = new ArrayList<clubMemberVo>();

			while (rs.next()) {
				clubMemberVo vo = new clubMemberVo();
				vo.setCLUB_ID(rs.getInt(1));
				vo.setSTUDENT_ID(rs.getString(2));
				vo.setNM(rs.getString(3));
				vo.setMAJOR(rs.getString(4));
				vo.setGRADE(rs.getString(5));
				vo.setGENDER_CD(rs.getString(6));
				vo.setSTAFF_CD(rs.getString(7));
				vo.setPHONE_NO(rs.getString(8));
				vo.setADDRESS(rs.getString(10));
				vo.setEMAIL(rs.getString(12));
				vo.setJoin_dt(rs.getString(13));
				vo.setBIRTH_DT(rs.getString(14));
				vo.setJOIN_CD(rs.getString(16));
				vo.setPlan(rs.getString(23));
				vo.setHope(rs.getString(24));

				clubList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("database fail");
		} finally {
			db_close();
		}
		return clubList;
	}
	
	//추가
	public ArrayList<clubMemberVo> allMember(int CLUB_ID) {
		ArrayList<clubMemberVo> clubList = null;
		String SQL = "";
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			SQL = "SELECT * FROM CLUB_MEMBER WHERE CLUB_ID = ?";
			
			pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, CLUB_ID);

			rs = pstmt.executeQuery();

			clubList = new ArrayList<clubMemberVo>();

			while (rs.next()) {
				clubMemberVo vo = new clubMemberVo();
				vo.setCLUB_ID(rs.getInt(1));
				vo.setSTUDENT_ID(rs.getString(2));
				vo.setNM(rs.getString(3));
				vo.setMAJOR(rs.getString(4));
				vo.setGRADE(rs.getString(5));
				vo.setGENDER_CD(rs.getString(6));
				vo.setSTAFF_CD(rs.getString(7));
				vo.setPHONE_NO(rs.getString(8));
				vo.setADDRESS(rs.getString(10));
				vo.setEMAIL(rs.getString(12));
				vo.setJoin_dt(rs.getString(13));
				vo.setBIRTH_DT(rs.getString(14));
				vo.setJOIN_CD(rs.getString(16));
				vo.setPlan(rs.getString(23));
				vo.setHope(rs.getString(24));

				clubList.add(vo);
			}

		} catch (Exception e) {
			System.out.println("database fail");
		} finally {
			db_close();
		}
		return clubList;
	}
	
	// join_cd 변경 (승인)
	public int update(int club_id, String student_id) {
		String SQL = "UPDATE club_member SET JOIN_DT = ? , JOIN_CD = '008001' WHERE CLUB_ID = ? AND STUDENT_ID = ?";
		
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = 
             new java.text.SimpleDateFormat("yyyyMMdd");

        String join_dt = sdf.format(dt);
        
		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);

			pstmt.setString(1, join_dt);
			pstmt.setInt(2, club_id);
			pstmt.setString(3, student_id);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		SQL = "UPDATE club SET club_cnt = club_cnt+1 where  CLUB_ID = ?";

		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);

			pstmt.setInt(1, club_id);

			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally { 
			  db_close(); 
			  }
		return -1; // 데이터베이스 오류
	}
	
	// staff_cd 변경
	public int update(int club_id, String student_id, String staff_cd) {
		
		String SQL = "UPDATE CLUB_MEMBER SET STAFF_CD = ?  WHERE CLUB_ID = ? AND STUDENT_ID = ?";
		       
		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			
			pstmt.setString(1, staff_cd);
			pstmt.setInt(2, club_id);
			pstmt.setString(3, student_id);

			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db_close();
		}
		return -1; // 데이터베이스 오류
	}	
	// 회원 가입 거부(삭제)
	public int delete(int club_id, String student_id, String distinction) {
		String SQL = "DELETE FROM CLUB_MEMBER WHERE CLUB_ID = ? AND STUDENT_ID = ?";
		int answer=0;
		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);

			pstmt.setInt(1, club_id);
			pstmt.setString(2, student_id);
			
			answer = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(distinction.equals("2")) {
			SQL = "UPDATE club SET club_cnt = club_cnt-1 where  CLUB_ID = ?";
			
			try {
	
				PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
	
				pstmt.setInt(1, club_id);
	
				return pstmt.executeUpdate();
	
			} catch (Exception e) {
				e.printStackTrace();
			}finally { 
				  db_close(); 
				  }
			}
		else {
			return answer;
		}
		return -1; // 데이터베이스 오류
	}
	
	public List<String> get_MyApplyList(String username,int check) {
		String SQL = "";
		if(check == 0)
			SQL = "SELECT CLUB_NM from club WHERE CLUB_ID = ANY(SELECT CLUB_ID from club_member where Student_ID= "+username+" AND JOIN_CD='008003')";
		else if(check ==1)
			SQL = "SELECT CLUB_NM from club WHERE CLUB_ID = ANY(SELECT CLUB_ID from club_member where Student_ID= "+username+" AND JOIN_CD='008001')";
		
		List<String> apply = new ArrayList<String>();

		try {
			
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				apply.add(rs.getString(1));
			}
			
			return apply;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null; //데이터베이스 오류
	}
	
	public int removeApply(String username,int club_id) { 
		String SQL = "DELETE from club_member where STUDENT_ID= ? AND CLUB_ID = ?";
////		delete from 테이블명 [where 검색조건]; 
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setString(1,username);
			pstmt.setInt(2,club_id);
			
			pstmt.executeUpdate();
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SQL = "UPDATE club SET club_cnt = club_cnt-1 where CLUB_ID = ?";
				
		try {

			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			pstmt.setInt(1, club_id);
			
			pstmt.executeUpdate();
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}finally { 
			  db_close(); 
			  }
		
		return -1;//오류
		
	}
	
	public String[] getUserForm(String username,int club_id) {
		
		String SQL = "SELECT NM,MAJOR,GRADE,GENDER_CD,PHONE_NO,ADDRESS,EMAIL,BIRTH_DT,plan,hope from club_member where STUDENT_ID="+username+" AND CLUB_ID="+club_id;
		
		String myInfo[] = new String[10];
		
		try {
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			myInfo[0]=rs.getString(1); //성명
			myInfo[1]=rs.getString(2); //전공
			myInfo[2]=rs.getString(3); //학년
			myInfo[3]=rs.getString(4); //성별 코드 
			myInfo[4]=rs.getString(5); //폰번호
			myInfo[5]=rs.getString(6); //주소
			myInfo[6]=rs.getString(7); //이메일
			myInfo[7]=rs.getString(8); //생년 월일
			myInfo[8]=rs.getString(9); //계획
			myInfo[9]=rs.getString(10); //바라는점
		}
		return myInfo;
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;//오류
	}
	
	public int modMember(clubMemberVo CM,String username,String clubNM) {
		String SQL = "UPDATE club_member SET NM=?,MAJOR=?,GRADE=?,GENDER_CD=?,PHONE_NO=?,ADDRESS=?,EMAIL=?,BIRTH_DT=?,plan=?,hope=? where STUDENT_ID="+username+" AND CLUB_ID=( SELECT CLUB_ID from club where CLUB_NM ='"+clubNM+"')";
				
		try {
		PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
		
		pstmt.setString(1, CM.getNM());
		pstmt.setString(2, CM.getMAJOR());
		pstmt.setString(3, CM.getGRADE());
		pstmt.setString(4, CM.getGENDER_CD());
		pstmt.setString(5, CM.getPHONE_NO());
		pstmt.setString(6, CM.getADDRESS());
		pstmt.setString(7, CM.getEMAIL());
		pstmt.setString(8, CM.getBIRTH_DT());
		pstmt.setString(9, CM.getPlan());
		pstmt.setString(10, CM.getHope());
		
		pstmt.executeUpdate();

		return 1;
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return -1;//오류
	}
	
	public int overlapCheck(String username,String clubNM ) {
	      String SQL = "SELECT JOIN_CD FROM club_member where STUDENT_ID="+username+" AND CLUB_ID=( SELECT CLUB_ID from club where CLUB_NM ='"+clubNM+"')";

	      try {
	         PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
	         ResultSet rs = pstmt.executeQuery();
	         
	         if(rs.next()) {
	            if(rs.getString(1).equals("008003"))
	               return 0;
	            else if(rs.getString(1).equals("008001"))
	               return 1;
	         }
	         
	         return 2;
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return -1;//디비 오류
	      
	   }
	   public int writeNews(String student_id,String update) {
		   String SQL = "SELECT news from student where STUDENT_ID="+student_id;
		   String nextSQL = "UPDATE student SET news=? where STUDENT_ID="+student_id;
		   String origin="";
		   
		   try {
		      PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
		      ResultSet rs = pstmt.executeQuery();
		      
		      if(rs.next()) {
		         origin = rs.getString(1);
		      }
		      
		      pstmt = (PreparedStatement) conn.prepareStatement(nextSQL);
		      
		      if(origin==null || origin.equals(""))
		         pstmt.setString(1,update);
		      else
		         pstmt.setString(1,origin+","+update);
		      
		      pstmt.executeUpdate();
		      
		      return 1;
		   } catch (Exception e) {
		      e.printStackTrace();
		   }
		   return -1;//디비 오류
		}
		   
	   public String[] ViewNews(String username) {
		      String SQL = "SELECT news from student where STUDENT_ID="+username;
		      String view="";
		      String viewList[] = null;
		      
		      try {
		         PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
		         ResultSet rs = pstmt.executeQuery();

		         if(rs.next()) {
		            view = rs.getString(1);
		            viewList = view.split(",");
		            
		            if(viewList.length==6) {
		               view=viewList[1];
		               int count=0;
		               for(int i=2; i<6; i++) {
		                  view+=","+viewList[i];
		               }
		            
		               String nextSQL = "UPDATE student SET news=? where STUDENT_ID="+username;
		               try {
		                  pstmt = (PreparedStatement) conn.prepareStatement(nextSQL);
		   
		                  pstmt.setString(1,view);
		                  
		                  pstmt.executeUpdate();
		                  
		               } catch (Exception e) {
		                  e.printStackTrace();
		               }
		            }
		            viewList = view.split(",");
		         };

		         
		         return viewList;
		      } catch (Exception e) {
		         e.printStackTrace();
		      }
		      
		      return null;//값 없음.
		      
		   }
		   public String getClubNMs(int club_id) {
		       String SQL = "SELECT CLUB_NM FROM club where club_id="+club_id;
		       String answer="";
		         try {
		            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
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
		   
		   public int getClubIds(String club_NM) {
		       String SQL = "SELECT CLUB_ID FROM club where CLUB_NM='"+club_NM+"'";
		       int answer=-1;
		         try {
		            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
		            ResultSet rs = pstmt.executeQuery();
		            
		            if(rs.next()) {
		                answer = rs.getInt(1);
		            }
		            
		            return answer;
		         } catch (Exception e) {
		            e.printStackTrace();
		         }
		         return answer;//디비 오류
		      }
		   
		   public int getJoin_cd(String username,int club_id) {
		       String SQL = "SELECT STAFF_CD FROM club_member where STUDENT_ID="+username+" AND CLUB_ID="+club_id;

		       try {
			         PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(SQL);
			         ResultSet rs = pstmt.executeQuery();
			         
			         if(rs.next()) {
			            if(rs.getString(1).equals("004001") || rs.getString(1).equals("004002"))
			               return 0;
			         }
			         
			         return 1;
			      } catch (Exception e) {
			         e.printStackTrace();
			      }
			      return -1;//디비 오류
			      
		      }
}
