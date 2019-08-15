def sha256Hex = { input ->  
  java.security.MessageDigest.getInstance("SHA-256")   
    .digest(input.getBytes("UTF-8")).encodeHex().toString()  
}
log.info(sha256Hex(sampler.getArguments().getArgument(0).getValue()))
