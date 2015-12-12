package com.dukescript.test.selenium.webdriver;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public final class WebDriverFXRunner extends BlockJUnit4ClassRunner {
    public WebDriverFXRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        final Class<?> klass = method.getDeclaringClass();
        Executor executor;
        try {
            Field field = klass.getDeclaredField("driver");
            field.setAccessible(true);
            executor = (Executor) field.get(null);
        } catch (Exception ex) {
            throw new IllegalStateException("Expecting static driver field in " + klass.getName());
        }
        final CountDownLatch finished = new CountDownLatch(1);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    WebDriverFXRunner.super.runChild(method, notifier);
                } finally {
                    finished.countDown();
                }
            }
        });
        for (;;) {
            try {
                finished.await();
                return;
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    }

}
