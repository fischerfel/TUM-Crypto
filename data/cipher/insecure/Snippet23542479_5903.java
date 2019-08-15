public String encrypt(String data) {
    String Key = "My_PRIVATE_KEY";
    try {
        byte[] KeyData = Key.getBytes();
        SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, KS);
        return bytesToHex(cipher.doFinal(data.getBytes()));

    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}

private String bytesToHex(byte[] data) {
    if (data == null) {
        return null;
    }

    int len = data.length;
    String str = "";
    for (int i = 0; i < len; i++) {
        if ((data[i] & 0xFF) < 16) {
            str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
        }
        else {
            str = str + java.lang.Integer.toHexString(data[i]&0xFF);
        }
    }
    return str;
}
