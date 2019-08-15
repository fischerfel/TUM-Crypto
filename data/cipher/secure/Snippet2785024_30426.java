  KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
     KeyPair myPair = kpg.generateKeyPair();
     PrivateKey k = myPair.getPrivate();
     System.out.print(k.serialVersionUID);

     Cipher c = Cipher.getInstance("RSA");
     c.init(Cipher.ENCRYPT_MODE, myPair.getPublic());
     String myMessage = new String("Testing the message");

     byte[] bytes  = c.doFinal(myMessage.getBytes());
     String tt = new String(bytes);
     System.out.println(tt);
     Cipher d = Cipher.getInstance("RSA");
     d.init(Cipher.DECRYPT_MODE, myPair.getPrivate());
     byte[] temp = d.doFinal(bytes);
     String tst = new String(temp);
     System.out.println(tst);
