package org.shadow.studio.concatenate.backend.test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.util.UUIDUtils;

public class TestUUIDApi {
    @Test
    public void UUIDtest() {
        String username = "G_Breeze";
        Assertions.assertNotNull(UUIDUtils.getUUIDByUsername(username));
    }
}
