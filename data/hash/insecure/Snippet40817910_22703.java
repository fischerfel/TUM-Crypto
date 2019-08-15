public ClientSocket(Socket socket) {
    try {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream());
        handshake();
}

private void handshake() {
    try {
        String line;
        String key = "";
        while (true) {
            line = input.readLine();
            if (line.startsWith("Sec-WebSocket-Key: ")) {
                key = line.split(" ")[1];
                System.out.println("'" + key + "'");
            }
            if (line == null || line.isEmpty())
                break;
        }
        output.println("HTTP/1.1 101 Switching Protocols");
        output.println("Upgrade: websocket");
        output.println("Connection: Upgrade");
        output.println("Sec-WebSocket-Accept: " + encode(key));
        output.println();
        output.flush();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private String encode(String key) throws Exception {
    key += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    byte[] bytes = MessageDigest.getInstance("SHA-1").digest(key.getBytes());
    return DatatypeConverter.printBase64Binary(bytes);
}
