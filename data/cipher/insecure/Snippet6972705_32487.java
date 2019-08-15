    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("DES");
    } catch (Exception ex) {
        System.out.println(ex.toString());
        //   return;
    }

    byte[] keyData = keyString.getBytes();
    SecretKeySpec key = new SecretKeySpec(keyData, 0, keyData.length, "DES");

    try {
        cipher.init(Cipher.ENCRYPT_MODE, key);
    } catch (Exception ex) {
        System.out.println(ex.toString());
        //  return;
    }

    int cypheredBytes = 0;

    byte[] inputBytes = null;
    try {
        inputBytes = textToEnrypt.getBytes("UTF-8");
        inputBytes = textToEnrypt.getBytes();
    } catch (Exception ex) {
        System.out.println(ex.toString());
        //return;
    }

    byte[] outputBytes = new byte[100];

    try {
        cypheredBytes = cipher.doFinal(inputBytes, 0, inputBytes.length,
                outputBytes, 0);
    } catch (Exception ex) {
        System.out.println(ex.toString());
        //return;
    }
    /*
    String str = new String(outputBytes, 0, cypheredBytes);
    buffer = str;
    System.out.println("Encrypted string = " + str);
     * */

    newResponse = new byte[cypheredBytes];

    for (int i = 0; i < cypheredBytes; i++) {

        newResponse[i] = outputBytes[i];

    }

    buffer=new String(newResponse);
    System.out.println("Encripted text is:"+buffer);
    return newResponse;
}

public void decrypt(String textToDecrypt, String keyString) {
    Cipher cipher;
    try {
        cipher = Cipher.getInstance("DES");
    } catch (Exception ex) {
        System.out.println(ex.toString());
        return;
    }

    byte[] keyData = keyString.getBytes();
    SecretKeySpec key = new SecretKeySpec(keyData, 0, keyData.length, "DES");

    try {
        cipher.init(Cipher.DECRYPT_MODE, key);
    } catch (Exception ex) {
        System.out.println("2. " + ex.toString());
        return;
    }

    int cypheredBytes = 0;

    byte[] inputBytes;
    try {
        inputBytes =textToDecrypt.getBytes("UTF-8");
       inputBytes = textToDecrypt.getBytes();
    } catch (Exception ex) {
        System.out.println("3. " + ex.toString());
        return;
    }

    byte[] outputBytes = new byte[100];

    try {
        cypheredBytes = cipher.doFinal(inputBytes, 0, inputBytes.length,
                outputBytes, 0);
    } catch (Exception ex) {
        System.out.println("4. " + ex.toString());
        return;
    }

    String str = new String(outputBytes, 0, cypheredBytes);
    System.out.println("Decrypted string = " + str);
}
