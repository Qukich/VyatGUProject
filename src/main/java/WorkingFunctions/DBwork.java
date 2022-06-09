package WorkingFunctions;

import java.sql.*;

public class DBwork {
    private static Connection conn = null;

    public DBwork() {
        try {
            String dbURL = "jdbc:postgresql://localhost:5432/Proekt";
            String user = "postgres";
            String pass = "1";
            conn = DriverManager.getConnection(dbURL, user, pass);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet Select(String operation) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery(operation);
    }

    public void Insert(String operation) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(operation);
    }

    public void Update(String operation) throws SQLException{
        Statement st = conn.createStatement();
        st.executeUpdate(operation);
    }

    public void Delete(String operation) throws SQLException{
        Statement st = conn.createStatement();
        st.executeUpdate(operation);
    }
}
