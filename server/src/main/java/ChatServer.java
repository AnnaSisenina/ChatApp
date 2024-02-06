import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements TCPConnectionListener {

    public static void main(String[] args) {
        new ChatServer();
    }
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private ChatServer(){
        System.out.println("Server running...");
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            while(true) {
                try{
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionRead(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: " + tcpConnection);
        List<String> log = readLinesFromFile();
        for (String message : log) {
            tcpConnection.sendString(message);
        }

    }


    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        saveLog(value);
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for (int i = 0; i < cnt; i++) {
            connections.get(i).sendString(value);
        }
    }

    private void saveLog(String value) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("server/src/main/java/log.txt", true))) {
            writer.write(value);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readLinesFromFile() {
        String filePath = "server/src/main/java/log.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
