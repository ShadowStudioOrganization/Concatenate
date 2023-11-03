package org.shadow.studio.concatenate.backend.test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.util.ExecutorUtils;
import org.shadow.studio.concatenate.backend.util.RootPathUtils;
import org.shadow.studio.concatenate.backend.util.UUIDUtils;

import java.io.IOException;

public class TestUUIDApi {
    @Test
    public void UUIDtest() {
        String username = "G_Breeze";
        Assertions.assertNotNull(UUIDUtils.getUUIDByUsername(username));
    }
    @Test
    public void test() throws InterruptedException, IOException {
        String[] urls = new String[]{
                "https://download-cdn.jetbrains.com/idea/ideaIU-2023.2.4.win.zip",
                "https://www.jb51.net/article/165676.htm"
        };
        ExecutorUtils.threadPollDownload(urls);
    }
}
