// First 3 tests are fine
val test1 = hash(MessageDigest.getInstance("SHA-1"))("foo", true)
val test2 = hash(MessageDigest.getInstance("SHA-1"))("foo")
val test3 = SHA1("foo", true)
// not enough arguments for method apply: (v1: String, v2: Boolean)String in trait Function2. Unspecified value parameter v2.
val test4 = SHA1("foo") 
