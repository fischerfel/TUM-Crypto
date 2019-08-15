//getting the original md
            String omd="";
            FileReader fr= new FileReader("D:\\Ns\\md.txt");
            BufferedReader br= new BufferedReader(fr);
            while((omd=br.readLine())!=null)
            {
                System.out.println("original md:"+omd);
            }

    //creating md of the file
    MessageDigest md= MessageDigest.getInstance("MD5");
    FileInputStream file =new FileInputStream("D:\\Ns\\message.txt");
    byte[] dataBytes= new byte[1024];
    int nread=0,nread1;
    while((nread=file.read(dataBytes))!=-1)
    {
        md.update(dataBytes,0,nread);

    }
    byte[] mdbytes=md.digest();
    StringBuffer sb= new StringBuffer();
    for(int i=0; i<mdbytes.length; i++)
    {
        sb.append(Integer.toString((mdbytes[i]& 0xff)+0x100, 16).substring(1));
    }
    String nmd=sb.toString();
    System.out.println("md  created:"+nmd);


    //comparing both
    if(nmd.equals(omd))
    {
        System.out.println("the file is not manipulated!!");
    }
    else{
        System.out.println("the file is manipulated!!");
    }
