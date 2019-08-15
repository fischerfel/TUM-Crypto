    public static InputStream getEncryptedInputStream(String key, String path) {
    try {
        InputStream is = ResourceManager.getResourceStatic(path);
        SecretKeySpec keyspec = new SecretKeySpec(getHash(key),"AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, keyspec);
        return new CipherInputStream(is,c);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    return null;
    }
