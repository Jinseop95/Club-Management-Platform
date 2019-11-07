package budget;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BudgetDAO {
	Connection con;
	Statement st;
	PreparedStatement ps;
	ResultSet rs;

	// MySQL
	String driverName = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost/mydb";

	/*
	 * //ORACLE String driverName="oracle.jdbc.driver.OracleDriver"; String url
	 * = " jdbc:oracle:thin:@localhost:1521:ORCL";
	 */
	String id = "root";
	String pwd = "2865";

	public BudgetDAO() {

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

	public String getTotal(int club_id, String io_gb_cd) {

		String sql = "";
		String total = "";
		sql = "select SUM(PRICE) FROM BUDGET WHERE CLUB_ID =" + club_id + " AND IO_GB_CD LIKE '" + io_gb_cd + "%'";

		try {

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				DecimalFormat formatter = new DecimalFormat("###,###");
				total = formatter.format(rs.getInt(1));
				;

			}

		} catch (Exception e) {
			System.out.println(e + "=> getBudget fail");
		} finally {
			db_close();
		}
		return total;
	}

	public int getTotal(int club_id, String io_gb_cd, int year, int month) {

		String sql = "";
		int total = 0;
		String tmp = "";

		sql = "select SUM(PRICE) FROM BUDGET WHERE CLUB_ID =" + club_id + " AND IO_GB_CD LIKE ? AND USE_DT LIKE ? ";

		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, io_gb_cd);

			if (year == 0)
				tmp = tmp + "____";
			else
				tmp = tmp + year;

			if (month == 0)
				tmp = tmp + "%";
			else {
				if (month < 10)
					tmp = tmp + "0" + month + '%';
				else
					tmp = tmp + month + "%";
			}

			ps.setString(2, tmp);

			rs = ps.executeQuery();
			while (rs.next()) {
				total = rs.getInt(1);

			}

		} catch (Exception e) {
			System.out.println(e + "=> getBudget fail");
		} finally {
			db_close();
		}
		return total;
	}

	public ArrayList<BudgetVO> getBudget(int club_id, String io_gb_cd) {
		ArrayList<BudgetVO> list = new ArrayList<BudgetVO>();
		String sql = "";

		sql = "select USE_DT, CONTENTS, PRICE  FROM BUDGET WHERE CLUB_ID = " + club_id + " AND IO_GB_CD LIKE '"
				+ io_gb_cd + "%'";
		try {

			st = con.createStatement();
			rs = st.executeQuery(sql);

			while (rs.next()) {
				BudgetVO vo = new BudgetVO();

				vo.setUse_dt(rs.getString(1));
				vo.setContents(rs.getString(2));
				vo.setPrice(rs.getInt(3));
				list.add(vo);

			}

		} catch (Exception e) {
			System.out.println(e + "=> getBudget fail");
		} finally {
			db_close();
		}
		return list;
	}

	public ArrayList<BudgetVO> getBudget(int club_id, String io_gb_cd, int year, int month) {
		ArrayList<BudgetVO> list = new ArrayList<BudgetVO>();
		String sql = "";
		String tmp = "";

		sql = "select USE_DT, CONTENTS, PRICE  FROM BUDGET WHERE CLUB_ID = " + club_id
				+ " AND IO_GB_CD LIKE ? AND USE_DT LIKE ? ";
		try {

			ps = con.prepareStatement(sql);
			ps.setString(1, io_gb_cd);

			if (year == 0)
				tmp = tmp + "____";
			else
				tmp = tmp + year;

			if (month == 0)
				tmp = tmp + "%";
			else {
				if (month < 10)
					tmp = tmp + "0" + month + '%';
				else
					tmp = tmp + month + "%";
			}

			ps.setString(2, tmp);

			rs = ps.executeQuery();

			while (rs.next()) {
				BudgetVO vo = new BudgetVO();

				vo.setUse_dt(rs.getString(1));
				vo.setContents(rs.getString(2));
				vo.setPrice(rs.getInt(3));
				list.add(vo);

			}

		} catch (Exception e) {
			System.out.println(e + "=> getBudget fail");
		} finally {
			db_close();
		}
		return list;
	}

	public int writeBudget(ArrayList<BudgetVO> bv, int club_id, String io_gb_cd) {
		String sql = "";
		String insert_sql = "";

		sql = "DELETE FROM BUDGET WHERE CLUB_ID = " + club_id + " AND IO_GB_CD LIKE '" + io_gb_cd + "%'";

		insert_sql = "insert into `budget` (`CLUB_ID`, `SEQ_NO`, `IO_GB_CD`, `USE_DT`, `CONTENTS`, `PRICE`) "
				+ "values(?, (SELECT IFNULL(MAX(seq_no) + 1, 1) FROM budget b), ?, ?, ?, ?)";

		try {

			st = con.createStatement();

			st.executeUpdate(sql);

			for (int i = 0; i < bv.size(); i++) {
				ps = con.prepareStatement(insert_sql);
				ps.setInt(1, club_id);
				ps.setString(2, io_gb_cd);
				ps.setString(3, bv.get(i).getUse_dt().replaceAll("-", ""));
				ps.setString(4, bv.get(i).getContents());
				ps.setInt(5, bv.get(i).getPrice());
				ps.executeUpdate();
			}

			return 1;

		} catch (Exception e) {
			System.out.println(e + "=> getBudget fail");
		} finally {
			db_close();
		}
		return -1;
	}
}
