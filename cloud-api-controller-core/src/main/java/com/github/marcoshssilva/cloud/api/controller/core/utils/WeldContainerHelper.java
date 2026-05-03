package com.github.marcoshssilva.cloud.api.controller.core.utils;

import org.jboss.weld.environment.se.WeldContainer;

public class WeldContainerHelper {
    private WeldContainerHelper() {}

    private static WeldContainer container = null;

    static void setContainer(WeldContainer container) {
        WeldContainerHelper.container = container;
    }

    public static WeldContainer getContainer() {
        return container;
    }
}
