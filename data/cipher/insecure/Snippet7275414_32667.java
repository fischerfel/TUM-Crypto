String stringKey = "60380131061660211660380426804995";
String message = "This is a secret message";
try {
    SecretKeySpec sks = new SecretKeySpec(stringKey.getBytes(),"AES");
    Cipher c = Cipher.getInstance("AES/ECB/ZeroBytePadding"); // Change to CBC and use appropriate IV
    c.init(Cipher.ENCRYPT_MODE, sks);
    c.update(message.getBytes());
    byte[] ciphertext = c.doFinal();
    Log.i("CE", new String(ciphertext));

    } catch (NoSuchAlgorithmException e) {
        Log.e("CE",e.getMessage());
    } catch (NoSuchPaddingException e) {
        Log.e("CE",e.getMessage());
    } catch (InvalidKeyException e) {
        Log.e("CE",e.getMessage());
    } catch (IllegalBlockSizeException e) {
        Log.e("CE",e.getMessage());
    } catch (BadPaddingException e) {
        Log.e("CE",e.getMessage());
}
