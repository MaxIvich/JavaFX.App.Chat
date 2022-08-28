package gb.ru.javafxchat.server;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import gb.ru.javafxchat.Command;
import gb.ru.javafxchat.client.ChatClient;

public class ClientHandler {
    private Socket socket;
    private ChatServer server;
    private DataInputStream in;
    private DataOutputStream out;
    private String nick;
    private AuthService authService;

    Path history = Path.of("src/main/resources/history.txt");


    public ClientHandler(Socket socket, ChatServer server, AuthService authService) {
        try {
            this.socket = socket;
            this.server = server;
            this.authService = authService;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    authenticate();
                    readMessages();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        while (true) {
            try {


               // checkOutTime();

                final String message = in.readUTF();
                final Command command = Command.getCommand(message);




                if (command == Command.AUTH) {
                    final String[] params = command.parse(message);
                    final String login = params[0];
                    final String password = params[1];
                    final String nick = authService.getNickByLoginAndPassword(login, password);
                    if (nick != null) {
                        if (server.isNickBusy(nick)) {
                            sendMessage(Command.ERROR, "Пользователь уже авторизован");
                            continue;
                        }
                        sendMessage(Command.AUTHOK, nick);
                        this.nick = nick;
                        server.broadcast(Command.MESSAGE, "Пользователь " + nick + " зашел в чат");
                        server.subscribe(this);
                        break;
                    } else {
                        sendMessage(Command.ERROR, "Неверные логин и пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    private void checkOutTime() {
        new java.util.Timer().schedule(
               new java.util.TimerTask(){
                   @Override
                   public void run() {
                       sendMessage(Command.ERROR, "Время подключения истекло  ");
                       sendMessage(Command.END);
                   }
               }
               ,120000);

    }


    public void sendMessage(Command command, String... params) {
        sendMessage(command.collectMessage(params));
    }

    private void closeConnection() {
        sendMessage(Command.END);
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
            server.unsubscribe(this);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String message) {
        try {
            out.writeUTF(message);
            writeTextToHistory(history,message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
        while (true) {
            try {
                final String message = in.readUTF();
                final Command command = Command.getCommand(message);
                if (command == Command.END) {
                    break;


                }
                if (command == Command.PRIVATE_MESSAGE) {
                    final String[] params = command.parse(message);
                    server.sendPrivateMessage(this, params[0], params[1]);
                    continue;
                }
               // if (command == Command.ChangeNick){
               //     String newNick = command.parse(message)[0];
               //     server.changeNick(this,newNick);
               // }
                server.broadcast(Command.MESSAGE, nick + ": " + command.parse(message)[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  String getNick() {
        return nick;
    }
    public void newNickAdd(String nnick) throws SQLException {
       String  n = this.getNick();
       SqlBd.SqlChengeNick(nnick,n);






    }
    private static void writeTextToHistory(Path history, String message) {
        try {
            FileWriter writer = new FileWriter(history.toFile(),true);

            writer.write(message + "\n");
            writer.close();
            read(history);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void read(Path history){
        try {
            System.out.println(Files.readString(history));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
