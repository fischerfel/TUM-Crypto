 def digest(algorithm: String = "SHA-256"): Flow[ByteString, ByteString, NotUsed] = {
Flow[ByteString].fold(MessageDigest.getInstance(algorithm)){
  case (digest: MessageDigest, bytes:ByteString) => {digest.update(bytes.asByteBuffer); digest}}
  .map {case md: MessageDigest => ByteString(md.digest())}
