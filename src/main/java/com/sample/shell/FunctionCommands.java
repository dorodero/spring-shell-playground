package com.sample.shell;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.command.CommandExceptionResolver;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.shell.command.CommandRegistration;

@Configuration
public class FunctionCommands {
    @Bean
    CommandRegistration commandRegistration() {
        return CommandRegistration.builder()
                .command("mycommand")
                .withTarget().function(ctx -> {
                    String arg1 = ctx.getOptionValue("arg1");
                    if (arg1 == null) {
                        throw new NullPointerException();
                    }
                    return String.format("hi, arg1 value is '%s'", arg1);
                })
                .and()
                .withOption()
                .longNames("arg1")
                .and()
                // ResolverをBean定義しておけば個別に設定する必要はない
//                .withErrorHandling()
//                .resolver(new CustomExceptionResolver())
//                .and()
                .build();
    }

    class CustomExceptionResolver implements CommandExceptionResolver {
        @Override
        public CommandHandlingResult resolve(Exception e) {
            if (e instanceof RuntimeException) {
                return CommandHandlingResult.of("Hi, handled exception\n", 42);
            }
            return null;
        }
    }

    //Bean定義しておくと全てのコマンドでハンドリングされる
    @Bean
    CustomExceptionResolver customExceptionResolver() {
        return new CustomExceptionResolver();
    }
}
