class ServerClient {
    public ServerClient() {
        handshake();
    }

    private void handshake() {
    try {
        String line;
        String key = "";
        boolean socketReq = false;
        while (true) {
            line = input.readLine();
            if (line.startsWith("Upgrade: websocket"))
                socketReq = true;
            if (line.startsWith("Sec-WebSocket-Key: "))
                key = line;
            if (line.isEmpty())
                break;
        }
        if (!socketReq)
            socket.close();
        String encodedKey = DatatypeConverter.printBase64Binary(
             MessageDigest.getInstance("SHA-1")
                .digest((key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes()));

        System.out.println(encodedKey);
        output.println("HTTP/1.1 101 Switching Protocols");
        output.println("Upgrade: websocket");
        output.println("Connection: Upgrade");
        output.println("Sec-WebSocket-Accept: " + encodedKey);
        output.flush();
        output.close();     // output = new PrintWriter(
                            //      Socket.getOutputStream());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
