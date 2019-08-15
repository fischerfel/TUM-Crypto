public static String decrypt3DES(String Key, String data) throws Exception
{
    Cipher cipher = null;
    byte[] text = null;
    byte[] desKey = null;
    Key keySpec = null;
    try {

        if (Key.length() <= 16) {
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            desKey = byteConvertor(Key);
            keySpec = new SecretKeySpec(desKey, "DES");
        } else if (Key.length() >= 32) {
            cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            desKey = byteConvertor(Key);
            keySpec = new SecretKeySpec(desKey, "DESede");
        }
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        text = cipher.doFinal(byteConvertor(data));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return alpha2Hex(byteArr2String(text));
}
