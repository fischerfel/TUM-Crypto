private static byte[] xorBytes(byte[] rndA, byte[] rndB) {
    // TODO Auto-generated method stub
    byte[] b = new byte[rndB.length];
    for (int i = 0; i < rndB.length; i++) {
        b[i] = (byte) (rndA[i] ^ rndB[i]);
    }
    return b;
}


public static byte[] decrypt(byte[] key, byte[] enciphered_data) {

    try {
        byte[] iv = new byte[] { 0,0,0,0,0,0,0,0 };
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        SecretKey s = new SecretKeySpec(key, "DESede");
        Cipher cipher;
        cipher = Cipher.getInstance("DESede/CBC/NoPadding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, s, ivParameterSpec);
        byte[] deciphered_data = cipher.doFinal(enciphered_data);
        return deciphered_data;
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
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
