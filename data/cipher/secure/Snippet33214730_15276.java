    public static String encrypt(String text) throws UnsupportedEncodingException {
        byte iv[] = new byte[16];
        byte[] encrypted = null;
        BASE64Encoder enc = new BASE64Encoder();
        try {            
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKeySpec(), ivspec);
            encrypted = cipher.doFinal(text.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return URLEncoder.encode(enc.encode(iv)+enc.encode(encrypted), "UTF-8");
    }

    public static String decrypt(String text) {
        byte iv[] = new byte[16];
        String decrypted  = "";
        byte[] splitText = null;
        byte[] textToDecrypt = null;
        BASE64Decoder dec = new BASE64Decoder();
        try { 
            text = URLDecoder.decode(text, "UTF-8");
            text = text.replaceAll(" ", "+");
            splitText = dec.decodeBuffer(text);
            splitText.toString();
            for(int i=0;i<16;i++){
                iv[i]=splitText[i];
            }
            textToDecrypt = new byte[splitText.length - 16];
            for(int i=16;i<splitText.length;i++){
                textToDecrypt[i-16]=splitText[i];
            }
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKeySpec(), ivspec);
           decrypted = new String(cipher.doFinal(textToDecrypt));
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return decrypted;
    }

static SecretKeySpec spec = null;
public static SecretKeySpec getKeySpec() throws IOException,
                                            NoSuchAlgorithmException {
    if (spec == null) {
        String keyFile = "aes_key.key";
        spec = null;
        InputStream fis = null;
        fis = Config.class.getClassLoader().getResourceAsStream(keyFile);
        byte[] rawkey = new byte[16];
        fis.read(rawkey);
        fis.close();
        spec = new SecretKeySpec(rawkey, "AES");
}
return spec;
}
