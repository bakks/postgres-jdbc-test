package commonwealth;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
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

    public static void cachedRowSetQuery(String query) throws Exception {
        System.out.println("\r\nquery = " + query);
        Statement st;
        ResultSet rs;

        Class.forName("org.postgresql.Driver").newInstance();
        Connection conn = DriverManager
                .getConnection("jdbc:postgresql://127.0.0.1:5432/commonwealth");
        st = conn.createStatement();
        rs = st.executeQuery(query);
        CachedRowSetImpl crs = new CachedRowSetImpl();
        crs.populate(rs);
        conn.close();

        printRS(crs);
        st.close();
        rs.close();
    }

    public static void basicQuery(Connection conn, String query) throws SQLException {
        Statement st = conn.createStatement();

        // Turn use of the cursor on.
        st.setFetchSize(3);
        ResultSet rs = st.executeQuery("SELECT * FROM pbbakkum.test limit 10");
        printRS(rs);
        rs.close();
        st.close();
    }

    public static void preparedStatementQuery(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from pbbakkum.test where col1 = ?");
        ResultSet rs;
        stmt.setInt(1, 0);
        stmt.addBatch();

        rs = stmt.executeQuery();
        printRS(rs);
        rs.close();
    }

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver").newInstance();
       // String url = "jdbc:postgresql://127.0.0.1:5433/commonwealth";
        //Connection conn = DriverManager.getConnection(url);

        // make sure autocommit is off
        //conn.setAutoCommit(false);

        //preparedStatementQuery(conn);
        //basicQuery(conn, "select * from pbbakkum.test limit 10;");
        cachedRowSetQuery("select * from pbbakkum.test limit 10");
        cachedRowSetQuery("select random()*9 from pbbakkum.nfl_playbyplay limit 10;");
        //cachedRowSetQuery("select qtr from pbbakkum.nfl_playbyplay limit 10;");
    }
}
