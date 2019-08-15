    String ipWithSecret = secret + "123.123.123.123";
    byte[] ipBytes = ipWithSecret.getBytes(StandardCharsets.UTF_8);
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] mdBytes = md.digest(ipBytes);
    System.out.println("MD5:      " + mdBytes);
    System.out.println("US ASCII: " + new String(mdBytes, StandardCharsets.US_ASCII));
    System.out.println("Hex:      " + HexBin.encode(mdBytes));
    System.out.println("Base64:   " + Base64.encodeBase64String(mdBytes));        
    System.out.print("Binary:   ");
    for (byte b : mdBytes) {
        System.out.print(Integer.toBinaryString(b & 255 | 256).substring(1));
    }
