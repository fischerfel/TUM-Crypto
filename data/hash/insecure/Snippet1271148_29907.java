DatatypeConverter.printHexBinary(
        MessageDigest.getInstance("SHA-1").digest(
                cert.getEncoded())).toLowerCase();
