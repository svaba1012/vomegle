import java.net.Socket;

public class User {
    public Socket socket;

    public User(Socket socket) {
        this.socket = socket;

    }

    public User(User user) {
        this.socket = user.socket;
    }
}
