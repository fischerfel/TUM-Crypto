SecretKey key = KeyGenerator.getInstance("AES").generateKey();
ObjectOutputStream secretkeyOS = new ObjectOutputStream(new FileOutputStream("publicKeyFile"));
secretkeyOS.writeObject(key);
secretkeyOS.close();

AlgorithmParameterSpec paramSpec1 = new IvParameterSpec(iv);
session.setAttribute("secParam", paramSpec1);
ObjectOutputStream paramOS = new ObjectOutputStream(new FileOutputStream("paramFile"));
paramOS.writeObject(paramSpec1);
paramOS.close();
