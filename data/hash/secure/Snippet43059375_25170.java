public static String generateJWE3(PlainPan pan) throws Exception {
    Gson gson = new Gson();
    String json = gson.toJson(pan);

    String sharedSecret = Constants.VTS_SHARED_SECRET;
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(sharedSecret.getBytes("UTF-8"));

    String kid = "gUkXp2s5v8y/A?D(G+KbPeShVmYq3t6w";

    KeyGenerator kg = KeyGenerator.getInstance("AES");
    kg.init(256);

    String encryptionKey = Constants.VTS_API_KEY;

    JWEAlgorithm jweAlgorithm = JWEAlgorithm.A256GCMKW;
    EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;

    JWEHeader.Builder headerBuilder = new JWEHeader.Builder(jweAlgorithm, encryptionMethod);

    headerBuilder.keyID(encryptionKey);

    JWEHeader header = headerBuilder.build();
    JWEEncrypter encrypter = new AESEncrypter(digest);

    encrypter.getJCAContext().setProvider(BouncyCastleProviderSingleton.getInstance());
    JWEObject jweObject = new JWEObject(header, new Payload(json));
    jweObject.encrypt(encrypter);

    String serialized = jweObject.serialize();
    JWEObject temp = JWEObject.parse(serialized);

    System.out.println("In JWE Header= " + temp.getHeader());
    System.out.println("In Enc Key= " + temp.getEncryptedKey());
    System.out.println("In IV= " + temp.getIV());
    System.out.println("In CT= " + temp.getCipherText());
    System.out.println("In AT= " + temp.getAuthTag());

    //  decrypt(sharedSecret,serialized);

    return serialized;
}
