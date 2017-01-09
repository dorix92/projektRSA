package projektrsa;

import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class Client implements Runnable {

    private Socket socket = null;
    private Thread thread = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private ClientThread client = null;
    private Rsa rsa;
    private Okienko window = null;

    public Client(String serverName, int serverPort) throws FileNotFoundException {
        rsa = new Rsa();
        window = new Okienko(this);

        window.setVisible(true);
        System.out.println("Establishing connection. Please wait ...");
        try {
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        } catch (UnknownHostException uhe) {
            System.out.println("Host unknown: " + uhe.getMessage());
        } catch (IOException ioe) {
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null) {
            client = new ClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
        client.close();
        client.stop();
    }

    public void run() {
        BigInteger orginal = null;
        BigInteger szyfr = null;
        String read = null;
        while (thread != null) {
            try {
                read = console.readLine();
                orginal = new BigInteger(read.getBytes()); //console.readLine().getBytes()
                szyfr = rsa.enc(orginal);
                streamOut.writeUTF(new String(szyfr.toString()));
                streamOut.flush();
            } catch (IOException ioe) {
                System.out.println("Sending error: " + ioe.getMessage());
                stop();
            }
        }
    }

    public void wyslij(String wiadomosc) {
        BigInteger orginal = null;
        BigInteger szyfr = null;
        String read = wiadomosc;
        try {

            orginal = new BigInteger(read.getBytes()); //console.readLine().getBytes()
            szyfr = rsa.enc(orginal);
            streamOut.writeUTF(new String(szyfr.toString()));
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println("Sending error: " + ioe.getMessage());
            stop();
        }

    }

    public void wyswietlanie(String wiadomosc) {
        BigInteger orginal = null;
        BigInteger szyfr = null;

        szyfr = new BigInteger(wiadomosc);
        orginal = rsa.dec(szyfr);
        //Co robi z wiadomoscia

        window.ShowMsg("Szyfrowany: " + szyfr);
        window.ShowMsg("Oryginalny: " + new String(orginal.toByteArray()));
        System.out.println("Szyfrowany " + szyfr);
        System.out.println("Oryginalny: " + new String(orginal.toByteArray()));
    }

    public static void main(String args[]) throws FileNotFoundException {
        Client client = null;

        client = new Client("localhost", 1);
    }
}
