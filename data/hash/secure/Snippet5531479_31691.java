MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
