package main.Utility;

import lombok.Getter;
import lombok.Setter;
import main.Models.Entities.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Getter
@Setter
public class ClientSocket {
    private static final ClientSocket SINGLE_INSTANCE = new ClientSocket();

    private User user;
    private static Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ClientSocket() {
        try {
            socket = new Socket("localhost", 6666);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
        } catch (Exception e) {
        }
    }

    public static ClientSocket getInstance() {
        return SINGLE_INSTANCE;
    }

    public Socket getSocket(){
        return socket;
    }


}

