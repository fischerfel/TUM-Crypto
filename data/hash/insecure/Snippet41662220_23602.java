public class SocketHandlerWebSocketLevel extends SocketHandler {

    private HashMap<String, String> connectionHeaders;
    private InputStreamReader stringReader;
    private OutputStreamWriter stringWriter;

    public SocketHandlerWebSocketLevel(Socket socket) {
        super(socket);
        connectionHeaders = new HashMap<String, String>();

        try {
            stringReader = new InputStreamReader(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
            print("could not get the input stream");
            return;
        }

        try {
            stringWriter = new OutputStreamWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            close();
            print("could not get the output stream");
            return;
        }
    }

    @Override
    public void run() {

        print("Started handler");
        char b;
        String buffer = "";
        try {
            mainLoop: while (true) {
                while (stringReader.ready() || buffer.length() == 0) {
                    if ((b = (char) stringReader.read()) != -1) {
                        buffer += b;
                    } else {
                        break mainLoop;
                    }

                }
                gotMessage(buffer);
                buffer = "";
            }
        } catch (IOException e) {
            close();
            print("connection was killed remotly, could not read the next byte");
            return;
        }

        close();
        print("connection was closed remotely, stopped Handler, closed socked");
    }

    private void gotMessage(String message) {
        if (connectionHeaders.size() == 0) {
            connectionHeaders = parseHttpHeader(message);
            handshakeResponse();
        } else {
            print(message);
        }
    }

    private void handshakeResponse() {
        /* 
           taken from: https://tools.ietf.org/html/rfc6455#page-7
           For this header field, the server has to take the value (as present
           in the header field, e.g., the base64-encoded [RFC4648] version minus
           any leading and trailing whitespace) and concatenate this with the
           Globally Unique Identifier (GUID, [RFC4122]) "258EAFA5-E914-47DA-
           95CA-C5AB0DC85B11" in string form, which is unlikely to be used by
           network endpoints that do not understand the WebSocket Protocol.  A
           SHA-1 hash (160 bits) [FIPS.180-3], base64-encoded (see Section 4 of
           [RFC4648]), of this concatenation is then returned in the server's
           handshake.

           Concretely, if as in the example above, the |Sec-WebSocket-Key|
           header field had the value "dGhlIHNhbXBsZSBub25jZQ==", the server
           would concatenate the string "258EAFA5-E914-47DA-95CA-C5AB0DC85B11"
           to form the string "dGhlIHNhbXBsZSBub25jZQ==258EAFA5-E914-47DA-95CA-
           C5AB0DC85B11".  The server would then take the SHA-1 hash of this,
           giving the value 0xb3 0x7a 0x4f 0x2c 0xc0 0x62 0x4f 0x16 0x90 0xf6
           0x46 0x06 0xcf 0x38 0x59 0x45 0xb2 0xbe 0xc4 0xea.  This value is
           then base64-encoded (see Section 4 of [RFC4648]), to give the value
           "s3pPLMBiTxaQ9kYGzzhZRbK+xOo=".  This value would then be echoed in
           the |Sec-WebSocket-Accept| header field.
        */

        String secWebSocketKey, secWebSocketAccept, GUID, template, merged, toSend;
        secWebSocketKey = connectionHeaders.get("Sec-WebSocket-Key");
        GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
        template = "HTTP/1.1 101 Switching Protocols\nUpgrade: websocket\nConnection: Upgrade\nSec-WebSocket-Accept: %s\nSec-WebSocket-Protocol: chat\n";

        // combine secWebSocketKey and the GUID
        merged = secWebSocketKey + GUID;
        print("merged: " + merged);

        // convert to byte[]
        byte[] asBytes = merged.getBytes();
        print("asBytes: " + Arrays.toString(asBytes));

        // SHA-1 hash
        byte[] sha1 = SHA1Hash(asBytes);
        print("sha1: " + Arrays.toString(sha1));

        // base64 encode
        byte[] base64 = base64Encode(sha1);
        print("base64: " + Arrays.toString(base64));

        // reconvert to string to put it into the template
        secWebSocketAccept = new String(base64);

        toSend = String.format(template, secWebSocketAccept);
        print(toSend);

        try {
            stringWriter.write(toSend, 0, toSend.length());
            stringWriter.flush();
        } catch (IOException e) {
            print("hanshake sending failed!");
        }

    }

    private HashMap<String, String> parseHttpHeader(String h) {
        HashMap<String, String> fields = new HashMap<String, String>();

        String[] rows = h.split("\n");
        if (rows.length > 1) {
            fields.put("Prototcol", rows[0]);
            Pattern pattern = Pattern.compile("^([^:]+): (.+)$");
            for (int i = 1; i < rows.length; i++) {
                Matcher matcher = pattern.matcher(rows[i]);
                while (matcher.find()) {
                    if (matcher.groupCount() == 2) {
                        fields.put(matcher.group(1), matcher.group(2));
                    }
                }
            }
        }
        return fields;
    }

    private byte[] SHA1Hash(byte[] bytes) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        md.update(bytes);
        return md.digest();
    }

    private byte[] base64Encode(byte[] bytes) {
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return encodedBytes;
    }
