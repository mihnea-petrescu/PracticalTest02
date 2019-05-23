package ro.pub.cs.systems.eim.practicaltest02;

import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientThread extends Thread {
    String command;
    int port;
    EditText invisibleEditText;

    Socket socket;

    public ClientThread(String command, int port, EditText invisibleEditText) {
        this.command = command;
        this.port = port;
        this.invisibleEditText = invisibleEditText;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", port);
            BufferedReader br = Utilities.getReader(socket);
            PrintWriter pw = Utilities.getWriter(socket);

            pw.println(command);
            pw.flush();
            String result;

            while ((result = br.readLine()) != null) {
                final String finalizedResult = result;
                invisibleEditText.post(new Runnable() {
                    @Override
                    public void run() {

                        invisibleEditText.setText(finalizedResult);

                    }
                });
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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