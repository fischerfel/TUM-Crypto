MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
      localMessageDigest.update(String.format(Locale.US, "%s:%s", new Object[] { paramString1, paramString2 }).getBytes());
      byte[] arrayOfByte = localMessageDigest.digest();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; ; i++)
      {
        if (i >= arrayOfByte.length)
          return localStringBuffer.toString();
        localStringBuffer.append(Integer.toString(256 + (0xFF & arrayOfByte[i]), 16).substring(1));
      }
