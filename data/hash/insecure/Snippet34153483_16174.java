@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageDigest.class)
public class WithMD5Calculator{

    @Test
    public void shouldHandleNSAEx(){
        PowerMockito.mockStatic(MessageDigest.class);
        Mockito.when(MessageDigest.getInstance("MD5")).thenThrow(new NoSuchAlgorithmException("Throwed"));

        WithMD5Calculator sut = new WithMD5Calculator(){};
        ExceptionAssert.assertThat(()-> sut.getMd5())
           .shouldThrow(ServiceException.class);
        // some more checks
    }
}
