 public static final String PUBLIC = "q/9CujExqL6rsMMO22WWIotoXDCw5KEmGQJqL9UJEfoErwZ9ZCm3OwMTSlAMSfoXEMA04Y1rhfYC3MtU/7dYEoREfsvOPGDBWanTKyMzv2otCfiURyQoghEdkhv3ipQQaaErT7lfBKobJsdqJlvxo4PCOUas2Z6YpoMYgthzTiM=";
    public static final String EXPONENT = "AQAB";

    public static PublicKey getPublicKey() throws Exception{
        byte[] modulusBytes = Base64.decode(PUBLIC, 0);
        byte[] exponentBytes = Base64.decode(EXPONENT,    0);

        BigInteger modulus = new BigInteger(1, (modulusBytes) );
        BigInteger exponent = new BigInteger(1, (exponentBytes));

        RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static byte[] encrypt(Key publicKey, String s) throws Exception{
        byte[] byteData = s.getBytes("UTF-8");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); 
        byte[] encryptedData = cipher.doFinal(byteData);
        return encryptedData;
    }

    public static String arrayAsString (byte [] array){
        String p = "";
        for (int i = 0; i < array.length; i++) {
            p +=  unsignedToBytes(array[i]);
            if (i < array.length - 1)
                p+= ",";
        }
        return p;
    }

    public static int unsignedToBytes(byte b) {
        return b & 0xFF;
    }

   public static void main(String[] args){
        PublicKey publicKey = getPublicKey();
        byte [] encrypted = encode(publicKey, "passwordHere");
        String pass = arrayAsString(encrypted);
        webservice.AuthenticateUser("testAdmin", pass); 
   }
