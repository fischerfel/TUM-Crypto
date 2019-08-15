public static void decrypt(){
    Cipher cipher;
    SecretKeySpec key;
    byte [] keyBytes;
    byte [] pt;
    byte [] ct;
    String plaintxt;

    keyBytes = new byte [] {(byte)0xFE, (byte)0xDC, (byte)0xBA, (byte)0x98, (byte)0x76, (byte)0x54, (byte)0x32, (byte)0x10};
    key = new SecretKeySpec(keyBytes, "DES");
    ct = new byte [] {(byte) 0x2C, (byte) 0xE6, (byte) 0xDD, (byte) 0xA4, (byte) 0x98, (byte) 0xCA, (byte) 0xBA, (byte) 0xB9};

    try{
        cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        pt = cipher.doFinal(ct);
        printByteArray(pt);
        plaintxt = byteToHex(pt);
        hexToAscii(plaintxt);
    }
    catch(Exception e){
        e.printStackTrace();
    }

}
