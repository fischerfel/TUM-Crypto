data='The quick brown fox jumps over the lazy dog'
data2 = unicode2native(data, 'ASC-II');
K = java.security.MessageDigest.getInstance('MD5');
md5 = dec2hex(typecast(K.digest(data2), 'UINT8')).';
md5 = md5(:).'
