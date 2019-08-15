main(){
    int blockSize = 64;
    String key = "key";
    String message = "The quick brown fox jumps over the lazy dog";
    StringBuilder ipad = new StringBuilder();
    StringBuilder opad = new StringBuilder();
    StringBuilder prime = new StringBuilder();
    for(byte b : key.getBytes()){
        prime.append(Util.binToHex(Util.unsign(b)));
    }
    while(prime.length() < blockSize * 2){
        prime.append("00");
    }
    for(int i = 0; i < blockSize; ++i){
        opad.append("5c");
        ipad.append("36");
    }

    String iKeyPad = Util.xOr(prime.toString(), ipad.toString());
    String oKeyPad = Util.xOr(prime.toString(), opad.toString());

    String first = hash(Util.hexToString(iKeyPad) + message);
    String second = hash(Util.hexToString(oKeyPad + first));
    if(second.equals("de7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9")){
        System.out.println("Success");
    }
    else{
        System.out.println(second);
    }
}
private String hash(String str){
    try{
        byte[] bytes;
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(str.getBytes());
        bytes = d.digest();
        StringBuilder s = new StringBuilder();
        for(byte b : bytes){
            s.append(Util.binToHex(Util.unsign(b)));
        }
        return s.toString();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return null;
}
public static String xOr(String a, String b){
    a = hexToBin(a);
    b = hexToBin(b);
    StringBuilder str = new StringBuilder();
    for(int i = 0; i < a.length(); ++i){
        if(a.charAt(i) != b.charAt(i)){
            str.append("1");
        }
        else{
            str.append("0");
        }
    }
    return binToHex(str.toString());
}

public static String hexToString(String str){
    StringBuilder string = new StringBuilder();
    for(int i = 0; i < str.length(); i += 2){
        int number = Integer.valueOf(str.substring(i, i + 2), 16);
        string.append((char)number);
    }
    return string.toString();
}
public static String binToHex(String str){
    StringBuilder string = new StringBuilder();
    for(int i = 0; i < str.length(); i += 4){
        String temp = str.substring(i, i + 4);
        string.append(Integer.toString(Integer.valueOf(temp, 2), 16));
    }
    return string.toString();
}
public static String unsign(byte b){
    int x = b;
    if(x < 0){
        x = x * -1;
        String str = Integer.toString(x, 2);
        while(str.length() != 8){
            str = "0" + str;
        }
        str = inverse(str);
        str = addOne(str);
        return str;
    }
    else{
        String str = Integer.toString(x, 2);
        while(str.length() != 8){
            str = "0" + str;
        }
        return str;
    }
}
