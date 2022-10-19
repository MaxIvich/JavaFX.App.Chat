package gb.ru.javafxchat.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class History {
    static Path history = Path.of("src/main/resources/history.txt");


    private static String read(Path history) {
        try {
            return  Files.readString(history);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void main(String[] args) {
        read(history);






    }


//
//   private static void writeTextToHistory(Path history,String message){

//       try {
//           Files.writeString(history,message);
//       } catch (IOException e) {
//           throw new RuntimeException(e);
//       }

//   }


//






}
