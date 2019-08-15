 md = java.security.MessageDigest.getInstance('MD5');
 md5 = md.digest(uint8('my_secret_password'))'
 % gives: 126  -28   22  -43   39  -94  -48   71  117   28    2  109 -126  -37  -66  -17

 md = java.security.MessageDigest.getInstance('SHA1');
 md5 = md.digest(uint8('my_secret_password'))'
 % gives: 51  -91  -61   39    0   56  -19  -61  112  -10    9  -71 -111  117  117  -71   52   46   50 -122
