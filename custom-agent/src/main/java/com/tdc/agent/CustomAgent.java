package com.tdc.agent;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

public final class CustomAgent {

    private static final String ANNOTATION_NAME =
            "com.tdc.demo.annotation.CustomInstrumentLog";

    private CustomAgent() {
    }

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[CustomAgent] Java Agent loaded.");

        var annotatedMethod = isMethod()
                .and(isAnnotatedWith(named(ANNOTATION_NAME)))
                .and(not(isAbstract()))
                .and(not(isSynthetic()));

        new AgentBuilder.Default()
                .ignore(
                        nameStartsWith("java.")
                                .or(nameStartsWith("javax."))
                                .or(nameStartsWith("jakarta."))
                                .or(nameStartsWith("sun."))
                                .or(nameStartsWith("jdk."))
                                .or(nameStartsWith("org.springframework."))
                                .or(nameStartsWith("net.bytebuddy."))
                                .or(nameStartsWith("com.tdc.agent."))
                )
                .type(declaresMethod(annotatedMethod))
                .transform((builder,
                            typeDescription,
                            classLoader,
                            module,
                            protectionDomain) -> builder
                        .visit(
                                Advice.to(MethodLoggingAdvice.class)
                                        .on(annotatedMethod.and(not(returns(void.class))))
                        )
                        .visit(
                                Advice.to(VoidMethodLoggingAdvice.class)
                                        .on(annotatedMethod.and(returns(void.class)))
                        )
                )
                .installOn(instrumentation);

        System.out.println(
                "[CustomAgent] Instrumentation installed for methods annotated with @CustomInstrumentLog."
        );
    }
}
