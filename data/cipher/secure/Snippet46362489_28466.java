 public byte[] getPageByteStream(String fileName)
    throws DMSApplicationException
  {
    logger.info(GridFsPagesDAOImpl.class + " Entering in to getPageByteStream DAO Method : " + fileName);

    Query searchQuery = new Query(Criteria.where("filename").is(fileName));
    GridFSDBFile gridFSDBFile = DmsDBUtils.getGridFsOperations().findOne(searchQuery);

    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    byte[] results = null;
    byte[] initVector = null;
    try {
      gridFSDBFile.writeTo(stream);
      byte[] bytes = null;
      bytes = stream.toByteArray();
      Base64 base64 = new Base64();
      byte[] decodedArr = base64.decode(bytes);
      byte[] decArr = Arrays.copyOfRange(decodedArr, 24, bytes.length);
    byte[] secretKey = base64.decode("mkJmh3d2WLNXgmWIv4znTU+IXk7XczlInO9mXmv1iBE=\n");
     String str = new String(secretKey, "UTF-8");
     System.out.println("decodes string :  "+str);
     Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      initVector = Arrays.copyOfRange(decodedArr, 8, 24);

      cipher.init(2, new SecretKeySpec(secretKey, "AES"), new IvParameterSpec(initVector));
      decArr = Arrays.copyOfRange(decodedArr, 24, bytes.length);
      byte[] decArr1 = Arrays.copyOfRange(decArr, 0, decArr.length - decArr.length % 16);

      results = cipher.doFinal(decArr1);

    } catch (Exception e) {
      e.printStackTrace();
      logger.info(GridFsPagesDAOImpl.class + " Exiting from getPageByteStream DAO Method " + e);


      if (gridFSDBFile != null) {
        try {
          stream.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
    finally
    {
      if (gridFSDBFile != null) {
        try {
          stream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    logger.info(GridFsPagesDAOImpl.class + " Exiting from getPageByteStream DAO Method " + fileName);
    return results;
  }
}
