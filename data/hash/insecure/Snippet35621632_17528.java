    MessageDigest md5 = null;
    byte[] buffer = new byte[1024];
    int bytesRead = 0;
    String md5ChkSumHex = null;
    InputStream is = null;

   String filePath = "D:/myFile.txt";

    try 
    {
        is = new FileInputStream(new File(filePath));

        md5 = MessageDigest.getInstance("MD5");

        try {
            while ((bytesRead = is.read(buffer)) > 0) {
                md5.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        byte[] md5ChkSumBytes = md5.digest();

        StringBuffer sb = new StringBuffer();

        /*Convert to hex*/

       for (int j = 0; j < md5ChkSumBytes.length; j++) 
        {
            String hex = Integer.toHexString(
                    (md5ChkSumBytes[j] & 0xff | 0x100)).substring(1, 3);
            sb.append(hex);
        }

        md5ChkSumHex = sb.toString();


    } catch (Exception nsae) {

    }

    return md5ChkSumHex;
