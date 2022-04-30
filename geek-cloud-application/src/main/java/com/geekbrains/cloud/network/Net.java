package com.geekbrains.cloud.network;

import java.io.*;
import java.net.Socket;


public class Net {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8189;

    private static Net INSTANCE;

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private Net(String host, int port) throws IOException {
        socket = new Socket(host, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    public static Net getINSTANCE() throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new Net(SERVER_HOST, SERVER_PORT);
        }
        return INSTANCE;
    }

    public Long readLong() throws IOException {
        return inputStream.readLong();
    }

    public void sendLong(Long data) throws IOException {
        outputStream.writeLong(data);
    }

    public String readUtf() throws IOException {
        return inputStream.readUTF();
    }

    public void sendUtf(String command) throws IOException {
        outputStream.writeUTF(command);
    }

    public void sendFile(File file) throws IOException {
        outputStream.writeUTF(file.getName());
        outputStream.writeLong(file.length());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int dataSize;
            while ((dataSize = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, dataSize);
            }
            outputStream.flush();
        }
    }

    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Error on close connection");
            e.printStackTrace();

        }
    }
}
