public class DES {

    public static String encode(String str, String key) throws Exception {
        byte[] rawKey=Base64.decode(key);
        IvParameterSpec sr=new IvParameterSpec(rawKey);
        DESKeySpec dks=new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
        SecretKey secretKey=keyFactory.generateSecret(dks);

        javax.crypto.Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

        byte data[]=str.getBytes("UTF8");
        byte encryptedData[]=cipher.doFinal(data);
        return Base64.encode(encryptedData).trim();
    }

    public static String decode(String str, String key) throws Exception {
        byte[] rawKey=Base64.decode(key);
        IvParameterSpec sr=new IvParameterSpec(rawKey);
        DESKeySpec dks=new DESKeySpec(rawKey);
        SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("DES");
        SecretKey secretKey=keyFactory.generateSecret(dks);
        Cipher cipher=Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);
        byte encryptedData[]=Base64.decode(str);
        byte decryptedData[]=cipher.doFinal(encryptedData);
        return new String(decryptedData, "UTF8").trim();
    }
}
