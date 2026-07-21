# Local Nexus

The root Compose project runs Nexus Repository at `http://localhost:8081` with persistent data in the `nexus_data` volume.

## Publish

From the repository root, run:

```powershell
.\platform\nexus\Publish.ps1
```

On a fresh Nexus installation, the script reads the generated `admin` password, accepts the [Sonatype Nexus Repository Community Edition EULA](https://links.sonatype.com/products/nxrm/ce-eula), enables anonymous read access, and allows release redeployment for quick local iteration. After changing that password manually, pass it explicitly:

```powershell
.\platform\nexus\Publish.ps1 -Username admin -Password '<password>'
```

The Security Platform parent, auto-configuration, and starter are published together because the starter depends on the other two artifacts.

## Consume

Add the local releases repository to a microservice POM:

```xml
<repositories>
    <repository>
        <id>nexus-releases</id>
        <url>http://localhost:8081/repository/maven-releases/</url>
    </repository>
</repositories>
```

Then add the starter:

```xml
<dependency>
    <groupId>com.ecommerce.platform</groupId>
    <artifactId>oauth2-resource-server-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

Use `-Dnexus.url=http://<host>:8081` when publishing Nexus from another machine.
