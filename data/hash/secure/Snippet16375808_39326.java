BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
int sizeOfBlock = 1024;
int sizeOfHash = 256;

MessageDigest md;
md = MessageDigest.getInstance("SHA-256");

byte[] block = new byte[sizeOfBlock];

List <byte []> blockList = new ArrayList <byte []>();

int tmp = 0;
while ((tmp = bis.read(block)) > 0) {
    System.out.println(tmp);
    blockList.add(block);           
}       

for (int j = blockList.size()-1; j > 0;){
    System.out.println(blockList.get(j).length); // for the first iteration it shouldnt be 1024 if the file size is not a multiple of 1024
    md.update(blockList.get(j--));
    byte[] hash = md.digest();
    byte[] appendBlock = new byte[blockList.get(j).length + hash.length];

    System.arraycopy(blockList.get(j), 0, appendBlock, 0, blockList.get(j).length);
    System.arraycopy(md.digest(), 0, appendBlock, blockList.get(j).length, hash.length);
    blockList.set(j, appendBlock);      
}

System.out.println(blockList.get(0).length);
md.update(blockList.get(0));
byte[] hash = md.digest();

String result = bytesToHex(hash); // converting function from byte to hex
System.out.println(result);
