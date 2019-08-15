public static byte[] encrypt(String toEncrypt) {
    try{
      String plaintext = toEncrypt;
      String key = "01234567890abcde";
      String iv = "fedcba9876543210";

      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE,keyspec,ivspec);
      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

      return encrypted;
    } catch ( NoSuchAlgorithmException nsae ) { 
        // What can you do if the algorithm doesn't exists??
        // this usually won't happen because you would test 
        // your code before shipping. 
        // So in this case is ok to transform to another kind 
        throw new IllegalStateException( nsae );
    } catch ( NoSuchPaddingException nspe ) { 
       // What can you do when there is no such padding ( whatever that means ) ??
       // I guess not much, in either case you won't be able to encrypt the given string
        throw new IllegalStateException( nsae );
    }
    // line 109 won't say it needs a return anymore.
  }
