package gb.ru.javafxchat.server;
import java.sql.*;



public class SqlBd {

    public static void main(String[] args) {

        // CREATE TABLE users(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //       name  VARCHAR(50),
        //       pass  VARCHAR(50));
        //
        //
        //
        //
        //      INSERT INTO users(name,pass)
        //       VALUES('admin','admin');
        //
        //       SELECT*FROM users;

        Connection connection;
        Driver driver;


        try {
            DriverManager.getConnection("jdbc:sqlite:src/main/resources/users.db");
            connection = DriverManager.getConnection("src/main/java/");





        }catch (SQLException ex){

            System.out.println(" Произошла ошибка при создании драйвера");
        }

    }

}
