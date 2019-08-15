 public void firstDigitalSignature() throws IOException, NoSuchAlgorithmException, Throwable
 {

     //*Generate Message Digest1*
     byte[] buffer=null;
     buffer = new byte[(int) inputFile1.length()]; 
     FileInputStream fis = new FileInputStream(inputFile1);
     fis.read(buffer);
     MessageDigest messageDigest = MessageDigest.getInstance("SHA-256"); 
     messageDigest.update(buffer);
     digestBytes = messageDigest.digest();  
     //convert the byte to hex format method 2
     StringBuffer hexString = new StringBuffer();
     for(int i=0;i<digestBytes.length;i++){
         hexString.append(Integer.toHexString(0xFF & digestBytes[i]));
     }
     System.out.println("Message Digest-1: "+hexString.toString()); 

   //*Using private key to encrypt the image-Digital signauture1 *
     Signature signature = Signature.getInstance("SHA256withRSA");
     signature.initSign(privateKey);
     signature.update(digestBytes);
     encryptmd= signature.sign();
     StringBuffer hexString2 = new StringBuffer();
     for(int i=0;i<encryptmd.length;i++){
          hexString2.append(Integer.toHexString(0xFF & encryptmd[i]));
          x=hexString2.toString();
          File file = new File("c://Directroy111");
          if (!file.exists()) {  
             if (file.mkdir()) {
                 System.out.println("Doctor is created!");
             } else {
                 System.out.println("Failed to create Doctor!");
             }
      }
         BufferedWriter out = new BufferedWriter(
         new FileWriter("C:\\Directroy111\\Digital Signature Doctor.txt"));
         out.write(x);
         out.close();
         this.copyImageFiles(sourceFile, destinationDir);
      }
          System.out.println("Message Digest Encrypted-1: "+hexString2.toString()+"\n");
   }


public void firstVerify() throws IOException, NoSuchAlgorithmException, Throwable
{
//Generate Message Digest1 - Decrypt
String verifyfile= "c:\\Directroy111\\2.jpg";
File decryptfile= new File(verifyfile);
byte[] buffer2=null;
buffer2 = new byte[(int) decryptfile.length()]; //array type is integer, thats why we use int here
FileInputStream fis2 = new FileInputStream(decryptfile);
fis2.read(buffer2);
MessageDigest messageDigest2 = MessageDigest.getInstance("SHA-256"); 
messageDigest2.update(buffer2);
byte[] digestBytes2 = messageDigest2.digest();  
StringBuffer hexString22 = new StringBuffer();
for(int i=0;i<digestBytes2.length;i++){
  hexString22.append(Integer.toHexString(0xFF & digestBytes2[i]));
}
System.out.println("Message Digest(Hash)-1(Decryption): "+hexString22.toString()); //System.out.println(hexString);

   //*******Decypt*************//
Signature signature = Signature.getInstance("SHA256withRSA");
  signature.initVerify(publicKey);
  //FileReader read= new FileReader("C:\\TEMP1\\Doctor\\Digital Signature Doctor.txt");
  FileInputStream br2 = new FileInputStream("C:\\Directroy111\\Digital Signature Doctor.txt");
 //BufferedInputStream bis=new BufferedInputStream(br2);
  //BufferedReader br = new BufferedReader(new FileReader(br2));
  byte[] data2=new byte[br2.available()];
  System.out.println(Arrays.toString(data2));
  br2.read(data2);
  br2.close();

  FileInputStream datafis=new FileInputStream("C:\\Directroy111\\Digital Signature Doctor.txt");
  BufferedInputStream bufin=new BufferedInputStream(datafis);
  byte[] buffer=new byte[1024];

  int len;
  while(bufin.available()!=0){
          len=bufin.read(buffer);
          signature.update(buffer,0,len);
      };
      bufin.close();
      System.out.println("111111");
      boolean decryptmd2= signature.verify(data2);
      System.out.println("signature verifies: " + decryptmd2);
      if(decryptmd2==false){
       str = String.valueOf(decryptmd2);
      System.out.println("Message Digest-1(Decryption): "+str);
      }else{
          System.out.println("1111");
      }

          //**Verify*
      if(str.equals(hexString22.toString())){
        System.out.println("Digital Signature-1 was not modified"+"\n");
      }else{

        System.out.println("ERROR!!!  Digital Signature-1 was modified"+"\n");
      }

} 
