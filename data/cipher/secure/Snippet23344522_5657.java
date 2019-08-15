public static byte[] encrypt(byte[] dataToEncrypt, PublicKey pubk){
    try{
    System.out.println("in encrypt");
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, pubk);

    byte[] encrypted = new byte[dataToEncrypt.length];
    System.out.println("Imagebyte length: " + dataToEncrypt.length);
    byte[] temp = new byte[53];
    byte[] temp2 = null;
    int x, y, z = 0;
    int repeats = dataToEncrypt.length / 53;
    System.out.println("Iterations: " + repeats);
    for (z = 0; z < repeats; z++) {
        System.out.println("Iteration number: " + z);
        int offset = z * 53;
        for (x = 0; x < 53; x++) {
            temp[x] = (byte) dataToEncrypt[offset + x];
        }
        temp2 = cipher.doFinal(temp);
        for (y = 0; y < 53; y++) {
            encrypted[offset + y] = (byte) temp2[y];
        }
        temp2 = null;
    }
    return encrypted;
    }catch(Exception e){
        return null;
    }
}


public static byte[] decrypt(byte[] bytesIn, PrivateKey prvk){
    try {
        System.out.println("in decrypt");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        System.out.println("read out" + prvk);
        cipher.init(Cipher.DECRYPT_MODE, prvk);
        byte[] decrypted = new byte[bytesIn.length];
        System.out.println("Imagebyte length: " + bytesIn.length);
        byte[] temp = new byte[53];
        byte[] temp2 = null;
        int x, y, z = 0;
        int repeats = bytesIn.length / 53;
        System.out.println("Iterations: " + repeats);

        for (z = 0; z < repeats; z++) {
            System.out.println("Iteration number: " + z);
            int offset = z * 53;
            for (x = 0; x < 53; x++) {
                temp[x] = (byte) bytesIn[offset + x];
            }
            temp2 = cipher.doFinal(temp);
            for (y = 0; y < 53; y++) {
                decrypted[offset + y] = (byte) temp2[y];
            }
            temp2 = null;
        }
        return decrypted;
    } catch (Exception e) {
        System.err.println(e);
        return null;
    }
}
