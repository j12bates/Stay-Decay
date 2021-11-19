import java.net.Socket;
import java.net.ServerSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.Scanner;

class Main {
    // Client Configuration
    static boolean localPlayer;         // Player the client is playing (A)
    static boolean server;

    static int length = 3;              // Matrix length

    // Network Sockets
    static Socket socket;
    static final int port = 8888;

    // Messages
    public static String versionString = "Game version 1.1";

    // Input/Output Streams
    public static Scanner remoteInput;
    public static PrintStream localOutput;

    public static Scanner localInput = new Scanner(System.in);
    public static PrintStream remoteOutput = System.out;

    // Initialize game
    public static void main(String[] args) {
        // If the matrix length is specified
        if (args.length > 0) length = Integer.parseInt(args[0]);
        
        // Specify Local or Networked
        try {
            if (args.length > 1) initStreams(args[1]);
            else initStreams("");
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Game.main(length, localPlayer);
    }

    // Initialize I/O Streams
    static void initStreams(String remoteAddress) throws Exception {
        // This is a networked game
        if (!remoteAddress.equals("") && !remoteAddress.equals("local")) {

            // Is this supposed to be a server?
            server = remoteAddress.equals("server") || remoteAddress.equals("0.0.0.0");

            // Start up a server
            if (server) {
                socket = new ServerSocket(port).accept();
                localPlayer = true;
            }

            // Connect to a server
            else {
                socket = new Socket(remoteAddress, port);
            }

            // Get I/O Streams
            remoteInput = new Scanner(
                socket.getInputStream()
            );
            localOutput = new PrintStream(
                socket.getOutputStream()
            );

            // Send configuration to client
            if (server) {
                localOutput.println(versionString);
                localOutput.println(!localPlayer ? "A" : "B");
                localOutput.println(length);
            }
 
            // If a client, verify and accept the configuration
            else {
                if (!remoteInput.nextLine().equals(versionString))
                    throw new Exception("Server is a different game version.");

                localPlayer = remoteInput.nextLine().equals("A");
                length = Integer.parseInt(remoteInput.nextLine());
            }
        }
 
        // This is a fully local game
        else {
            remoteInput = localInput;
            localOutput = remoteOutput;
        }
    }
}
