cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding");
cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
byte[] base64byes = loginMessage.getBytes();
byte[] cipherData = cipher.doFinal(base64byes);
System.out.println("RSA: " + cipherData.length); //is 512 long
//4. Send to scheduler
Base64PrintWriter base64encoder = new Base64PrintWriter(out);
base64encoder.writeln(new String(cipherData)); //send string is 1248 long
base64encoder.flush();
