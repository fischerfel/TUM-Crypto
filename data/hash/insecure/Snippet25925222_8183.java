public boolean encrypt(String x) throws Exception {
    System.out.println("x is " + x);
    java.security.MessageDigest d = java.security.MessageDigest.getInstance("SHA-1");
    d.update(x.getBytes());
    d.reset();
    String result = d.digest().toString() + "     ";
    char[] tempCharArray = result.toCharArray();
    String bitArray = "";
    for(int i=0; i< tempCharArray.length; i++){
        bitArray += String.format("%8s", Integer.toBinaryString(tempCharArray[i] &
          0xff)).replace(' ', '0');
    }

    result = bitArray.substring(0,8);
    return result;
}
