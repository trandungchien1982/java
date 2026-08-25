package com.tdc.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

public final class MethodLoggingAdvice {

    private MethodLoggingAdvice() {
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
    public static void onExit(
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] parameters,
            @Advice.Return(typing = Assigner.Typing.DYNAMIC) Object returnValue,
            @Advice.Thrown Throwable throwable) {

        if (!Boolean.getBoolean("custom.agent.enabled")) {
            return;
        }

        try {
            String methodName = method.getDeclaringClass().getSimpleName()
                    + "." + method.getName();

            String returnedValue = throwable == null
                    ? String.valueOf(returnValue)
                    : "<THROWN: " + throwable.getClass().getSimpleName()
                    + ": " + throwable.getMessage() + ">";

            System.out.printf(
                    "Function - OnMethodExit() - This is the custom agent in method: %s at time: %s, "
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
