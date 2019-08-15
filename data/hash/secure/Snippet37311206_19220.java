public String HashMessage(String message){

MessageDigest messageDigest messageDigest=MessageDigest.getInstance("SHA-256");
    messageDigest.update(message.getBytes());

    byte byteData[] = messageDigest.digest();

    StringBuffer hexString = new StringBuffer();
    for (int i=0;i<byteData.length;i++) {
        String hex=Integer.toHexString(mask & byteData[i]);
        if(hex.length()==1)
            hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();

}
