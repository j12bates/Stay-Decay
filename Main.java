import java.net.Socket;
import java.net.ServerSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Scanner;

class Main {
    // Matrix length (default)
    static int x = 3;

    // Player the client is playing (A)
    static boolean localPlayer;

    // Network Sockets
    static Socket socket;
    static final int port = 8888;

    // Input/Output Streams
    public static Scanner remoteInput;
    public static PrintStream localOutput;

    public static Scanner localInput = new Scanner(System.in);
    public static PrintStream remoteOutput = System.out;

    // Initialize game
    public static void main(String[] args) {
        // If the matrix length is specified
        if (args.length > 0) x = Integer.parseInt(args[0]);
        
        // Specify Local or Networked
        try {
            if (args.length > 1) initStreams(args[1]);
            else initStreams("");
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Game.main(x, localPlayer);

        localOutput.println("henlo, worlnd");
        localOutput.println(remoteInput.nextLine());
    }

    // Initialize I/O Streams
    static void initStreams(String remoteAddress) throws IOException {
        // This is a networked game
        if (!remoteAddress.equals("")) {

            // Is this supposed to be a server?
            if (remoteAddress.equals("0.0.0.0")) {
                socket = new ServerSocket(port).accept();
                localPlayer = true;
            }

            // Connect to a server
            else {
                socket = new Socket(remoteAddress, port);
                localPlayer = false;
            }

            // Get I/O Streams
            remoteInput = new Scanner(
                socket.getInputStream()
            );
            localOutput = new PrintStream(
                socket.getOutputStream()
            );
        }
        
        // This is a fully local game
        else {
            remoteInput = localInput;
            localOutput = remoteOutput;
        }
    }
}
