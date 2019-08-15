UUID uuid = UUID.randomUUID();
MessageDigest salt = MessageDigest.getInstance("SHA-256");
salt.update(uuid.toString().getBytes("UTF-8"));
