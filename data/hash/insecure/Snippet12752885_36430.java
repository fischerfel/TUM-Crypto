    final MessageDigest sha = MessageDigest.getInstance("SHA-1");
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    outputStream.write(namespaceBytes);
    outputStream.write(nameBytes);
    sha.update(outputStream.toByteArray());
    final byte[] hash = sha.digest();
