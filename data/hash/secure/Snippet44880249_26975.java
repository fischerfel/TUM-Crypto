byte[] encodedID = MessageDigest.getInstance("SHA-256").digest(id.getBytes("UTF-8"));
String encodedID64 = Base64.encodeToString(encodedID,Base64.NO_WRAP);
