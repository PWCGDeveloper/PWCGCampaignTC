package pwcg.dev.deploy;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public class PWCGJarCopy extends DeployBase
{
    static public void copyJar () throws IOException
    {
        File jarFile = new File("build\\libs\\PWCGTC.jar");
        File targetJarFile = new File("\\PWCG\\Deploy\\PWCGTC.jar");
        if (jarFile.exists())
        {

            Files.copy(jarFile, targetJarFile);
        }
    }
}
