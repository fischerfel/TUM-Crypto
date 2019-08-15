package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.Properties;

import server.message.HandshakeMessage;
import server.message.Message;


public class ConnectionManager {
    private static final int CLIENT_VERSION = 1;
    private Socket socket;
    private MessageDecoder decoder = new MessageDecoder();
    private MessageDispatcher dispatcher;
    private ConnectionListener listener;

    public ConnectionManager(Socket connection, MessageDispatcher dispatcher, ConnectionListener listener) {
        socket = connection;
        this.dispatcher = dispatcher;
        this.listener = listener;
    }

    public void start() {
        Thread t = new Thread(new ChannelReader());
        t.setName("Client thread");
        t.setDaemon(true);
        t.start();
    }

    public void send(byte[] data) {
        if (socket == null)
            return;

        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.write(data);
            dos.flush();
        } catch (IOException e) {
            disconnect("Client closed the connection");
        }
    }

    private class ChannelReader implements Runnable {
        private boolean accepted = false;
        private String ret = null;

        @Override
        public void run() {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while (socket != null && socket.isConnected()) {
                    int len = in.readShort();
                    if (len < 0) {
                        disconnect("Invalid message length.");
                    }

                    String s;
                    readLine(in);
                    Properties props = new Properties();
                    while((s=readLine(in)) != null && !s.equals("")) {
                        String[] q = s.split(": ");
                        props.put(q[0], q[1]);
                    }

                    if(props.get("Upgrade").equals("websocket") && props.get("Sec-WebSocket-Version").equals("13")) { // check if is websocket 8
                        String key = (String) props.get("Sec-WebSocket-Key");
                        String r = key + "" + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"; // magic key
                        MessageDigest md = MessageDigest.getInstance("SHA-1");
                        md.reset();
                        md.update(r.getBytes());
                        byte[] sha1hash = md.digest();


                        String returnBase = base64(sha1hash);


                        ret = "HTTP/1.1 101 Switching Protocols\r\n";
                            ret+="Upgrade: websocket\r\n";
                            ret+="Connection: Upgrade\r\n";
                            ret+="Sec-WebSocket-Accept: "+returnBase;

                    } else {
                        disconnect("Client got wrong version of websocket");
                    }

                    Message msg = decoder.decode((String) props.get("Sec-WebSocket-Protocol"));

                    if (!accepted) {
                        doHandshake(msg);
                    } else if (dispatcher != null) {
                        dispatcher.dispatch(msg);
                    }
                }
            } catch (Exception e) {
                disconnect(e.getMessage());
                e.printStackTrace();
            }
        }

        private void doHandshake(Message msg) {
            if (!(msg instanceof HandshakeMessage)) {
                disconnect("Missing handshake message");
                return;
            }
            HandshakeMessage handshake = (HandshakeMessage) msg;
            if (handshake.getVersion() != CLIENT_VERSION) {
                disconnect("Client failed in handshake.");
                return;
            }
            send(ret.getBytes());
            accepted = true;
            listener.clientConnected(ConnectionManager.this);
        }   

        private String base64(byte[] input) throws ClassNotFoundException, 
        SecurityException, NoSuchMethodException, IllegalArgumentException, 
        IllegalAccessException, InvocationTargetException, InstantiationException {
            Class<?> c = Class.forName("sun.misc.BASE64Encoder");
            java.lang.reflect.Method m = c.getMethod("encode", new Class<?>[]{byte[].class});
            String s = (String) m.invoke(c.newInstance(), input);
            return s;
        }

        private String readLine(InputStream in) {
            try{
                String line = "";
                int pread;
                int read = 0;
                while(true) {
                    pread = read;
                    read = in.read();
                    if(read!=13&&read!=10)
                        line += (char) read;
                    if(pread==13&&read==10) break;
                }
                return line;
            }catch(IOException ex){

            }
            return null;
        }

    }

    public synchronized void disconnect(String message) {
        System.err.println(message);
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
        socket = null;
        listener.clientDisconnected(ConnectionManager.this);
    }
}
