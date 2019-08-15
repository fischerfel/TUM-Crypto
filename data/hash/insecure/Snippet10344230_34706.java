// Headers are read in a previous method which wraps the socket using RFC6445
// protocol. If it detects 2 keys it will call this and pass in the headers.
public static MessagingWebSocket wrapOldProtocol(HashMap<String, String> headers, PushbackInputStream pin, Socket sock) throws IOException, NoSuchAlgorithmException {
    // SPEC
    // http://tools.ietf.org/html/draft-hixie-thewebsocketprotocol-76#page-32

    // Read the "key3" value. This is 8 random bytes after the headers.
    byte[] key3 = new byte[8];
    for ( int i=0; i<key3.length; i++ ) {
        key3[i] = (byte)pin.read();
    }

    // Grab the two keys we need to use for the handshake
    String key1 = headers.get("Sec-WebSocket-Key1");
    String key2 = headers.get("Sec-WebSocket-Key2");

    // Count the spaces in both keys
    // Abort the connection is either key has 0 spaces
    int spaces1 = StringUtils.countMatches(key1, " ");
    int spaces2 = StringUtils.countMatches(key2, " ");
    if ( spaces1 == 0 || spaces2 == 0 ) {
        throw new IOException("Bad Handshake Request, Possible Cross-protocol attack");
    }

    // Strip all non-digit characters from each key
    // Use the remaining value as a base-10 integer.
    // Abort if either number is not a multiple of it's #spaces counterpart
    // Need to use long because the values are unsigned
    long num1 = Long.parseLong( key1.replaceAll("\\D", "") );
    long num2 = Long.parseLong( key2.replaceAll("\\D", "") );
    if ( !(num1 % spaces1 == 0) || !(num2 % spaces2 == 0) ) {
        throw new IOException("Bad Handshake Request. Possible non-conforming client");
    }

    // Part1/2 is key num divided by the # of spaces
    int part1 = (int)(num1 / spaces1);
    int part2 = (int)(num2 / spaces2);

    // Now calculate the challenge response
    // MD5( num1 + num2 + key3 )  ... concat, not add
    MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(ByteBuffer.allocate(4).putInt(part1));
    md.update(ByteBuffer.allocate(4).putInt(part2));
    md.update(key3);
    byte[] response = md.digest();

    // Now build the server handshake response
    // Ignore Sec-WebSocket-Protocol (we don't use this)
    String origin = headers.get("Origin");
    String location = "ws://" + headers.get("Host") + "/";

    StringBuilder sb = new StringBuilder();
    sb.append("HTTP/1.1 101 WebSocket Protocol Handshake").append("\r\n");
    sb.append("Upgrade: websocket").append("\r\n");
    sb.append("Connection: Upgrade").append("\r\n");
    sb.append("Sec-WebSocket-Origin: ").append(origin).append("\r\n");
    sb.append("Sec-WebSocket-Location: ").append(location).append("\r\n");
    sb.append("\r\n");

    // Anything left in the buffer?
    if ( pin.available() > 0 ) {
        throw new IOException("Unexpected bytes after handshake!");
    }

    // Send the handshake & challenge response
    OutputStream out = sock.getOutputStream();
    out.write(sb.toString().getBytes());
    out.write(response);
    out.flush();

    System.out.println("[MessagingWebSocket] Handshake Complete.");

    // Return the wrapper socket class.
    MessagingWebSocket ws = new MessagingWebSocket(sock);
    ws.oldProtocol = true;
    return ws;
}
