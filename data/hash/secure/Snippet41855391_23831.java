private String getBase64(String data){

    try{

        byte[] enc = data.getBytes("UTF-8");
        return Base64.encodeToString(enc, Base64.DEFAULT);

    }catch (Exception e){
        e.printStackTrace();
        return null;
    }

}

private String getSHA(String data){

    try {

        MessageDigest mda = MessageDigest.getInstance("SHA-512");
        byte[] digesta = mda.digest(data.getBytes("UTF-8"));

        return convertByteToHex(digesta);

    }catch(Exception e){
        e.printStackTrace();
        return null;
    }
}


public String convertByteToHex(byte data[]) {
    StringBuilder hexData = new StringBuilder();
    for (byte aData : data)
        hexData.append(String.format("%02x", aData));
    return hexData.toString();
}
