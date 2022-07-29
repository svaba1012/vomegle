import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server pokrenut na portu 5000");
            User newUser = null;
            User firstUser = null;
            User secondUser = null;
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Ulogovao se novi korisnuk");
                newUser = new User(socket);
                if(firstUser == null){
                    firstUser = newUser;
                } else if (secondUser == null) {
                    System.out.println("Uparivanje dva nova korisnika");
                    secondUser = newUser;
                    ChatThread chat = new ChatThread(new User(firstUser), new User(secondUser));
                    Thread chatThread = new Thread(chat);
                    chatThread.start();
                    firstUser = null;
                    secondUser = null;
                }
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}