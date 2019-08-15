public static void main(String[] args) throws Exception {
        String message = "smile";

        encrypt("Jack the Bear");
    }

    public static void encrypt(String password) throws Exception {

      byte byteData[] =null;

     MessageDigest md = null;
       try {
         md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("US-ASCII"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

      byteData = md.digest();
      System.out.println(Arrays.toString(byteData));


    }
