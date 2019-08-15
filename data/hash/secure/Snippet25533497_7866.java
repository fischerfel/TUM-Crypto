public enum SECURE_HASH_TYPE{MD2, MD5, SHA, SHA256, SHA384, SHA512}

private static String SecureHashToString(SECURE_HASH_TYPE hash){

    String hashString = "SHA-256";                              //default value

    if(hash != null){
        switch(hash){
            case MD2:
                hashString = "MD2";
                break;

            case MD5:
                hashString = "MD5";
                break;

            case SHA:
                hashString = "SHA";
                break;

            case SHA256:
                hashString = "SHA-256";
                break;

            case SHA384:
                hashString = "SHA-384";
                break;

            case SHA512:
                hashString = "SHA-512";
                break;

            default:
                hashString = "SHA-512";
                break;
        }
    }

    return hashString;
}



public static byte[] getByteHashCode(Object obj, SECURE_HASH_TYPE hashing){
    if(obj == null)
        return null;

    MessageDigest md;
    byte[] byteData = null;

    try {
        md = MessageDigest.getInstance(SecureHashToString(hashing));

        md.update(ObjectUtil.toByteArray(obj));

        byteData = md.digest();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return byteData;
}


 public static byte[] toByteArray(Object obj){
    if(obj == null)
        return null;

    if(obj instanceof String)
        return ((String)obj).getBytes();

    ByteArrayOutputStream baos = null;
    ObjectOutputStream oos = null;
    byte[] byte_array = null;

    try{
        baos = new ByteArrayOutputStream();
        oos = new ObjectOutputStream(baos);

        oos.writeObject(obj);
        byte_array = baos.toByteArray();

        baos.close();
        oos.close();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

    return byte_array;
}
