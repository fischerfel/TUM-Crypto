import java.security.MessageDigest;

String val1 = vars.get("csv_val1");
String val2 = vars.get("csv_val2");
String val3 = vars.get("csv_val3");

String totalString = val1+val2+val3;

MessageDigest md = MessageDigest.getInstance("MD5");
byte[] md5hash = new byte[32];
md.update(totalString.getBytes("utf-8"), 0, totalString.length());
md5hash = md.digest();

StringBuffer sb = new StringBuffer();
for (int i=0;i<md5hash.length;i++) {
    String sval = Integer.toHexString((int) md5hash[i] & 0xFF);
    if(sval.length()== 1)
    {
        sval = "0"+sval;
    }
                sb.append(sval);
}   

log.info("tktest: "+ sb);

vars.putObject("MD5Signature", sb.toString());
