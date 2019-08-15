class MySpec extends Specification{
    def 'test'(){
        given: 'a user that has its password encoded by SpringSecurity'
            def user = new SecUser(username: 'blah', password: 'p').save(flush:true)
            MessageDigest md = MessageDigest.getInstance('MD5')
            md.update('p'.getBytes('UTF-8'))
        expect: 'the password should be encoded with MD5 algorithm'
            user.password == (new BASE64Encoder()).encode(md.digest())
    }
}
