private byte[] strToByte(String s) {
    int len = s.length();
    byte[] data = new byte[len/2];

    for(int i = 0; i < len; i+=2){
        data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

private long verifyBet(int _num){
    MessageDigest md512 = null;
    try {
        md512 = MessageDigest.getInstance("SHA-512");
    } catch (Exception e) {
        e.printStackTrace();
    }
    String sSeed = "e600f76aa6c520dff7db34559bd05cb1048b1830a07cd81844147a19048fc9be";
    int cSeed = 443944,
        num = _num;
    byte serverSeed[] = strToByte(sSeed),
        clientSeed[] = ByteBuffer.allocate(4).putInt(cSeed).array(),
        betNumber[] = ByteBuffer.allocate(4).putInt(num).array();
    byte data[] = ByteBuffer.allocate(serverSeed.length + clientSeed.length + betNumber.length)
        .put(serverSeed).put(clientSeed).put(betNumber).array();
    data = md512.digest(data);
    data = md512.digest(data);
    long secret = 0;
    boolean found = false;
    while(!found){
        for(int x = 0; x <= 61; x += 3){
            long result = ((data[x] & 0xFF) << 16 | (data[x+1] & 0xFF) << 8) | data[x+2] & 0xFF;
            if (result < 16000000){
                secret = result % 1000000;
                found = true;
                x = 62;
            }
        }
        data = md512.digest(data);
    }
    return secret;
}
