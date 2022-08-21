package gb.ru.javafxchat.server;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;


public class SqlBd {

    public static void main(String[] args) {
        Connection connection;
        Driver driver;

        try {
            connection =DriverManager.getConnection("src/main/java/");





        }catch (SQLException ex){

            System.out.println(" Произошла ошибка при создании драйвера");
        }

    }

}
