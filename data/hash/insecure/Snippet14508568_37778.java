     MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(password.toUpperCase().getBytes());

    byte byteData[] = md.digest();

    //convert the byte to hex format
    StringBuffer hexString = new StringBuffer();
    for (int i=0;i<byteData.length;i++) {
        String hex=Integer.toHexString(0xff & byteData[i]);
        if(hex.length()==1) hexString.append('0');
        hexString.append(hex);
    }

   Javascript
    v_password = jQuery.trim(v_password);
v_userid = jQuery.trim(v_userid);
var v_digest = str_md5(v_password.toUpperCase()); // Implementation in java?
var v_pswdDigest = hex_md5(v_digest + v_userid.toUpperCase());
return v_pswdDigest;
