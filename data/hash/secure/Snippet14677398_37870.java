val md = MessageDigest.getInstance(hashInfo.algorithm)
val input = new FileInputStream("file")
iterateStream(input){ (data, length) => 
    md.update(data, 0, length)
}
md.digest
