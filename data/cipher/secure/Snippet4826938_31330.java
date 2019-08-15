static String encryptString(final String RAWDATA, boolean ENCODE) { // This
                  // was a
                  // custom
   Log.d("TEST1", iv.toString()); 
  String encrypted = null;
  byte[] encryptedBytes = null;
  byte[] key;

  key = AES_V1_KEY.getBytes();
  //SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
  // Instantiate the cipher
  Cipher cipher = null;
  try {
   String input = Integer.toString(RAWDATA.length()) + '|' +RAWDATA;
   KeyGenerator kgen = KeyGenerator.getInstance(KEY_ALGORITHM);
   SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
   sr.setSeed(key);
    kgen.init(128, sr); // 192 and 256 bits may not be available
   SecretKey skey = kgen.generateKey();
      byte[] raw1 = skey.getEncoded();

      SecretKeySpec skeySpec = new SecretKeySpec(raw1,KEY_ALGORITHM);
   cipher = Cipher.getInstance(CIPHER_ALGORITHM);
   cipher.init(Cipher.ENCRYPT_MODE, skeySpec,MyEnc1.ivParameterSpec);
   //cipher.init(Cipher.ENCRYPT_MODE, MyEnc1.secretKeySpec,MyEnc1.ivParameterSpec);
    // encryptedBytes = cipher.doFinal(nullPadString(input).getBytes());

   encryptedBytes = cipher.doFinal(nullPadString(input).getBytes());
  }catch(InvalidAlgorithmParameterException e){
   Log.d("error",e.toString());
  } 
  catch (NoSuchAlgorithmException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (NoSuchPaddingException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (InvalidKeyException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IllegalBlockSizeException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (BadPaddingException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }

  encrypted = new String(encryptedBytes);
  if (ENCODE)
   encrypted = new String(Base64.encodeBytes(encryptedBytes));
  else
   encrypted = new String(encryptedBytes);
  return encrypted;

  // /return encrypted;
 }// method end
