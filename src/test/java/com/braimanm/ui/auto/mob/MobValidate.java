package com.braimanm.ui.auto.mob;

import com.braimanm.ui.auto.context.PageComponentContext;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;

@SuppressWarnings("NewClassNamingConvention")
public class MobValidate {

    @BeforeTest
    public void setup() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName("emulator-5554");
        options.setApp(System.getProperty("user.dir") + "/src/test/java/com/braimanm/ui/auto/mob/bitbar-sample-app.apk");
        options.setNewCommandTimeout(Duration.ofSeconds(30));
        options.setFullReset(true);
        PageComponentContext.initContext(() -> new AndroidDriver(options));
    }

    @Test
    public void test1() {
        MainAppPage page = new MainAppPage().fromResource("mob/mob-valid.xml");
        page.initPage(PageComponentContext.getContext());
        MobPage mob = page.getMobPage();
        mob.populate();
        mob.submit();
        mob.validate();
    }

    @AfterTest
    public void clean() {
        PageComponentContext.removeContext();
    }

}
