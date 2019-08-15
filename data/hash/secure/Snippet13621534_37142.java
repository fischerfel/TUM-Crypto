String plainText = "text" + "salt";

MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
byte[] hash = messageDigest.digest( plainText.getBytes() );

System.out.println("Result: " + new String(hash));
