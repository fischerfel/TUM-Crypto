 MessageDigest digest = MessageDigest.getInstance("SHA-256");
 byte[] hash = digest.digest("Nitrogen".getBytes(StandardCharsets.UTF_8));
