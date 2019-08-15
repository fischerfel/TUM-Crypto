Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

KeyGenerator keyGen = KeyGenerator.getInstance("AES");
SecretKey secKey = (SecretKey) keyGen.generateKey();
SecretKey secKey1 = (SecretKey) keyGen1.generateKey();

byte[] stringKey=secKey.getEncoded();

cipher.init(Cipher.ENCRYPT_MODE, secKey1);
byte[] DykeyBytes = cipher.doFinal(stringKey);

StringBuffer sbselect2=new StringBuffer();
sbselect2.append("SELECT keylock FROM ");
sbselect2.append(UserConstants.USER_DETAILS_TABLE_NAME1);
sbselect2.append(" where Username='" + un + "'");
ps2=conn.prepareStatement(sbselect2.toString());
ResultSet rs1 =ps2.executeQuery();
rs1.next();
String Enkey = rs1.getString("keylock");

System.out.println("Encrypted+Encoded key from current user "+Enkey);
rs1.close();

//decrypting the DB stored Key
Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
cipher2.init(Cipher.DECRYPT_MODE, singlekey );
byte[] dynamicKey = Enkey.getBytes("UTF8");
// below is where the error points  to at user.dao.UserDao.isRegisteredUser(UserDao.java:313)
byte[] decryptedBytes = cipher2.doFinal(dynamicKey);

Object DeKey = new String(decryptedBytes);//under check
SecretKeySpec key = new SecretKeySpec(decryptedBytes, "AES");
