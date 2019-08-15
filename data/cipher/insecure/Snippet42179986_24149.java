try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(PKey.getBytes("UTF-8")); // <-- note encoding
        byte[] toEncryptByte = encString.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("DESEDE/EBC/X9.23PADDING");
        SecretKeySpec myKey = new SecretKeySpec(hash,"DESede");
        cipher.init(Cipher.ENCRYPT_MODE, myKey);
        byte[] encryptedPlainText = cipher.doFinal(toEncryptByte);

        String encrypted = Base64.encodeToString(encryptedPlainText, 0);

        return encrypted;


    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e){
        e.printStackTrace();
    } catch (InvalidKeyException e){
        e.printStackTrace();
    } catch (IllegalBlockSizeException e){
        e.printStackTrace();
    } catch (BadPaddingException e){
        e.printStackTrace();
    }

    return null;
}
