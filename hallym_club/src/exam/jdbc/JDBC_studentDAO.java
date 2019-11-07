package exam.jdbc;
 
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Student.StudentVO;
 
public class JDBC_studentDAO {
 
    Connection con;
    Statement st;
    PreparedStatement ps;
    ResultSet rs;
   
    //MySQL
    String driverName="com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost/mydb";
 
    /*
    //ORACLE
    String driverName="oracle.jdbc.driver.OracleDriver";
    String url = " jdbc:oracle:thin:@localhost:1521:ORCL";
    */
    String id = "root";
    //String pwd ="k1234";
    String pwd ="2865";
 
    public JDBC_studentDAO(){
       
        try {
            
            Class.forName(driverName);
           
            
            con = DriverManager.getConnection(url,id,pwd);      
           
        } catch (ClassNotFoundException e) {
           
            System.out.println(e+"=> fail");
           
        } catch (SQLException e) {
           
            System.out.println(e+"=> fail");
        }
    }//JDBC_studentDAO()
   

    public void db_close(){
       
        try {
           
            if (rs != null ) rs.close();
            if (ps != null ) ps.close();      
            if (st != null ) st.close();
       
        } catch (SQLException e) {
            System.out.println(e+"=> fail");
        }      
       
    } //db_close
   

    public int studentInsert(StudentVO vo){
        int result = 0;
        
        java.util.Date dt = new java.util.Date();

        java.text.SimpleDateFormat sdf = 
             new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);

        
        try{
        
            String sql = "INSERT INTO STUDENT (`STUDENT_ID`, `PASSWORD`, `INPUT_ID`,`INPUT_IP`, `INPUT_DATE`) VALUES(?,?,?,?,?)";
           
            ps = con.prepareStatement(sql);
            ps.setString(1, vo.getUsername());
            ps.setString(2, vo.getPassword());
            ps.setString(3, vo.getUsername());
            ps.setString(4, Inet4Address.getLocalHost().getHostAddress());
            ps.setString(5, currentTime);
            
            result = ps.executeUpdate();
           
        }catch (Exception e){
           
            System.out.println(e + "=> studentInsert fail");
           
        }finally{
            db_close();
        }
       
        return result;
    }//studentInsert
    
    public boolean check(String id){
       boolean result = false;
       
       try{
          String sql = "select student_id from student where student_id=?";
          ps = con.prepareStatement(sql);
          ps.setString(1, id);
          rs = ps.executeQuery();
          while(rs.next()){
             result = true;
             }
       }catch(Exception e){
          e.printStackTrace();
       }finally{          
            db_close();
        }  
       return result;
    }
    

    public ArrayList<StudentVO> getStudentlist(){
       
        ArrayList<StudentVO> list = new ArrayList<StudentVO>();
      
        
        try{//占쏙옙占쏙옙
            st = con.createStatement();
            rs =  st.executeQuery("select * from STUDENT");
            //rs = st.executeQuery("select * from Student limit 2,4");
           
            while(rs.next()){
                StudentVO vo = new StudentVO();
               
                vo.setUsername(rs.getString(1));
                vo.setPassword(rs.getString(2));
                vo.setInput_ip(rs.getString(4));
                vo.setInput_date(rs.getString(5));
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getStudentVOlist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getStudentVOlist
   
  
    public ArrayList<StudentVO> getStudentlist(int page, int limit){
        
        ArrayList<StudentVO> list = new ArrayList<StudentVO>();
       
        try{
            st = con.createStatement();
            //rs =  st.executeQuery("select * from MEMBER");
            rs = st.executeQuery("select * from Student limit ?,?");
           
            while(rs.next()){
               StudentVO vo = new StudentVO();
               
                vo.setUsername(rs.getString(1));
                vo.setPassword(rs.getString(2));
               
                list.add(vo);
            }
        }catch(Exception e){          
            System.out.println(e+"=> getStudentlist fail");        
        }finally{          
            db_close();
        }      
        return list;
    }//getStudentlist
}