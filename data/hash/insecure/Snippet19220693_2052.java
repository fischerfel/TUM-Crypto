public class MyClass {

    public String myMethodCall() {

        return new String(MessageDigest.getInstance("SHA1").digest("someString".getBytes()));

    }        

}
