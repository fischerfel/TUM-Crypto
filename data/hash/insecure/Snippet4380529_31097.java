<%@page import="java.util.*" %>
<%@page import="java.security.*" %>

<%
String str = "A string to hash";
String md5string = md5hash(str);

out.println(md5string);
%>

<%!
public String md5hash(String str) {
    MessageDigest md = MessageDigest.getInstance("MD5");

    md.update( str.getBytes() );
    byte[] digest = md.digest();
    StringBuffer hexString = new StringBuffer();
    for (int i = 0, j = digest.length; i < j; i++) {
        String tmp = Integer.toHexString(0xFF & digest[i]);
        if (tmp.length() < 2) {
            tmp = "0" + tmp;
        }

        hexString.append(tmp);
    }

    return hexString.toString();
}
%>