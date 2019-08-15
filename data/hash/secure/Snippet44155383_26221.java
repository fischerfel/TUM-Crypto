       MessageDigest digest = MessageDigest.getInstance("SHA-256");
       byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
       byte[] encoded = Base64.encodeBase64(hash);
        String encryptedPassword=new String(encoded);
