 Key privateKey = YourCarrierClass.getYourVariablePrivate();
 Cipher cipher;
 BigInteger passwordInt = new BigInteger(ajaxSentPassword, 16);
 byte[] dectyptedText = new byte[1];
 try {
   cipher = javax.crypto.Cipher.getInstance("RSA/ECB/PKCS1Padding");
   byte[] passwordBytes = passwordInt.toByteArray();
   cipher.init(Cipher.DECRYPT_MODE, privateKey);
   dectyptedText = cipher.doFinal(passwordBytes);
   } catch(NoSuchAlgorithmException e) {
   } catch(NoSuchPaddingException e) { 
   } catch(InvalidKeyException e) {
   } catch(IllegalBlockSizeException e) {
   } catch(BadPaddingException e) {
   }
   String passwordNew = new String(dectyptedText);
   System.out.println("Password new " + passwordNew);
