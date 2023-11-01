package org.shadow.studio.concatenate.backend.test;


import org.junit.jupiter.api.Test;
import org.shadow.studio.concatenate.backend.util.UUIDUtils;

public class test {
    @Test
    public void UUIDtest() {
        String username = "G_Breeze";
        System.out.println("username:" + username);
        System.out.println("uuid:" + UUIDUtils.getUUIDByUsername(username));
    }
}
