String passwordToEncrypt = ....//user entered
byte[] passwordToEncryptBytes = passwordToEncrypt.getBytes();

KeyGenerator keyGen = KeyGenerator.getInstance("AES");
SecretKey mySecretKey = keyGen.generateKey();

Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
cipher.init(Cipher.ENCRYPT_MODE, mySecretKey);
IvParameterSpec ivParameter = 
                    cipher.getParameters().getParameterSpec(IvParameterSpec.class);
byte[] encryptedPasswordData = cipher.doFinal(passwordToEncryptBytes);
