MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
Server.write(new String(hash, StandardCharsets.UTF_8);
