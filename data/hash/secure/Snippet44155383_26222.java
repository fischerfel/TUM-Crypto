       StringBuilder sb = new StringBuilder();
       MessageDigest digest = MessageDigest.getInstance("SHA-256");
       byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
      for (int i = 0; i < hash.length; i++) {
       sb.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
