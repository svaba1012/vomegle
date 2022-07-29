import java.io.IOException;
import java.io.PrintWriter;

public class ChatThread implements Runnable{
    public User firstUser;
    public User secondUser;

    public ChatThread(User firstUser, User secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    @Override
    public void run() {
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        try {
            writer2 = new PrintWriter(secondUser.socket.getOutputStream(), true);
            writer1 = new PrintWriter(firstUser.socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        OneWayCommunicationThread firstWayCommunication = new OneWayCommunicationThread(Thread.currentThread(), this.firstUser, this.secondUser);
        OneWayCommunicationThread secondWayCommunication = new OneWayCommunicationThread(Thread.currentThread(), this.secondUser, this.firstUser);
        Thread comWay1Thread = new Thread(firstWayCommunication);
        Thread comWay2Thread = new Thread(secondWayCommunication);
        comWay1Thread.setDaemon(true);
        comWay2Thread.setDaemon(true);
        comWay1Thread.start();
        comWay2Thread.start();
        writer1.println("asa");
        writer2.println("asa");
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            try {
                firstUser.socket.close();
                secondUser.socket.close();

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        System.out.println("Cet je zavrsen");
    }
}
