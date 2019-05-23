package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ro.pub.cs.systems.eim.practicaltest02.ServerThread;

public class CommunicationThread extends Thread {
    ServerThread serverThread;
    Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            // Read command from client
            String command = br.readLine();
            HashMap<String, Key> data = serverThread.getData();

            String response = null;

            String[] elements = command.split(",");
            Log.v("dbg", elements[0] + elements[1]);
            if ("get".equals(elements[0])) {
                Key value = data.get(elements[1]);


                if (value == null) {
                    response = "none\n";
                } else {
                    long timeStamp = value.getTimeStamp();
                    long diff = System.currentTimeMillis() - timeStamp;
                    if (diff > 60 * 1000) {
                        response = "This key is expired";
                        //data.remove(elements[1]);
                    } else {
                        response = data.get(elements[1]).getKeyName();
                    }
                }
            } else if ("put".equals(elements[0])) {
                // Do put
                long timeStamp = System.currentTimeMillis();
                serverThread.setData(elements[1], new Key(elements[2], timeStamp));
                //data.put(elements[0], new Key(elements[1], timeStamp));
                response = "Operation executed successfully\n";
            }

            pw.println(response);
            pw.flush();

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("dbg", "[Comm THREAD] Error no server socket");
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}