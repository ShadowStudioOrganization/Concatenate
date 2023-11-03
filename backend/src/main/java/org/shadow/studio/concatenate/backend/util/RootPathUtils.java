package org.shadow.studio.concatenate.backend.util;

import java.io.File;

public class RootPathUtils {
    public static String getRootPath(File workingDirectory) {
        String workingDirectoryStr = workingDirectory.getPath();
        return workingDirectoryStr.split("versions")[0];
    }
}
