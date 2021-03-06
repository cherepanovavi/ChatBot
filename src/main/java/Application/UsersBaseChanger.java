package Application;

import LogicFiles.UserState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UsersBaseChanger {

    private final String url = "jdbc:mysql://localhost:3306/usersbase" +
            "?verifyServerCertificate=false" +
            "&useSSL=false" +
            "&allowPublicKeyRetrieval=true" +
            "&requireSSL=false" +
            "&useLegacyDatetimeCode=false" +
            "&amp" +
            "&serverTimezone=UTC";
    private final String user = "root";
    private final String password = "DeniskinBeast2018";

    private Connection con;

    public ObservableList<UserState> selectFromDataBase() {
        ObservableList<UserState> usersList = FXCollections.observableArrayList();
        String query = "select username, score, attempts_on_question from users_results";
        try {
            con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String userName = rs.getString(1);
                int score = rs.getInt(2);
                int questionAttempts = rs.getInt(3);

                UserState userState = new UserState(userName);
                userState.setScore(score);
                userState.setQuestionAttempts(questionAttempts);

                usersList.add(userState);
            }
            try {
                stmt.close();
            } catch (SQLException se) {
                System.out.println("can't close the Statement");
            }
            try {
                rs.close();
            } catch (SQLException se) {
                System.out.println("can't close the ResultSet");
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
                System.out.println("can't close the connection");
            }
        }
        return usersList;
    }

    public void insertIntoDataBase(UserState userState) {
        String query = "insert into users_results (username, score, attempts_on_question) values (?, ?, ?)";
        try {
            con = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userState.getName());
            ps.setInt(2, userState.getScore());
            ps.setInt(3, userState.getQuestionAttempts());

            ps.executeUpdate();
            try {
                ps.close();
            } catch (SQLException se) {
                System.out.println("can't close the PreparedStatement");
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException se) {
                System.out.println("can't close the Connection");
            }
        }
    }
}
