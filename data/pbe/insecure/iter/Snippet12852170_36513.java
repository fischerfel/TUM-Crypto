private static byte[] bytes;
  Cipher ecipher;
  Cipher dcipher;

  // 8-byte Salt
  byte[] salt = {
      (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
      (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
  };

  // Iteration count
  int iterationCount = 19;

  public String setup(String passPhrase) 
  {
      String output = null;
      try {
          // Create the key
          KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
          SecretKey key = SecretKeyFactory.getInstance(
              "PBEWithMD5AndDES").generateSecret(keySpec);

          ecipher = Cipher.getInstance(key.getAlgorithm());
          dcipher = Cipher.getInstance(key.getAlgorithm());

          // Prepare the parameter to the ciphers
          AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

          // Create the ciphers
          ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
          dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

          // print key
          System.out.println("key  = " + key);
          System.out.println("paramSpec  = " + paramSpec);


          output = key.toString();
      //    showToast("setting up key " + output);
      //    showToast("key size " + output.length());
          System.out.println("key Size " + output.length());

      } catch (java.security.InvalidAlgorithmParameterException e) {
      } catch (java.security.spec.InvalidKeySpecException e) {
      } catch (javax.crypto.NoSuchPaddingException e) {
      } catch (java.security.NoSuchAlgorithmException e) {
      } catch (java.security.InvalidKeyException e) {
      }

      return output;
  }
