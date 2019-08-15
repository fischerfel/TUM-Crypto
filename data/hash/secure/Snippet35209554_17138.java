data = unicode2native(data, 'UTF-8');
K = java.security.MessageDigest.getInstance('MD5');
md5 = reshape(dec2hex(typecast(K.digest(data), 'UINT8')), 1, 32);
