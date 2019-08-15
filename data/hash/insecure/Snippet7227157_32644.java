    import java.security.MessageDigest;
    class Enc{

            public String encryptPassword(String password) throws Exception{
                    byte[] bArray=password.getBytes();
                    MessageDigest md=MessageDigest.getInstance("SHA-1");
                    md.reset();
                    md.update(bArray);
                    byte[] encoded=md.digest();
                    System.out.println(encoded.toString());

                    return "";
            }
            public static void main(String args[]){
                    try{
                    Enc e=new Enc();
                    e.encryptPassword("secret");
                    }catch(Exception e){e.printStackTrace();}
            }
    }

/*

jabira-whosechild-lm.local 12:40:35 % while (true); do java Enc; done 
[B@77df38fd
[B@77df38fd
[B@60072ffb
[B@77df38fd
[B@6016a786
[B@60072ffb
[B@77df38fd
[B@77df38fd
[B@77df38fd
[B@77df38fd
[B@77df38fd
[B@77df38fd
[B@77df38fd
[B@6016a786
[B@6f507fb2
[B@77df38fd
[B@6016a786
[B@77df38fd
[B@77df38fd
[B@6016a786
*/
