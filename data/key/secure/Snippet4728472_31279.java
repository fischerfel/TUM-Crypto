byte[] K = Hex.decode(bek.getBytes());
Key key = new SecretKeySpec(K, "AES");
Mac mac = Mac.getInstance("AES/CCM/NoPadding", "BC");
