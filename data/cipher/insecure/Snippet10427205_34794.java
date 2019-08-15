byte[] key = new byte[]{31, 30, 31, 36, 32, 11, 11, 11, 22, 26,
               30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
myKeySpec = new DESedeKeySpec(key);
mySecretKeyFactory = SecretKeyFactory.getInstance("TripleDES");
de = mySecretKeyFactory.generateSecret(myKeySpec);

    Cipher c = Cipher.getInstance("TripleDES");
c.init(Cipher.DECRYPT_MODE, key);

    int l = completeHexStr.length();

    if (l%8==1){
        completeHexStr = completeHexStr + "0000000";
    }else if (l%8==7){
        completeHexStr = completeHexStr + "0";
    }
byte decordedValue[] =completeHexString.getBytes();
byte[] decValue = c.doFinal(decordedValue);
String decryptedValue = new String(decValue);
System.out.println("decryptedValue= " + decryptedValue);
