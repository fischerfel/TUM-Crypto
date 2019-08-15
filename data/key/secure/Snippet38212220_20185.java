byte hmacKey[] = passphrase.getBytes(StandardCharsets.UTF8);
Key key = new SecretKeySpec(hmacKey,signatureAlgorithm.getJcaName());
