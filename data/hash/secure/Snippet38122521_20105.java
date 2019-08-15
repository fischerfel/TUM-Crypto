    public void testEncryption(String algo) {
    String shared_secret = "LyQnklSrxsk3Ch2+AHi9HoDW@//x1LwM123QP/ln";
    try {

        // Step 1 - Create SHA-256 digest of the shared key
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(shared_secret.getBytes("UTF-8"));

        // Step 2 - generate a 256 bit Content Encryption Key(CEK)
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey cek = kg.generateKey();

        // Step 3 - encrypt the CEK using 256 bit digest generated in Step 1
        // and 96 bit random IV. Algorithm should be

        // random 96 bit Initialize Vector
        SecureRandom random = new SecureRandom();
        // byte iv[] = new byte[96];
        // random.nextBytes(iv);
        byte iv[] = random.generateSeed(96);
        System.out.println("IV: " + toBase64(iv) + " length: " + iv.length);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        GCMParameterSpec gspec = new GCMParameterSpec(96, iv);

        // encrypt
        Cipher cipher = Cipher.getInstance(algo);
        System.out.println(String.format("CEK Cipher alg:%S provider:%S", cipher.getAlgorithm(),
                cipher.getProvider().getName()));

        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(digest, "AES"), gspec);
        byte[] result = cipher.doFinal(cek.getEncoded());

        System.out.println(String.format("Encrypted CEK :%S", toBase64(result)));

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
