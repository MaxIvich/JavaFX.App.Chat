package gb.ru.javafxchat.client;
import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import gb.ru.javafxchat.Command;

public class ChatClientApp extends Application {
        @Override
        public void start(Stage stage) throws IOException {
             FXMLLoader fxmlLoader = new FXMLLoader(ChatClientApp.class.getResource("client-view.fxml"));
             Scene scene = new Scene(fxmlLoader.load(), 600, 400);
             stage.setTitle("GB Chat client");
             stage.setScene(scene);
             stage.show();

            ChatController controller = fxmlLoader.getController();
            stage.setOnCloseRequest(event -> controller.getClient().sendMessage(Command.END));
        }

        public static void main(String[] args) {
            launch();
        }
    }
    //java-fx-chat/src/main/resources/javafxchat/
    //module gb.ru.javafxchat {
    //    requires javafx.controls;
    //    requires javafx.fxml;
    //
    //
    //    opens gb.ru.javafxchat to javafx.fxml;
    //    exports gb.ru.javafxchat;
    //    exports gb.ru.javafxchat.client;
    //    opens gb.ru.javafxchat.client to javafx.fxml;
    //    }

