    FileInputStream inStream = null;
    DigestInputStream dis = null;
    BufferedInputStream bis = null;

    try {
       md = MessageDigest.getInstance("SHA1");
       inStream = new FileInputStream(d);
       dis = new DigestInputStream(inStream, md);
       bis = new BufferedInputStream(dis);
       while (true) {
            int b = bis.read();
            if (b == -1)
                break;
            }
   } catch (NoSuchAlgorithmException | IOException e) {
       e.printStackTrace();
   } finally {
       try{
          if(inStream!= null)
              inStream.close();
          if(dis != null)
              dis.close();
          if(bis != null)
              bis.close()
      } catch (Exception ex){
          ex.printStackTrace()
      }         
    }
