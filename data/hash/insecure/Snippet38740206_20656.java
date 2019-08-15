BouncyCastleProvider bcp = new BouncyCastleProvider();
bcp.put("Alg.Alias.MessageDigest.SHA1WITHRSA", "SHA-1");
bcp.put("Alg.Alias.MessageDigest.1.2.840.113549.1.1.5", "SHA-1");
Security.addProvider(bcp);

MessageDigest.getInstance("SHA1WITHRSA", "BC"); // throws an exception
MessageDigest.getInstance("1.2.840.113549.1.1.5", "BC"); // throws an exception
