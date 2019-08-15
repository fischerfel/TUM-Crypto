System.out.println("row1=" + test1 + ":" + tst1.getHash(test1));
System.out.println("row2=" + test2 + ":" + tst1.getHash(test2));
System.out.println("row3=" + test3 + ":" + tst1.getHash(test3));

private String getHash(String inputStr){
    try{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputStr.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    catch(Exception e)
    {
        e.printStackTrace();
        return null;
    }
}

row1=hi,hello,bye:cfe40e96aa052a484208c2aefb6f39bb
row2=gg,hello,bye:f652785f0e214507e6aea44ecd3ffb7a
row3=hi,hello,bye:cfe40e96aa052a484208c2aefb6f39bb
