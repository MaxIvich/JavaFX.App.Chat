module gb.ru.javafxchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens gb.ru.javafxchat to javafx.fxml;
    exports gb.ru.javafxchat;
    exports gb.ru.javafxchat.client;
    opens gb.ru.javafxchat.client to javafx.fxml;
}