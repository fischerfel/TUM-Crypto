String md5 = MD5Encoder.encode(str);    
Cursor cursor = db.rawQuery("SELECT desc FROM datatable WHERE md5 = '?' ",new String[] { md5 });

public class MD5Encoder {

public static String encode(String source){

    String result = null;

    try {

        MessageDigest md = MessageDigest.getInstance("md5");

        byte[] output = md.digest(source.getBytes());

        StringBuffer sb = new StringBuffer();

        for(int i=0;i<output.length;i++){

            String s = Integer.toHexString(0xff&output[i]);

            if(s.length()==1){
                sb.append("0"+s);
            }else{
                sb.append(s);
            }
        }
        result= sb.toString();

    } catch (NoSuchAlgorithmException e) {

        e.printStackTrace();
    }
    return result;
}
 }
