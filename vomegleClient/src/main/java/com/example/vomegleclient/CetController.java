package com.example.vomegleclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class CetController {
    public VBox msgField;
    public TextField text;
    public Socket socket;
    public PrintWriter writer;

    public BufferedReader reader;
    public Label chatSearchLabel;
    public Button exitChatButton;
    public Button sendButton;

    public ScrollPane scrollPane;
    public void initialize(){
        try {
            this.socket = new Socket("localhost", 5000);
        } catch (IOException e) {
            this.chatSearchLabel.setText("Сервер је недоступан. Покушајте касније...");
            this.exitChatButton.setDisable(false);
            this.exitChatButton.setText("Мени");
            return;
        }
        try {
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = (Stage) Stage.getWindows().get(0);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Platform.exit();
                System.exit(0);
            }
        });

        Thread listenThread = new Thread(new ListenThread(this));
        listenThread.start();
    }

    public void addMsgLabel(String msg, boolean isStranger){
        HBox msgBox = new HBox();
        msgBox.setMinWidth(760);
        Label msgLabel = new Label(msg);
        msgLabel.setMinWidth(300);

        if(isStranger){
            msgBox.setAlignment(Pos.CENTER_LEFT);
            msgLabel.getStyleClass().add("chatLbl");
            msgLabel.getStyleClass().add("strangerLbl");
        }else {
            msgBox.setAlignment(Pos.CENTER_RIGHT);
            msgLabel.getStyleClass().add("chatLbl");
            msgLabel.getStyleClass().add("userLbl");
            msgLabel.setAlignment(Pos.CENTER_RIGHT);
        }

        msgLabel.setMaxWidth(600);
        msgLabel.setWrapText(true);
//        System.out.println(msgLabel.getText());
        msgBox.getChildren().add(msgLabel);
        msgField.getChildren().add(msgBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                scrollPane.vvalueProperty().bind(msgField.heightProperty());
            }
        });
    }

    public void enableMessaging(){
        this.chatSearchLabel.setText("ВОмегле вам је нашао новог странца за чет.");
        this.sendButton.setDisable(false);
        this.text.setDisable(false);
        this.exitChatButton.setDisable(false);
    }

    public void onSendButtonClicked(ActionEvent actionEvent) {
        String msg = text.getText();
        if(msg.isBlank()){
            return;
        }
        this.addMsgLabel(msg, false);
        this.writer.println(msg);
        text.clear();
    }

    public void loadNewSceneFXML(String fxmlFileName, Stage stage){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFileName));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.show();
    }

    public void closeChat(Stage stage){
        try {
            if(this.socket != null){
                this.socket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loadNewSceneFXML("new-chat-view.fxml", stage);

    }
    public void anotherUserDisconected(){
        this.sendButton.setDisable(true);
        this.text.setDisable(true);
        this.chatSearchLabel.setText("Корисник се дисконектовао. Притисните дугме за мени.");
        this.exitChatButton.setText("Мени");
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void onCloseButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        closeChat(stage);
    }

}
