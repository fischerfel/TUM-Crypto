     public static void main(String[] args) throws Exception {
        //Encryption
        AES encr = new AES();   
        encr.setKey("KEY");
        encr.setSalt("SALT");
        encr.setup();
        String message = "This is just an example";
        System.out.println("Message : " + message);



        byte[] code = encr.encrypt(message);
        System.out.println("Encrypted Strinng : "+ new String(code, "UTF-8"));

        //Decryption
        AES dec = new AES();
        dec.setKey("INCORRECT"); //<--- incorrect 
        dec.setSalt("SALT");
        dec.setup();

        System.out.println(dec.decryptString(code));
    }




        public synchronized  void setKey(String key) throws UnsupportedEncodingException {
        this.key = key.getBytes("UTF-8");
        isPasswordAlreadySet = true;
    }


    public synchronized  void setSalt(String salt) throws UnsupportedEncodingException {
        this.salt = salt.getBytes("UTF-8");
    }

    public synchronized  void setup() throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.update(key);
    digest.update(salt);
    byte[] raw = digest.digest();

    skeySpec = new SecretKeySpec(raw, "AES");
    cipher = Cipher.getInstance("AES");
    }  

public synchronized byte[] encrypt(byte[] klartext) throws Exception {
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] encrypted = cipher.doFinal(klartext);

    return encrypted;
    }

    public synchronized byte[] encrypt(String klartext) throws Exception{
    return encrypt(klartext.getBytes("UTF-8")); 
    }






     public synchronized byte[] decrypt(byte[] code) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(code);
    return original;
    }

    public synchronized double decryptDouble(byte[] code) throws Exception {
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(code);
    return doubleFromBytes( original);
    }
