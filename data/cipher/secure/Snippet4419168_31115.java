      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    
  DocumentBuilder builder = dbf.newDocumentBuilder();  
  Document doc = builder.parse(new File(inputFilePath));
  NodeList nl = doc.getElementsByTagName("ds:SignatureValue");
  if (nl.getLength() == 0) {
     throw new Exception("Cannot find SignatureValue element");
   }
  String signature = "OZg96GMrGh0cEwbpHwv3KDhFtFcnzPxbwp9Xv0pgw8Mr9+NIjRlg/G1OyIZ3SdcOYqqzF4/TVLDi5VclwnjBAFl3SEdkyUbbjXVAGkSsxPQcC4un9UYcecESETlAgV8UrHV3zTrjAWQvDg/YBKveoH90FIhfAthslqeFu3h9U20=";
  X509Certificate cert = X509Certificate.getInstance(new FileInputStream(<a file path>));
  PublicKey pubkey = cert.getPublicKey();
  Cipher cipher = Cipher.getInstance("RSA","SunJCE");
  cipher.init(Cipher.DECRYPT_MODE, pubkey);
  byte[] decodedSignature = Base64Coder.decode(signature);
  cipher.update(decodedSignature);
  byte[] sha1 = cipher.doFinal();


  System.out.println(Base64Coder.encode(sha1));
