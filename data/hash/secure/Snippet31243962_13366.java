MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(pwd.getBytes("UTF-8"));
byte[] digest = md.digest();
stmt.setString(5, new String(digest, "UTF-8"));

int action = stmt.executeUpdate();
