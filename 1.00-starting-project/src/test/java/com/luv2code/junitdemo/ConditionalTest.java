package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionalTest {

    @Test
    @Disabled("Don't run this until JIRA #123 resolved")
    void basicTest(){

    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void basicTestWindowsOnly(){

    }

    @Test
    @EnabledOnOs(OS.MAC)
    void basicTestMacOnly(){

    }

    @Test
    @EnabledOnOs({OS.MAC, OS.WINDOWS})
    void basicTestMacAndWindows(){

    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void basicTestLinuxOnly(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_17)
    void basicTestForJava17Only(){

    }

    @Test
    @EnabledOnJre(JRE.JAVA_24)
    void basicTestForJava24Only(){

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_17, max=JRE.JAVA_24)
    void basicTestForJava17To24(){

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_17, max=JRE.JAVA_21)
    void basicTestForJava17To21(){

    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_17)
    void basicTestForMinJava17(){

    }

    @Test
    @EnabledForJreRange(max=JRE.JAVA_21)
    void basicTestForMaxJava21(){

    }

    @Test
    @EnabledIfEnvironmentVariable(named = "LUV2CODE_ENV", matches = "DEV")
    void basicTestForEnv(){

    }

    @Test
    @EnabledIfSystemProperty(named = "LUV2CODE_SYS_PROP", matches = "CI_CD_DEPLOY")
    void basicTestForSysProp(){

    }
}
