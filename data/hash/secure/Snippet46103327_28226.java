md = java.security.MessageDigest.getInstance('MD5');
key_converter = @(tup) char(md.digest(tup));
