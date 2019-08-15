import java.security.MessageDigest;
//imports omitted

@Test
public void test() throws ProcessingException{
String test = "iamastringwithäöchars";           
System.out.println(this.digest(test));      
}

public String digest(String data) throws ProcessingException {
    MessageDigest hash = null;

    try{
        hash = MessageDigest.getInstance("SHA-256");
    }
    catch(Throwable throwable){
        throw new ProcessingException(throwable);
    }
    byte[] digested = null;
    try {
        digested = hash.digest(data.getBytes("ISO-8859-1"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    String ret = BinaryUtils.BinToHexString(digested);
    return ret;
}
