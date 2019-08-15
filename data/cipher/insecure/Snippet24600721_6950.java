private static String saveToFile(Serializable object, String fileName,
        Context ctx) {
    try {
        Cipher cipher = null;
        cipher = Cipher.getInstance("DES");
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        SealedObject sealedObject = null;
        sealedObject = new SealedObject(object, cipher);
        CipherOutputStream cipherOutputStream = null;

        FileOutputStream fos = ctx.openFileOutput(fileName,
                Context.MODE_PRIVATE);
        cipherOutputStream = new CipherOutputStream(
                new BufferedOutputStream(fos), cipher);
        ObjectOutputStream outputStream = null;
        outputStream = new ObjectOutputStream(cipherOutputStream);
        outputStream.writeObject(sealedObject);
        outputStream.close();

        return "Save Complete!";

    } catch (IOException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
        return e.getMessage();
    } catch (InvalidAlgorithmParameterException e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
