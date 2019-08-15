StringBuilder firstImageRGBStr=new StringBuilder();
for (int i=0; i<32; i++)
 for (int j=0; j<32; j++)
   firstImageRGBStr.append(img.getRGB(i,j));


MessageDigest md = MessageDigest.getInstance("SHA");
byte[] firstImageDigest = md.digest(firstImageRGBStr.toString().getBytes());
