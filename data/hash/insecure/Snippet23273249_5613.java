import javax.xml.bind.DatatypeConverter;

String hash = DatatypeConverter.printHexBinary( 
           MessageDigest.getInstance("MD5").digest("SOMESTRING".getBytes("UTF-8")));
