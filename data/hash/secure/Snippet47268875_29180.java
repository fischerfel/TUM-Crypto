val digest = MessageDigest.getInstance("SHA-256")
val hash = new String(digest.digest (password.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).getBytes
