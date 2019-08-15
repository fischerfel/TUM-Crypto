 public  String SHAsum(String convertme) {
           MessageDigest md = null;
           byte[] data;
        try {
            md = MessageDigest.getInstance("SHA-1");
            data=convertme.getBytes("utf8");
            md.update(data);
            md.hashCode();

            //md.update( key.getBytes() );
            return new BigInteger( 1, md.digest() ).toString(16);


        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } //This could also be SHA1withDSA, no exception handling
        catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

        }
