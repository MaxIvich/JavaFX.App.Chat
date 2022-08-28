package gb.ru.javafxchat.client;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.application.Platform;
import gb.ru.javafxchat.Command;

import static gb.ru.javafxchat.Command.*;

public class ChatClient {

        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;

        private final ChatController controller;


        Path history = Path.of("src/main/resources/history.txt");

        public ChatClient(ChatController controller) {
            this.controller = controller;
        }

        public void openConnection() throws IOException {
            socket = new Socket("localhost", 9990);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    waitAuth();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();

        }

        private void waitAuth() throws IOException {
            while (true) {
                final String message = in.readUTF();
                final Command command = getCommand(message);
                final String[] params = command.parse(message);
                if (command == AUTHOK) { // /authok nick1
                    final String nick = params[0];
                    controller.setAuth(true);
                    controller.addMessage("Успешная авторизация под ником " + nick);
                    controller.addMessage(controller.AddHistory(history));


                    break;
                }
                if (command == ERROR) {
                    Platform.runLater(() -> controller.showError(params[0]));
                    continue;
                }
            }
        }




        private void closeConnection() {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void readMessages() throws IOException {
            while (true) {
                final String message = in.readUTF();
                final Command command = getCommand(message);
                if (END == command) {
                    controller.setAuth(false);
                     break;

                }
                final String[] params = command.parse(message);
                if (ERROR == command) {
                    String messageError = params[0];
                    Platform.runLater(() -> controller.showError(messageError));
                    continue;
                }
                if (MESSAGE == command) {
                    Platform.runLater(() -> controller.addMessage(params[0]));
                }
                if (CLIENTS == command) {
                    Platform.runLater(() -> controller.updateClientsList(params));
                }



            }
        }

        private void sendMessage(String message) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(Command command, String... params) {
            sendMessage(command.collectMessage(params));
        }




        public void nnick(){

        }



    }


