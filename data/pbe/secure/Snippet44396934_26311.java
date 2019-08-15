SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), iterations, length);
        SecretKey key = skf.generateSecret(spec);
        byte[] res = key.getEncoded();
