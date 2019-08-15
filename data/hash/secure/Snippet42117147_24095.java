DigestInputStream dis = new DigestInputStream(is, MessageDigest.getInstance('MD5')) 
println "MD5 generated from Groovy: " + dis.getMessageDigest().digest().encodeHex().toString()
