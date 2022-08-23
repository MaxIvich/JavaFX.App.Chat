package gb.ru.javafxchat.server;
import java.io.IOException;
import java.sql.*;
import java.sql.DriverManager;


public class SqlBd implements AuthService{
    String Url = "jdbc:sqlite:C:/Users/jylve/Desktop/java-fx-chat/src/main/resources/users.db";


   private static Connection connection;


    //     try {
  //     connection = DriverManager.getConnection(Url);
  // } catch (SQLException e) {
  //     throw new RuntimeException(e);
  // }

  // Statement statement = null;
  //     try {
  //     statement = connection.createStatement();
  // } catch (SQLException e) {

  //     throw new RuntimeException(e);
  // }


    public SqlBd(){
        try {
            connection = DriverManager.getConnection(Url);
        }catch (SQLException e){
            throw new RuntimeException("не удалось подключится к БД" +e.getMessage(),e);

        }

    }
    public void SqlReg(){

    }
    public  static void SqlChengeNick(String newNick,String oldnick) throws SQLException {
        String Url = "jdbc:sqlite:C:/Users/jylve/Desktop/java-fx-chat/src/main/resources/users.db";
        connection = DriverManager.getConnection(Url);
        Statement statement = connection.createStatement();


        String update  = """
                update auth 
                set nick = %s 
                where nick = %s
                
                """;

        String.format(update,newNick,oldnick);
    }











    public static void main(String[] args) {

        // CREATE TABLE users(
        //      id INTEGER PRIMARY KEY AUTOINCREMENT,
        //       name  VARCHAR(50),
        //       pass  VARCHAR(50));
        //
        //     INSERT INTO users(name,pass)
        //       VALUES('admin','admin');
        //
        // <!-- https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc -->
        //        <dependency>
        //            <groupId>com.microsoft.sqlserver</groupId>
        //            <artifactId>mssql-jdbc</artifactId>
        //            <version>11.2.0.jre18</version>
        //        </dependency>


        //         <dependencies>
        //    <dependency>
        //      <groupId>org.xerial</groupId>
        //      <artifactId>sqlite-jdbc</artifactId>
        //      <version>(version)</version>
        //     </dependency>
        //  </dependencies>
        //
        //      src/main/resources/users.db
        //
        //       SELECT*FROM users;

    }

    @Override
    public String getNickByLoginAndPassword(String login, String password) {
        try {
            PreparedStatement statement = connection.prepareStatement("select  nick from auth where login = ? and pass = ? ");
            statement.setString(1,login);
            statement.setString(2,password);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.getString(1);



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() throws IOException {

    }

}


