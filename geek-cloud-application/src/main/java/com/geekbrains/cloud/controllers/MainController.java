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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
