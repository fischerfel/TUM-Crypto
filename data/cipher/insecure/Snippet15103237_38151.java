//SecretKey to save data
    private SecretKey desKey;

    //Complete encryption and decryption work
    private Cipher c;

    //Save encryption results
    private byte[] cipherResultByte;

    private final static String Algorithm = "DESede/ECB/NoPadding";//Encryption method/operation mode

    private static DESedeKeySpec dks;

    private static SecretKeyFactory keyFactory;   

    public DESeseTest02() {

        Security.addProvider(new com.sun.crypto.provider.SunJCE());

        try {

            dks = new DESedeKeySpec("00000000000000000000000000000000".getBytes());   

             keyFactory = SecretKeyFactory.getInstance("DESede");


            //Generate key
            desKey=keyFactory.generateSecret(dks);


            //Generate Cipher object, specify its support DES algorithm
            c=Cipher.getInstance(Algorithm);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    public byte[] createEncryptor(byte[] b){

        try {

//          System.out.println(str);
            //根据密钥，对Cipher进行初使化，DECRYPT_MODE加密模式
            c.init(Cipher.ENCRYPT_MODE, desKey);

//          byte[] input=str.getBytes();
//          System.out.println(input.length);

            //Encryption, the results will be preserved
            cipherResultByte=c.doFinal(b);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return cipherResultByte;
    }
