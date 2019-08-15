try {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] keyBytes = keystore.get("secretkey");
        byte[] nonceBytes = nonce.getBytes("UTF-8");

        outputStream.write( nonceBytes );
        outputStream.write( keyBytes );

        byte[] bytesToEncrypt = outputStream.toByteArray();

        byte[] hashedBytes = digest.digest(bytesToEncrypt);

        Socket clientSocket = new Socket(myserver, myport);
        OutputStream outToServer = clientSocket.getOutputStream();
        outToServer.write(hashedBytes);
    }
