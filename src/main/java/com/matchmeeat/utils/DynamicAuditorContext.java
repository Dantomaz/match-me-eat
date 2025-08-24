package com.matchmeeat.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;

/**
 * By default, currently logged-in user's username is used for entity auditing.</br>
 * This class allows to invoke a Spring bean method with 'SYSTEM' auditor instead.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DynamicAuditorContext {

    public static final String SYSTEM_AUDITOR = "SYSTEM";
    private static final ThreadLocal<String> currentAuditor = new ThreadLocal<>();

    public static <T> T runAsSystem(Callable<T> callable) {
        return runAs(SYSTEM_AUDITOR, callable);
    }

    private static <T> T runAs(String auditor, Callable<T> callable) {
        String previousAuditor = currentAuditor.get();
        currentAuditor.set(auditor);
        try {
            return callable.call();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (previousAuditor != null) {
                currentAuditor.set(previousAuditor);
            } else {
                currentAuditor.remove();
            }
        }
    }

    public static void runAsSystem(Runnable runnable) {
        runAs(SYSTEM_AUDITOR, runnable);
    }

    private static void runAs(String auditor, Runnable runnable) {
        String previousAuditor = currentAuditor.get();
        currentAuditor.set(auditor);
        try {
            runnable.run();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (previousAuditor != null) {
                currentAuditor.set(previousAuditor);
            } else {
                currentAuditor.remove();
            }
        }
    }

    private static void set(String auditor) {
        currentAuditor.set(auditor);
    }

    private static String get() {
        return currentAuditor.get();
    }

    private static void clear() {
        currentAuditor.remove();
    }
}
