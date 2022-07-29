import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class OneWayCommunicationThread implements Runnable{
    public User sender;
    public User reciver;

    Thread chat;

    public OneWayCommunicationThread(Thread chat, User sender, User reciver) {
        this.sender = sender;
        this.reciver = reciver;
        this.chat = chat;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(reciver.socket.getInputStream()));
            writer = new PrintWriter(sender.socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(true){
            try {
                String msg = reader.readLine();
                if(msg == null){
                    this.chat.interrupt();
                    break;
                }
//                System.out.println(msg);
                writer.println(msg);
            } catch (IOException e) {
                this.chat.interrupt();
            }
        }
    }
}
