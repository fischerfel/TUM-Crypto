try {
      String sName = "localhost";
      int port = 8080;

      Socket client = new Socket(sName, port);
      OutputStream os = client.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);
      BufferedWriter bw = new BufferedWriter(osw);

      //String password = null;
      String password = fieldPassword.getText();
      char[] a = password.toCharArray();
      byte[] salt = new byte[256];

      SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      KeySpec spec = new PBEKeySpec(a, salt, 65536, 256);
      SecretKey tmp = factory.generateSecret(spec);
      SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");


      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, secret);
      String msg = jTextField2.getText();
      byte[] text = msg.getBytes();
      byte[] ciphertext = cipher.doFinal(text);

      String hex=DatatypeConverter.printHexBinary(ciphertext);
      System.out.println(hex);

      String sendMessage = "{'command':'send','dst':'" + jTextField1.getText() + "','msgencrypt':'" + hex  +"'}";`this is json to send to the server`
      bw.write(sendMessage);
      bw.flush();
 } catch (IOException ex) {
      Logger.getLogger(NewJFrame3.class.getName()).log(Level.SEVERE, null, ex);
 }catch (Exception e){ 
 }
