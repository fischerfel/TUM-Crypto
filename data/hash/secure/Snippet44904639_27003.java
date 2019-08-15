MessageDigest md=MessageDigest.getInstance("SHA-256");
md.update(pass.getBytes());
byte byteData[]=md.digest();
StringBuffer sb=new StringBuffer();
for(int i=0;i<byteData.length;i++)
    sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
BufferedWriter output=new BufferedWriter(new FileWriter("passwords.txt",true));
output.write(userTF.getText()+" "+sb.toString()+"\n");
output.close();
