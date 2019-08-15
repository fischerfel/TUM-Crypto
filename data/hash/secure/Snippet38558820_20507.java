byte[] rawBigInt = bigInt.toByteArray();

ByteBuffer buff = ByteBuffer.allocate(rawBigInt.length + 4);
buff.order(ByteOrder.BIG_ENDIAN);
buff.putInt(rawBigInt.length).put(rawBigInt);

System.out.print("Buffer contains: ");
System.out.println( DatatypeConverter.printHexBinary(buff.array()) );


MessageDigest hash = MessageDigest.getInstance("SHA-256");
hash.update(buff);
byte[] digest = hash.digest();

System.out.print("Digest contains: ");
System.out.println( DatatypeConverter.printHexBinary(digest) );
