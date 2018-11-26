package Source.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersBaseChanger {

    private static final String url = "jdbc:mysql://localhost:3306/usersbase";
    private static final String user = "root";
    private static final String password = "DeniskinBeast2018";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public void selectFromDataBase()
    {
        String query = "select username, score, attempts_on_question from users_table";
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next())
            {

            }
        }
    }
}
