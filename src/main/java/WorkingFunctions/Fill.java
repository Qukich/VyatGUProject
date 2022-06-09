package WorkingFunctions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Fill {
    public List<String> fillList(String operation) throws SQLException {
        DBwork db = new DBwork();
        List<String> tmp = new ArrayList<>();
        ResultSet rs = db.Select(operation);
        while (rs.next()){
            tmp.add(rs.getString(1));
        }
        return tmp;
    }

    public List<Integer> fillListInt(String operation) throws SQLException{
        DBwork db = new DBwork();
        List<Integer> tmp = new ArrayList<>();
        ResultSet rs = db.Select(operation);
        while (rs.next()){
            tmp.add(rs.getInt(1));
        }
        return tmp;
    }
}
