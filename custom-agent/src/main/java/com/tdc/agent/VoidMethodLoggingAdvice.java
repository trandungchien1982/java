package com.tdc.agent;

import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

public final class VoidMethodLoggingAdvice {

    private VoidMethodLoggingAdvice() {
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
    public static void onExit(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] parameters,
            @Advice.Thrown Throwable throwable) {

        if (!Boolean.getBoolean("custom.agent.enabled")) {
            return;
        }

        try {
            String methodName = method.getDeclaringClass().getSimpleName()
                    + "." + method.getName();

            String returnedValue = throwable == null
                    ? "<void>"
                    : "<THROWN: " + throwable.getClass().getSimpleName()
                    + ": " + throwable.getMessage() + ">";

            System.out.printf(
                    "Method - OnMethodExit() - This is the custom agent in method: %s at time: %s, "
                            + "the parameters: %s, return value: %s%n",
                    methodName,
                    LocalDateTime.now(),
                    Arrays.deepToString(parameters),
                    returnedValue
            );
        } catch (Throwable ignored) {
            // Instrumentation must never break business execution.
        }
    }
}
