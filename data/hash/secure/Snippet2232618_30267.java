private static byte[] hash(String toHash, String algorithm){
      try{
      MessageDigest dg = MessageDigest.getInstance(algorithm);
      dg.update(toHash.getBytes(ENCODING));
      return dg.digest();
    }catch(Exception e){
      throw new ApiInternalException("Error while hashing string: " + toHash,e);
    }
    }
