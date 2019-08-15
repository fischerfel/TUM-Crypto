private Key getPublicKey() {
    try {
        InputStream fis = activity.getResources().openRawResource(R.raw.publick);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Key RSApublicKey = (Key) ois.readObject();
        return RSApublicKey;
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}

private Key getPrivateKey() {
    try {
        InputStream fis = activity.getResources().openRawResource(R.raw.privatek);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Key RSAprivateKey = (Key) ois.readObject();
        return RSAprivateKey;
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    catch (ClassNotFoundException e) {
        Log.e("error", "Error: " + e);
        e.printStackTrace();
    }
    return null;
}

public byte[] encrypt(String data) {
    byte[] encrypted = null;
    try {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
        encrypted = cipher.doFinal(data.getBytes());
    }
    catch (IllegalBlockSizeException e) {
            e.printStackTrace();
    }
    catch (BadPaddingException e) {
        e.printStackTrace();
    }
    catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }
    catch (NoSuchProviderException e) {
        e.printStackTrace();
    }
    return encrypted;
}

public String decrypt(byte[] encrypted) {
    byte[] decrypted = null;
    try {
        Cipher cipher;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
        decrypted = cipher.doFinal(encrypted);
    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    catch (NoSuchPaddingException e) {
        e.printStackTrace();
    }
    catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    catch (BadPaddingException e) {
        e.printStackTrace();
    }
    catch (NoSuchProviderException e) {
        e.printStackTrace();
    }
    return new String(decrypted);
}
