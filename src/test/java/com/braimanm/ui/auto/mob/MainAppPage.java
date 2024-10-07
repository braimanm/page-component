package com.braimanm.ui.auto.mob;

import com.braimanm.ui.auto.pagecomponent.PageObject;
import com.braimanm.ui.auto.pagefactory.FindByTemplate;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings({"NewClassNamingConvention", "unused"})
@XStreamAlias("main-app")
public class MainAppPage extends PageObject {

    @FindByTemplate("scroller")
    private MobPage mobPage;

    public MobPage getMobPage() {
        return mobPage;
    }
}
