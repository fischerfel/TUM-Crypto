byte fport= (byte) 0x01;//Byte.valueOf("1");
byte dirValue = (byte) 0x00;//Byte.valueOf("0");
byte[] devAddr = {0x08,0x00,0x00,0x00};//hexStringToByteArray("08000000");
short fCnt = 200;

 public byte[] getClearPayLoad(byte[] payload, byte[] _nwkSKey, byte[] _appSKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, MalformedPacketException {
    byte[] key;

    if (fport == 0) {
        if (_nwkSKey == null) {
            throw new IllegalArgumentException("Missing nwkSKey");
        }
        if (_nwkSKey.length != 16) {
            throw new IllegalArgumentException("Invalid nwkSKey");
        }
        key = _nwkSKey;
    } else {
        if (_appSKey == null) {
            throw new IllegalArgumentException("Missing appSKey");
        }
        if (_appSKey.length != 16) {
            throw new IllegalArgumentException("Invalid appSKey");
        }
        key = _appSKey;
    }
    int k = (int) Math.ceil(payload.length / 16.0);
    System.out.println("payload length: "+payload.length);
    System.out.println("k is: "+ k);
    ByteBuffer a = ByteBuffer.allocate(16 * k);
    a.order(ByteOrder.LITTLE_ENDIAN);
    for (int i = 1; i <= k; i++) {
        a.put((byte) 0x01);
        a.put(new byte[]{0x00, 0x00, 0x00, 0x00});
        a.put(dirValue);
        a.put(devAddr);
        a.putInt(fCnt);
        a.put((byte) 0x00);
        a.put((byte) i);
    }
    Key aesKey = new SecretKeySpec(key, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, aesKey);
    byte[] s = cipher.doFinal(a.array());
    byte[] paddedPayload = new byte[16 * k];
    System.arraycopy(payload, 0, paddedPayload, 0, payload.length);
    byte[] plainPayload = new byte[payload.length];
    for (int i = 0; i < payload.length; i++) {
        plainPayload[i] = (byte) (s[i] ^ paddedPayload[i]);
    }           
    return plainPayload;
}
