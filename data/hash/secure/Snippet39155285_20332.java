MessageDigest md = MessageDigest.getInstance("SHA-256");

md.update(/* PUT THE IMAGE CONTENTS HERE */);
byte[] digest = md.digest();

String uniqueName = Base64.encodeToString(digest, Base64.DEFAULT);
