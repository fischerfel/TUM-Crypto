private String encryptText(String plainText){
    try {
        this.iv = prefs.getByteArray("IV", null);
        if(this.iv == null) { //If not set, set the IV
            cipher.init(Cipher.ENCRYPT_MODE, this.secret);
            AlgorithmParameters params = cipher.getParameters();
            this.iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            prefs.putByteArray("IV", this.iv);
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, this.secret, new IvParameterSpec(this.iv));
        }

        byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));
        String ret = new String(Base64.encodeBase64(ciphertext));
        return ret;
    } catch (InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
    } catch (InvalidAlgorithmParameterException ex) {
        Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
    }
    return "";
   }
