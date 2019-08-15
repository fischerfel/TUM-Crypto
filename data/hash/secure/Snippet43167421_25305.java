  class TaskStartPart {
   public void calculHash() throws InterruptedException, IOException,  NoSuchAlgorithmException {
    try {   
     DigestInputStream digestInputStream=null ;
     MessageDigest messageDigest=MessageDigest.getInstance("SHA-512") ;
     digestInputStream=new DigestInputStream(new TaskPart(new   File("C:\\Users\\win7p\\Documents/t")),messageDigest) ;
    while(digestInputStream.read()>=0) ;
     for(byte b: messageDigest.digest()) {
      hexRes2 += String.format("%02x",b);
     } sb = new StringBuilder(hexRes2);  //StringBuilder which adjust the string to be parsed
     dateiSpeichern(0,0,sb.substring(hexRes2.length() -  128,hexRes2.length())); System.out.println(sb.substring(hexRes2.length() - 128,hexRes2.length()));
        digestInputStream.close();
      } catch (IOException ex ) {ex.printStackTrace();}       
     }
    }   

  class TaskPart extends InputStream {  
    private File mFile ;
    private List<File> mFiles ;
    private InputStream mInputStream ;

   public TaskPart(File file) throws FileNotFoundException {
    mFile=file ;
    if(file.isDirectory()) {
    mFiles=new ArrayList<File>(Arrays.asList(file.listFiles())) ;
    Collections.sort(mFiles) ;
    mInputStream=nextInputStream() ;
   } else {
     mFiles=new ArrayList<File>() ;
     mInputStream=new FileInputStream(file) ;
   }
  }

  @Override
  public int read() throws IOException {
   int result=mInputStream==null?-1:mInputStream.read() ;
   if(result<0 && (mInputStream=nextInputStream())!=null)
    return read() ;
    else return result ;
   }

   protected String getRelativePath(File file) {
   return   file.getAbsolutePath().substring(mFile.getAbsolutePath().length()) ;
   }

   protected InputStream nextInputStream() throws FileNotFoundException {
    if(!mFiles.isEmpty()) {
     File nextFile=mFiles.remove(0) ;
    return new SequenceInputStream(
    new ByteArrayInputStream(getRelativePath(nextFile).getBytes()),
    new TaskPart(nextFile)) ;
   }
   else return null ;
  }
 }

  private void dateiSpeichern(int i1, int i2, String hexR) throws  InterruptedException, IOException {
   try { 
    String tF = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").format(new   Date().getTime());
   try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\win7p\\Documents/hashLog.txt", true))) {
   writer.append(tF);
   writer.newLine();
   writer.append(dtf);
   writer.newLine();
   writer.append("Hash Value: ");
   writer.append(hexR); //without StringBuilder I would have much //strings added next to eachone line by line
 } //normal here is also a catch code.
