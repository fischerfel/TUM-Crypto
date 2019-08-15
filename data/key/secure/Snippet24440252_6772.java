private static byte[] decryptPBKDF2WithBC(char[] password, byte[] data, byte[] salt, byte[] iv)
      throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
      InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

    PBEParametersGenerator generator = new PKCS5S2ParametersGenerator();
    generator.init(PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(password), salt,1024);
    KeyParameter params = (KeyParameter)generator.generateDerivedParameters(256);

    byte[] endcoded = params.getKey();
    SecretKey key = new SecretKeySpec(endcoded, "AES");

    Cipher ciph = Cipher.getInstance("AES/CBC/PKCS5Padding");

    ciph.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    return ciph.doFinal(data);
  }

  public String decrypt(String encrypted){
    String salt = SALT;
    String password = PASSWORD;
    String[] parts = encrypted.split("--");
    if (parts.length != 2) return null;

    byte[] encryptedData = Base64.decode(parts[0], Base64.DEFAULT);
    byte[] iv = Base64.decode(parts[1], Base64.DEFAULT);

    byte[] result = null;
    try {
      result = decryptPBKDF2WithBC(password.toCharArray(), encryptedData, salt.getBytes(), iv);
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeySpecException e) {
      e.printStackTrace();
    } catch (NoSuchPaddingException e) {
      e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    }
    catch(Exception e){
      e.printStackTrace();
    }

    try {
      return new String(result, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
    catch(Exception e){
      e.printStackTrace();
      return null;
    }
  }
