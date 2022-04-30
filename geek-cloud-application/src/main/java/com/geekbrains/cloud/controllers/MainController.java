package com.geekbrains.cloud.controllers;

import com.geekbrains.cloud.network.Net;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private static final String LOCAL_FILE_PATH = "files";
    private Net net;

    @FXML
    public ListView<String> serverFiles;
    @FXML
    public ListView<String> localFiles;
    @FXML
    public TextField input;

    private void read() {
        while (true) {
            try {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                String command = net.readUtf();
                if (command.equals("#list#")) {
                    readServerListFiles();
                }
                if (command.equals("#ready#")) {
                    String file = localFiles.getSelectionModel().getSelectedItem();
                    net.sendFile(new File(LOCAL_FILE_PATH + '/' + file));
                }
            } catch (IOException e) {
                e.printStackTrace();
                net.closeConnection();
                break;
            }
        }
    }

    private void readServerListFiles() {
        try {
            Platform.runLater(() -> serverFiles.getItems().clear());
            Long filesCount = net.readLong();
            for (int i = 0; i < filesCount; i++) {
                String fileName = net.readUtf();
                Platform.runLater(() -> serverFiles.getItems().addAll(fileName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readLocalFilesList(String localFilePath) {
        File dir = new File(localFilePath);
        String[] localFilesList = dir.list();

        localFiles.getItems().addAll(localFilesList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            net = Net.getINSTANCE();
            readLocalFilesList(LOCAL_FILE_PATH);
            Thread readThread = new Thread(this::read);
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                net.sendUtf("#file#");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

