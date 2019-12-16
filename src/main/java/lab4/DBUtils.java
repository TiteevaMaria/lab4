package lab4;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBUtils
{
    public static Connection connection;

    public static void connectDB() throws ClassNotFoundException, SQLException
    {
        connection = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:trainsDB.s3db");
    }

    public static void writeDB(Train train) throws SQLException
    {
        System.out.println("Trying to write to DB");
        String query = "INSERT INTO 'Trains' ('Name', 'Time') VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, train.name);
        statement.setTimestamp(2, train.time);
        statement.execute();
    }

    public static void createDB() throws ClassNotFoundException, SQLException
    {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE if not exists 'Trains' ('Name' text NOT NULL, 'Time' time NOT NULL);");
        System.out.println("Таблица создана или уже существует.");
    }

    public static Set<Train> readDB() throws SQLException
    {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Trains WHERE Time > " + (System.currentTimeMillis() + 10800000));
        Set<Train> trains = new HashSet<>();
        while (resultSet.next()) {
            Train train = new Train();
            train.name = resultSet.getString("Name");
            train.time = resultSet.getTimestamp("Time");
            trains.add(train);
        }
        return trains;
    }
}
