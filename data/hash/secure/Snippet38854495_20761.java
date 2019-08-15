MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
passwordHash = Server.readLine();
if((new String(hash, StandardCharsets.UTF_8)).equals(passwordHash)) authentication.success();
