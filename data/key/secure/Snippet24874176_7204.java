 public byte[] calculateDiverseKey(byte [] input) throws InvalidKeyException, NoSuchAlgorithmException {
    AesCmac mac = null;
    mac = new AesCmac();
    SecretKey key = new SecretKeySpec(masterKey, "AES");
    mac.init(key);  //set master key
    mac.updateBlock(input); //given input
    for (byte b: input) System.out.print(" "+b);
    return mac.doFinal();
    }
