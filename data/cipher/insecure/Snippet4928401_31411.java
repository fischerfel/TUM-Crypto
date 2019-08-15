 public BoolString tryEncrypt(String inString, String key){
        boolean success= true;
        String err="";
        String outString="Encrypted"; // BoolString.value

        try {
            byte[] byteKey= key.getBytes("UTF8");   
            if (byteKey.length != 24) {
                success= false;
                err= "Key is "+byteKey.length+" bytes. Key must be exactly 24 bytes in length.";
                throw new Exception(err); // could also return here
            }
            KeySpec ks= new DESedeKeySpec(byteKey); 
            SecretKeyFactory skf= SecretKeyFactory.getInstance("DESede");
            SecretKey sk= skf.generateSecret(ks);
            Cipher cph=Cipher.getInstance("DESede");
            cph.init(Cipher.ENCRYPT_MODE, sk);
            byte[] byteInString= inString.getBytes("UTF8");
            byte[] byteEncoded= cph.doFinal(byteInString);
            outString= Base64.encodeToString(byteEncoded, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException e){err="Unable to convert key to byte array."; success= false;}
        catch (InvalidKeyException e){err="Unable to generate KeySpec from key";success= false;} 
        catch (NoSuchAlgorithmException e){err="Unable to find algorithm.";success= false;}
        catch (InvalidKeySpecException e){err="Invalid Key Specification";success= false;}
        catch (NoSuchPaddingException e){err="No such padding";success= false;}
        catch (IllegalArgumentException e){err="Illegal argument";success= false;}   
        catch (Exception e){err=e.getMessage();success= false;}

        return new BoolString(success,err,outString);
    }

// a utility class to signal success or failure, return an error message, and return a useful String value
// see Try Out in C#
public final class BoolString {
    public final boolean success;
    public final String err;
    public final String value;

    public BoolString(boolean success, String err, String value){
        this.success= success;
        this.err= err;
        this.value= value;
    }
}
