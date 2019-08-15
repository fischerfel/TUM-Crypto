  private void getRegistrationUser() throws IOException, InvalidKeyException, InvalidAlgorithmParameterException{
    String username = br.readLine();

    System.out.println("[+] [Server - Thread " + Thread.currentThread().getId() + "] Username received");

    //SHA384 of shared key
    byte[] hash = ObjectHash.getByteHashCode(shared_key, SECURE_HASH_TYPE.SHA384);

    byte[] IV = new byte[16];
    byte[] cipherKey = new byte[32];

    int j, limit;

    for(j = 0; j < IV.length; j++)
        IV[j] = hash[j];

    limit = j;

    for(; j < hash.length; j++)
        cipherKey[j - limit] = hash[j];

    try{

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivparameters = new IvParameterSpec(IV);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(cipherKey, "AES"), ivparameters);

        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        String encryptedHashPassword = (String)((SealedObject)ois.readObject()).getObject(cipher);

        String decryptedHashPassword = decipherMessage(encryptedHashPassword, cipherKey);

        ois.close();

        sendACK();

    }
    catch (IOException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }   
}
