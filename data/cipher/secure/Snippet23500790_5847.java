BufferedReader br = new BufferedReader(new FileReader(publicKeyFile));
String publicKey = br.readLine();//(Key) keyIn.readObject();
br.close();

Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.WRAP_MODE, publicKey);
