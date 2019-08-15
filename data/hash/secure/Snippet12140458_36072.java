mddigest=java.security.MessageDigest.getInstance('MD5');
filestream=java.io.FileInputStream(java.io.File(filename));
digestream=java.security.DigestInputStream(filestream,mddigest);
md5hash=reshape(dec2hex(typecast(mddigest.digest,'uint8')),1,[])
