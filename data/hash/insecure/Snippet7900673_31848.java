public class DESHelper {

    private String keyPass = "KeyPass";
    private String keyAlias = "keyAlias";
    private String keyStorePass="keyStorePass";
    private KeyStore ks = null;
    private static final String KEYSTORE_PATH = "keystore/mykeystore.keystore";
    private static final String KEYSTORE_TYPE = "jceks";    
    protected static String DES_KEY = "pin";
    private Map<String, byte[]> mKeys = new HashMap<String, byte[]>();

    public String decrypt(byte[] message, String salt) throws Exception {

        SecretKey secretKey = getSecretKey();

        if (secretKey == null) 
        {
            throw new GeneralSecurityException("No secret key");
        }

        final IvParameterSpec iv = new IvParameterSpec(generateIV(keyAlias, salt, DES_KEY));
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public String encrypt(String value, String salt) {

        byte ciphertext[][] = new byte[2][];

        try
        {           
            SecretKey secretKey = getSecretKey();

            if (secretKey == null) 
            {
                throw new GeneralSecurityException("No secret key");
            }

            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            byte iv[] = generateIV(keyAlias, salt, DES_KEY);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            ciphertext[0] = cipher.update(stringToByteArray(value));
            ciphertext[1] = cipher.doFinal(generateMAC(keyAlias, salt, DES_KEY, value));
        }
        catch (GeneralSecurityException gse) 
        {
            gse.printStackTrace();
            return null;
        }

        if (ciphertext[0] == null) 
        {
            ciphertext[0] = new byte[0];
        }

        if (ciphertext[1] == null) 
        {
            ciphertext[1] = new byte[0];
        }

        byte ct[] = new byte[ciphertext[0].length + ciphertext[1].length];
        System.arraycopy(ciphertext[0], 0, ct, 0, ciphertext[0].length);
        System.arraycopy(ciphertext[1], 0, ct, ciphertext[0].length, ciphertext[1].length);

        // Convert encrypted bytes to a String
        StringBuffer result = new StringBuffer(2 * ct.length + 1);
        result.append("b");           // first byte indicates encoding method.
        Base64 enc = new Base64();
        result.append(enc.encode(ct));

        return result.toString();
    }

    /**
     * 
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws GeneralSecurityException 
     */
    public SecretKey getSecretKey() throws InvalidKeyException, NoSuchAlgorithmException, GeneralSecurityException{

        byte des_key[];
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DESede") ;

        if (secretKeyFactory == null) 
        {
            throw new GeneralSecurityException("No DESede factory");
        }

        try
        {
            des_key = getKey(keyAlias);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        SecretKey secretKey = secretKeyFactory.generateSecret(new DESedeKeySpec(des_key));

        return secretKey;
    }

    /**
     * Generate MAC
     */
    protected byte[] generateMAC(String type,String salt,String key,String value) {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            if (type!=null) {
                md.update(stringToByteArray(type));
            }

            if (salt!=null) {
                md.update(stringToByteArray(salt));
            }

            if (key!=null) {
                md.update(stringToByteArray(key));
            }

            if (value!=null) {
                md.update(stringToByteArray(value));
            }

            byte[] sha = md.digest();
            byte[] mac = new byte[4];

            for (int scan=0;scan<sha.length;scan++) 
            {
                mac[scan%4]=(byte)(mac[scan%4]^sha[scan]);
            }

            return mac;
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    protected byte[] generateIV(String type,String salt,String key) {
        try 
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            if (type!=null) {
                md.update(stringToByteArray(type));
            }

            if (salt!=null) {
                md.update(stringToByteArray(salt));
            }

            if (key!=null) {
                md.update(stringToByteArray(key));
            }

            byte[] sha = md.digest();
            byte[] iv = new byte[8];

            for (int scan=0;scan<sha.length;scan++) 
            {
                iv[scan%8]=(byte)(iv[scan%8]^sha[scan]);
            }

            return iv;
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    protected byte[] getKey(String alias) throws GeneralSecurityException, IOException {

        synchronized (mKeys) 
        {
            byte k[] = mKeys.get(alias);
            if (k != null) {
                return k;
            }
        }

        byte key[] = getKeystoreKey(alias, 192);

        if (key == null) 
        {
            return null;
        }

        if (key.length != 24) {
            return null;
        }

        synchronized (mKeys) 
        {
            mKeys.put(alias, key);
            return key;
        }
    }

    protected byte[] stringToByteArray(String aString) {
        try 
        {
            return aString.getBytes("UTF-8");
        } 
        catch (java.io.UnsupportedEncodingException ex) 
        {
            System.out.println("Exception in stringToByteArray " + ex.getMessage());
            return null;
        }
    }

    /**
    * Returns String From An Array Of Bytes
    */
    private String bytes2String(byte[] bytes) {

        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i <= bytes.length; i++) 
        {
            stringBuffer.append((char) bytes[i]);
        }

        return stringBuffer.toString();

    }


    protected byte[] getKeystoreKey(String aKeyName,int size) throws GeneralSecurityException, IOException {

        Key key = getKeystore().getKey(aKeyName, keyPass.toCharArray());

        if (key==null) 
        {
            System.out.println("KeyStore key "+aKeyName+" not found");
            return null;
        }

        byte keyBytes[] = key.getEncoded();

        if (keyBytes.length != (size/8) ) 
        {
            System.out.println("KeyStore key "+aKeyName+" invalid length. Expected:"+size+" was: "+(keyBytes.length*8));
            return null;
        }

        return keyBytes;
    }

    protected KeyStore getKeystore() throws GeneralSecurityException, IOException {
        if ( ks == null )
        {
            ks = KeyStore.getInstance(KEYSTORE_TYPE);
            char[] password = keyStorePass.toCharArray();

            if (password == null) 
            {
                System.out.println("Couldn't get keystore password from config");
            }

            // get the keystore as a resource
            InputStream is = getClass().getClassLoader().getResourceAsStream(KEYSTORE_PATH);

            try
            {
                ks.load(is, password);
            }
            finally
            {
                if (is != null)
                {
                    is.close();
                }
            }
        }

        return ks;
    }

    public class Base64 {
        private char sBase64[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

        /** Creates a new instance of Base64 */
        public Base64()
        {
        }

        public byte[] decode(String aSource)
        {
            int aSource_length=aSource.length();

            while (aSource_length>0 && aSource.charAt(aSource_length-1)=='=') aSource_length--;

            if (aSource_length%4==1) 
            {
                return null;
            }

            int len = (aSource_length/4)*3 + aSource_length%4 - (aSource_length%4==0?0:1);
            byte result[] = new byte[len];

            int o_ofs=0;
            int i_ofs=0;

            while (i_ofs<aSource_length) 
            {
                int i24=0;
                for (int bscan=0;bscan<4;bscan++) 
                {
                    if (i_ofs<aSource_length) 
                    {
                        int decode = decode(aSource.charAt(i_ofs));
                        if (decode==-1) return null;
                        i24=(i24<<6)+decode;
                    } 
                    else 
                    {
                        i24=i24<<6;
                    }

                    i_ofs++;
                }

                if (o_ofs<len) 
                {
                    result[o_ofs++]=(byte)(i24>>16);
                }

                if (o_ofs<len) 
                {
                    result[o_ofs++]=(byte)(i24>>8);
                }

                if (o_ofs<len) 
                {
                    result[o_ofs++]=(byte)(i24);
                }
            }
            return result;
        }

        private int decode(char c)
        {
            if (c>='A' && c<='Z') 
            {
                return c-'A';
            }

            if (c>='a' && c<='z') 
            {
                return c-'a'+26;
            }

            if (c>='0' && c<='9') 
            {
                return c-'0'+52;
            }

            if (c=='+') 
            {
                return 62;
            }

            if (c=='/') 
            {
                return 63;
            }

            return -1;
        }

        public String encode(byte[] aSource)
        {
            int result_length = aSource.length*4/3 + (aSource.length%3==0?0:1);

            char result[]=new char[ ((aSource.length+2)/3)*4 ];

            int o_ofs=0;
            int i_ofs=0;

            while (i_ofs<aSource.length) {

                int i24=0;
                for (int bscan=0;bscan<3;bscan++) {
                    if (i_ofs>=aSource.length) {
                        i24=i24<<8;
                    } 
                    else {
                        i24=(i24<<8)+(aSource[i_ofs]&0xff);
                    }

                    i_ofs++;
                }

                if (o_ofs<result_length) result[o_ofs++]=sBase64[(i24>>18)&63];
                if (o_ofs<result_length) result[o_ofs++]=sBase64[(i24>>12)&63];
                if (o_ofs<result_length) result[o_ofs++]=sBase64[(i24>>6)&63];
                if (o_ofs<result_length) result[o_ofs++]=sBase64[(i24)&63];
            }
            while (o_ofs<result.length) result[o_ofs++]='=';
            return new String(result);
        }
    }

}
