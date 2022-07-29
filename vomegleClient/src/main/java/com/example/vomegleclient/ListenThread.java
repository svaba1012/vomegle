package com.example.vomegleclient;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ListenThread implements Runnable{

    public Socket socket;
    public CetController controller;

    public ListenThread(CetController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(controller.socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        cekaj odgovor servera sto znaci da je nadjen korisnik
        try {
            reader.readLine();
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.anotherUserDisconected();
                }
            });
            return;
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.enableMessaging();
            }
        });

        while(true){
            String msg = null;
            try {
                msg = reader.readLine();
            } catch (SocketException e){

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(msg == null){
                break;
            }
            String finalMsg = msg;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.addMsgLabel("Странац: " + finalMsg, true);
                }
            });
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.anotherUserDisconected();
            }
        });
    }
}
