package gb.ru.javafxchat.server;
import java.io.Closeable;
import  java.sql.*;


public interface AuthService extends Closeable {
        String getNickByLoginAndPassword(String login, String password);

}
