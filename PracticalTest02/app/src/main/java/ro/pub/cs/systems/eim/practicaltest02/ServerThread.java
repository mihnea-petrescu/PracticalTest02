package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;


public class ServerThread extends Thread {
    int port;
    ServerSocket serverSocket;
    HashMap<String, Key> data;

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("dbg", "[SERVER THREAD] Error no server socket");
        }

        this.data = new HashMap<>();
    }

    public synchronized void setData(String key, Key value) {
        this.data.put(key, value);
    }

    public synchronized HashMap<String, Key> getData() {
        return data;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            } catch (IOException e) {
                e.printStackTrace();
                Log.v("dbg", "[SERVER THREAD] Error no server comm thread created");
            }
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}