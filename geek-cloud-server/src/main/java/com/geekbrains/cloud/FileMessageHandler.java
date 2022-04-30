package com.geekbrains.cloud;

import java.io.*;
import java.net.Socket;

public class FileMessageHandler implements Runnable {

    private final File dir;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private static final String FILE_PATH = "server-files";

    public FileMessageHandler(Socket socket) throws IOException {
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        System.out.println("Client accepted");

        dir = new File(FILE_PATH);
        sendFilesList();
    }

    private void sendFilesList() throws IOException {
        String[] files = dir.list();

        outputStream.writeUTF("#list#");
        if (files != null) {
            outputStream.writeLong(files.length);
            for (String file : files) {
                outputStream.writeUTF(file);
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String utf = inputStream.readUTF();
                if (utf.equals("#file#")) {
                    outputStream.writeUTF("#ready#");

                    String fileName = inputStream.readUTF();
                    getFile(fileName);

                    outputStream.writeUTF("#test#");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFile(String fileName) {
        System.out.println("ready to receive file");
        try (FileOutputStream fileOutputStream = new FileOutputStream(FILE_PATH+"/" + fileName)) {
            long fileLength = inputStream.readLong();
            byte[] buffer = new byte[1024];
            int dataLength;
            while ((dataLength = inputStream.read(buffer)) > 0) {
                System.out.println(dataLength);
                fileOutputStream.write(buffer,0, dataLength);
                fileLength -= dataLength;
                if (fileLength <= 0) {
                    break;
                }
            }
            fileOutputStream.flush();

            System.out.println("file received");
            sendFilesList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
