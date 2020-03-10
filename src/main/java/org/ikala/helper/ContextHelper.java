package org.ikala.helper;

import org.testng.ITestContext;

public class ContextHelper {
    private static ThreadLocal<ContextDataProtoTypeHolder> contextDataProtoTypeHolderThreadLocal = new ThreadLocal<>();
    private static ITestContext iTestContext;

    //=====setters=====
    public static void setContextDataProtoTypeHolder(ContextDataProtoTypeHolder contextDataProtoTypeHolder) {
        contextDataProtoTypeHolderThreadLocal.set(contextDataProtoTypeHolder);
    }

    public static void setiTestContext(ITestContext iTestContext) {
        ContextHelper.iTestContext = iTestContext;
    }

    //======getters======
    public static ContextDataProtoTypeHolder getContextDataProtoTypeHolder() {
        return contextDataProtoTypeHolderThreadLocal.get();
    }

    public static ITestContext getiTestContext() {
        return iTestContext;
    }

    //======others========
    public static synchronized void removeContextDataProtoTypeHolder() {
        contextDataProtoTypeHolderThreadLocal.remove();
    }
}
