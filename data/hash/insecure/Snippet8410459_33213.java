// Create the WebSocket handshake response.
HttpResponse res = new DefaultHttpResponse(HTTP_1_1,
    new HttpResponseStatus(101, "Web Socket Protocol Handshake"));
res.addHeader(Names.UPGRADE, WEBSOCKET);
res.addHeader(CONNECTION, Values.UPGRADE);

// Fill in the headers and contents depending on handshake method.
// New handshake specification has a challenge.
if (req.containsHeader(SEC_WEBSOCKET_KEY1)
        && req.containsHeader(SEC_WEBSOCKET_KEY2)) {

    // New handshake method with challenge
    res.addHeader(SEC_WEBSOCKET_ORIGIN, req.getHeader(ORIGIN));
    res.addHeader(SEC_WEBSOCKET_LOCATION, getWebSocketLocation(req));

    String protocol = req.getHeader(SEC_WEBSOCKET_PROTOCOL);

    if (protocol != null) {
        res.addHeader(SEC_WEBSOCKET_PROTOCOL, protocol);
    }

    // Calculate the answer of the challenge.
    String key1 = req.getHeader(SEC_WEBSOCKET_KEY1);
    String key2 = req.getHeader(SEC_WEBSOCKET_KEY2);
    int a = (int) (Long.parseLong(key1.replaceAll("[^0-9]", "")) / key1
            .replaceAll("[^ ]", "").length());
    int b = (int) (Long.parseLong(key2.replaceAll("[^0-9]", "")) / key2
            .replaceAll("[^ ]", "").length());
    long c = req.getContent().readLong();
    ChannelBuffer input = ChannelBuffers.buffer(16);
    input.writeInt(a);
    input.writeInt(b);
    input.writeLong(c);
    ChannelBuffer output = ChannelBuffers
            .wrappedBuffer(MessageDigest.getInstance("MD5").digest(
                    input.array()));
    res.setContent(output);
} else {
    // Old handshake method with no challenge:
    res.addHeader(WEBSOCKET_ORIGIN, req.getHeader(ORIGIN));
    res.addHeader(WEBSOCKET_LOCATION, getWebSocketLocation(req));
    String protocol = req.getHeader(WEBSOCKET_PROTOCOL);
    if (protocol != null) {
        res.addHeader(WEBSOCKET_PROTOCOL, protocol);
    }
}

// Send the response...
