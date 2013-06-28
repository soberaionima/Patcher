package by.hzv.patcher.util

import java.security.CodeSource;

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 17, 2012
 */
class AppDirResolver {
	//TODO Spring shell explicitly set APP_DIR as system variable, so it is possible to use it instead
    static String resolve() {
        CodeSource codeSource = AppDirResolver.class.getProtectionDomain().getCodeSource()
        File jarFile = new File(codeSource.getLocation().toURI().getPath());
        return jarFile.getParentFile()                   // lib dir
            .getParentFile().getAbsolutePath()           // APP dir
    }
}
