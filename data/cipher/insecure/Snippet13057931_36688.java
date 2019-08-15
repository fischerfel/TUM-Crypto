public boolean connect(String ip, int port) {
    try {
        socket = new Socket(ip, port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        dos.writeByte(0x02);
        dos.writeByte(0x00);
        writeString(username);
        writeString(ip);
        dos.writeInt(port);
        if (dis.readByte() != 0xFD)
            return false;
        String serverId = readString();
        byte[] publicKey = new byte[dis.readShort()];
        for (int i = 0; i < publicKey.length; i++)
            publicKey[i] = dis.readByte();
        byte[] token = new byte[dis.readShort()];
        for (int i = 0; i < token.length; i++)
            token[i] = dis.readByte();
        PublicKey serverPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
        byte[] sharedSecret = new byte[16];
        new Random().nextBytes(sharedSecret);
        URL url = new URL("http://session.minecraft.net/game/joinserver.jsp?user=" + username + "&sessionId=" + session + "&serverId=" + serverId);
        url.openConnection();

        Cipher cipher = Cipher.getInstance("AES");
        return true;
    }
    catch (Exception ex) { System.out.println("Failed to login for " + username); ex.printStackTrace(); }
    return false;
}
