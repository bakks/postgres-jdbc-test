package commonwealth;


import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetExample {

    public static void main(String[] args) {

        query("select random()*9 from pbbakkum.nfl_playbyplay limit 10;");
        query("select qtr from pbbakkum.nfl_playbyplay limit 10;");
    }

    public static void query(String qry) {
        System.out.println("\r\nquery = " + qry);
        Statement stmt = null;
        ResultSet resultSet = null;

        try {
            Class.forName("org.postgresql.Driver").newInstance();
            Connection conn = DriverManager
                    .getConnection("jdbc:postgresql://127.0.0.1/commonwealth");
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(qry);
            CachedRowSetImpl crs = new CachedRowSetImpl();
            crs.populate(resultSet);
            conn.close();

            printRS(crs);

        } catch (SQLException se) {
            System.out.println(se.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private static void printRS(ResultSet rs) throws SQLException {
        System.out.println("");
        ResultSetMetaData md = rs.getMetaData();
        int cn = md.getColumnCount();
        for (int i = 1; i <= cn; i++) {
            System.out.print(md.getColumnName(i) + ",");
        }
        while (rs.next()) {
            System.out.println("");
            for (int i = 1; i <= cn; i++) {
                System.out.print(rs.getString(i) + ",");
            }
        }
    }
}
