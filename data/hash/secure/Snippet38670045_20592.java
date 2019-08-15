private byte[] reverse(byte[] b){
    int i = b.length - 1;
    byte newB[] = new byte[4];
    for(int x = 0; x < b.length; x++){
        newB[x] = b[i];
        i--;
    }
    return newB;
}

private byte[] strToByte(String s) {
    int len = s.length();
    byte[] data = new byte[len/2];

    for(int i = 0; i < len; i+=2){
        data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
    }
    return data;
}

private long verifyBet(){
    //MessageDigest md256 = null;
    MessageDigest md512 = null;
    try {
        //md256 = MessageDigest.getInstance("SHA-256");
        md512 = MessageDigest.getInstance("SHA-512");
    } catch (Exception e) {
        e.printStackTrace();
    }
    String //res = "ServerSeed = ",
        sSeed = "e600f76aa6c520dff7db34559bd05cb1048b1830a07cd81844147a19048fc9be";
        //sHash = "ca90022ac66a6a77d8b5072e101bff505c2bff552b1b9a0785f0c438d5b6228f";
    int cSeed = 443944,
        num = 0;
    byte serverSeed[] = strToByte(sSeed),
        //serverHash[] = strToByte(sHash),
        clientSeed[] = reverse(ByteBuffer.allocate(4).putInt(cSeed).array()),
        betNumber[] = reverse(ByteBuffer.allocate(4).putInt(num).array());
    byte data[] = ByteBuffer.allocate(serverSeed.length + clientSeed.length + betNumber.length)
        .put(serverSeed).put(clientSeed).put(betNumber).array();
    data = md512.digest(data);
    data = md512.digest(data);
    long secret = 0;
    boolean found = false;
    while(!found){
        for(int x = 0; x <= 61; x += 3){
            long result = (data[x] << 16 | data[x+1] << 8) | data[x+2];
            if (result < 16000000){
                secret = result % 1000000;
                found = true;
            }
        }
        data = md512.digest(data);
    }
    return secret;
}
