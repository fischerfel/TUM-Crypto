String text = "YOLO";
MessageDigest digest = MessageDigest.getInstance("SHA-256");
byte[] hash = digest.digest(text.getBytes("UTF-8"));
System.out.println(hash.toString());
