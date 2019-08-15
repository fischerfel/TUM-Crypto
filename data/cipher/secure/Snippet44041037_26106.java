private String decryptWithKey(String myKey, byte[] strToDecrypt) throws Exception
{
    MessageDigest sha = null;
    try {
        key = myKey.getBytes(CHAR_SCHEME);
        sha = MessageDigest.getInstance("SHA-256");

        key = sha.digest(key);
        key = Arrays.copyOf(key, 32); 
        secretKey = new SecretKeySpec(key, ALGO);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

        byte[] ivByte = new byte[cipher.getBlockSize()];


        ivByte = hexStringToByteArray("0716A494177F29F102AF33AFD0253BA1");;

        System.out.println(new String(ivByte));
