<build>
  <plugins>
    <plugin>
      <groupId>org.codehaus.gmavenplus</groupId>
      <artifactId>gmavenplus-plugin</artifactId>
      <version>1.5</version>
      <executions>
        <execution>
          <id>set-property</id>
          <phase>process-resources</phase>
          <goals>
            <goal>execute</goal>
          </goals>
          <configuration>
            <scripts>
              <script>
                import java.nio.file.Path
                import java.nio.file.FileSystems
                import java.nio.file.Files
                import java.io.BufferedInputStream
                import java.security.MessageDigest
                import javax.xml.bind.annotation.adapters.HexBinaryAdapter

                Path pathA = FileSystems.getDefault().getPath('src/main/resources/A.target')
                Path pathB = FileSystems.getDefault().getPath('src/main/resources/B.target')

                byte[] configsBytes = new byte[Files.size(pathA) + Files.size(pathB)]

                InputStream inA = new BufferedInputStream(Files.newInputStream(pathA))
                inA.read(configsBytes)
                inA.close()

                InputStream inB = new BufferedInputStream(Files.newInputStream(pathB))
                inB.read(configsBytes, (int)Files.size(pathA), (int)Files.size(pathB))
                inB.close()

                MessageDigest md5 = MessageDigest.getInstance('MD5')
                // from http://stackoverflow.com/a/12514417/1744774
                String digest = new HexBinaryAdapter().marshal(md5.digest(configsBytes))
                log.info(String.format('Setting property configs.checksum to %s', digest))
                project.properties.setProperty('configs.checksum', digest)

                // InvocationTargetException: No such property: configs
                //log.info(String.format('Property configs.checksum=%s', "${configs.checksum}"))
              </script>
            </scripts>
          </configuration>
        </execution>

        <execution>
          <id>use-property</id>
          <phase>process-resources</phase>
          <goals>
            <goal>execute</goal>
          </goals>
          <configuration>
            <scripts>
              <script>
                // ...
                log.info(String.format('Property configs.checksum=%s', "${configs.checksum}"))
                // ...
              </script>
            </scripts>
          </configuration>
        </execution>
      </executions>

      <dependencies>
        <dependency>
          <groupId>org.codehaus.groovy</groupId>
          <artifactId>groovy-all</artifactId>
          <version>2.4.3</version>
          <scope>runtime</scope>
        </dependency>
      </dependencies>
    </plugin>
  </plugins>
</build>
