            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec("wedoit".toCharArray(), SALT, 65536, 128); 
//note I only use 128 here because 256 doesnt work despite having local_policy.jar / US_export_policy.jar in {jdk}/lib/security 

        SecretKey tmp = factory.generateSecret(spec);
        secretBytes = tmp.getEncoded();
        secret = new SecretKeySpec(secretBytes, "AES");
        saveSecret(secret); //saves the bytes to a simple file via object / ciper / buffered / fileoutputstream 
