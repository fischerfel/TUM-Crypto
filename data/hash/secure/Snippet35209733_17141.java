s = unicode2native('The quick brown fox jumps over the lazy dog','UTF-8');
K = java.security.MessageDigest.getInstance('MD5');
out = dec2hex(typecast(K.digest(s),'uint8'))

9E
10
7D
9D
37
2B
B6
82
6B
D8
1D
35
42
A4
19
D6
