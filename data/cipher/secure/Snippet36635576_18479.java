//FIXME somehow the cipher is truncating the message or something
private byte[] encrypt(File file, String password) throws IOException{

    Path path = Paths.get(file.toURI());
    byte[] messageBytes = Files.readAllBytes(path);
    //Message bytes: 3253
    System.out.printf("Message bytes: %d%n", messageBytes.length);
    byte[] messageName = path.getFileName().toString().getBytes();

    byte[] cipherBytes = {};
    byte[] iv = {};
    try{

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(S_KEY_FACTORY_ALG);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), SALT, KEY_SPEC_ROUNDS, AES_KEY_SIZE);
        SecretKey key = keyFactory.generateSecret(pbeKeySpec);
        System.out.printf("Secret key take 1: " + Arrays.toString(key.getEncoded()) + "%n");

        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");

        //FIXME this is not encrypting the message correctly
        Cipher cipher = Cipher.getInstance(CIPHER_ALG);
        final byte[] nonce = new byte[GCM_NONCE_LENGTH];
        SecureRandom random = SecureRandom.getInstanceStrong();
        random.nextBytes(nonce);
        GCMParameterSpec paramSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        iv = paramSpec.getIV();

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, paramSpec);
        cipher.updateAAD(messageName);
        cipher.update(messageBytes);
        cipherBytes = cipher.doFinal();

    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }catch(NoSuchPaddingException e){
        e.printStackTrace();
    }catch(InvalidKeyException e){
        e.printStackTrace();
    }catch(InvalidAlgorithmParameterException e){
        e.printStackTrace();
    }catch(InvalidKeySpecException e){
        e.printStackTrace();
    }catch(BadPaddingException e){
        e.printStackTrace();
    }catch(IllegalBlockSizeException e){
        e.printStackTrace();
    }

    //IV bytes: 16
    System.out.printf("IV bytes: %d%n", iv.length);
    //Cipher bytes: 21
    System.out.printf("Cipher bytes: %d%n", cipherBytes.length);
    //Cipher bytes: [121, 68, 7, -69, -35, -9, -83, 101, -60, -80, 42, 59, -67, 126, 18, -82, 79, -60, 34, -125, 12]
    System.out.printf("Cipher bytes: " + Arrays.toString(cipherBytes) + "%n");

    //FIXME somehow the cipher is shortening the message or something
    return cipherBytes;
}
