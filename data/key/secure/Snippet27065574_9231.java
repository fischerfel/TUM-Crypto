byte[] keyBytes = /* set the key somehow */;
SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
Arrays.equals(keyBytes, keySpec.getEncoded()); // true
