public class Prova {

public static void main(String[] args){
    String s = "with the lights out is less dangerous here we are now entertain us";
    s = cripta(s);
    System.out.println(s);
    s = decripta(s);
    System.out.println(s);
}

public static String cripta(String s){
    System.out.println("lunghezza stringa:"+s.length());
    byte[] input = s.getBytes();
    byte[] output;
    byte[] keyBytes = hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    byte[] ivBytes = hexStringToByteArray("AAAAAAAAAAAAAAAA");
    String out = "";


    SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede" );
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
    try {
        Cipher cp = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cp.init(Cipher.ENCRYPT_MODE, key);
        byte[] criptati = new byte[cp.getOutputSize(input.length)];
        int enc_len = cp.update(input, 0, input.length, criptati, 0);
        enc_len += cp.doFinal(criptati, enc_len);
        out = new String(criptati);
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ShortBufferException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return out;
}

public static String decripta(String s){

    byte[] input = s.getBytes();
    byte[] output;
    byte[] keyBytes = hexStringToByteArray("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
    byte[] ivBytes = hexStringToByteArray("AAAAAAAAAAAAAAAA");
    String out = "";

    try {
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cp = Cipher.getInstance("DESede/ECB/NoPadding");
        cp.init(Cipher.DECRYPT_MODE,  key);
        byte[] decrypt = new byte[cp.getOutputSize(input.length)];

        int dec_len = cp.update(input, 0, input.length, decrypt, 0);
        System.out.println(dec_len);
        dec_len += cp.doFinal(decrypt, dec_len );
        System.out.println(dec_len);
        out = new String(decrypt);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ShortBufferException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return out;
}

private static byte[] hexStringToByteArray(String s) {
    // TODO Auto-generated method stub
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;

}
