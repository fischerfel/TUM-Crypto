    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();


    for (int i = 0; i < imageBytes.length; i++)
    {
        digest.update(imageBytes[i]);
    }

    String hashString = new String(encodeHex(digest.digest()));
