NSString *path    = @"/1.0/customer/abc@abc.com";
UInt64    expires = 1532405086;
NSString *key     = @"bzjG1Z5c5+4on14I0zJaJFAzTs1302Ya40A6JhI2uNY=";
// NSString *accesKeyId = @"432QQYKATPUBUNHESSPDTQU"; // Unused

// String canonical = "GET\n\n\n" + expires + "\n" + path;
NSString *canonicalString = [NSString stringWithFormat:@"GET\n\n\n%lld\n%@", expires, path];
NSData   *canonicalData   = [canonicalString dataUsingEncoding:NSUTF8StringEncoding];
NSLog(@"canonicalData: %@", canonicalData);

// byte[] keyBytes = Base64.decodeBase64(key.getBytes("US-ASCII"));
NSData *keyData = [[NSData alloc] initWithBase64EncodedString:key options:0];
NSLog(@"keyData: %@", keyData);

// Mac hmac = Mac.getInstance("HmacSHA256");
// hmac.init(new SecretKeySpec(keyBytes, "HmacSHA256"));
// byte[] signatureBytes = hmac.doFinal(canonical.getBytes("UTF-8"));
NSMutableData *signatureData = [NSMutableData dataWithLength:CC_SHA256_DIGEST_LENGTH];
CCHmac(kCCHmacAlgSHA256,
       keyData.bytes, keyData.length,
       canonicalData.bytes, canonicalData.length,
       signatureData.mutableBytes);
NSLog(@"signatureData: %@", signatureData);

// String signatureBase64 = new String(Base64.encodeBase64(signatureBytes), "US-ASCII");
NSString *signatureBase64 = [signatureData base64EncodedStringWithOptions:0];
NSLog(@"signatureBase64: %@", signatureBase64);

// String signature = URLEncoder.encode(signatureBase64, "UTF-8");
NSCharacterSet *base64Characterset = [[NSCharacterSet characterSetWithCharactersInString:@"+/="] invertedSet];
NSString *signature = [signatureBase64 stringByAddingPercentEncodingWithAllowedCharacters:base64Characterset];
NSLog(@"signature: %@", signature);
