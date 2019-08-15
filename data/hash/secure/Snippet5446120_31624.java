String input = "your string";
MessageDigest digest = MessageDigest.getInstance("SHA-256");
digest.update(input.getBytes("UTF-8"));
byte[] hash = digest.digest();
