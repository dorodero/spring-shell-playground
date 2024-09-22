package com.sample.shell;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.springframework.shell.command.CommandHandlingResult;
import org.springframework.shell.command.annotation.ExceptionResolver;
import org.springframework.shell.component.PathInput;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.context.InteractionMode;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@ShellComponent()
public class ComponentCommands extends AbstractShellComponent {
    @ShellMethod(key = "component path input", value = "Path input", group = "Components", interactionMode = InteractionMode.INTERACTIVE)
    public String pathInput() {
        PathInput component = new PathInput(getTerminal(), "Enter value");
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());
        PathInput.PathInputContext context = component.run(PathInput.PathInputContext.empty());
        if (context.getResultValue() == null) {
            throw new RuntimeException("test");
        }

        return "Got value " + context.getResultValue();
    }

    // ShellComponent内にExceptionResolverのメソッドを用意していると
    // Bean定義しているものより優先される
    @ExceptionResolver
    CommandHandlingResult errorHandler(RuntimeException e) {
        return CommandHandlingResult.of("Wow, handled custom exception\n", 42);
    }

    // コンポーネントのカスタマイズ
    @ShellMethod(key = "component stringcustom", value = "String input", group = "Components")
    public String stringInputCustom(boolean mask) {
        StringInput component = new StringInput(getTerminal(), "Enter value", "myvalue",
                new StringInputCustomRenderer());
        component.setResourceLoader(getResourceLoader());
        component.setTemplateExecutor(getTemplateExecutor());

        if (mask) {
            component.setMaskCharacter('*');
        }
        StringInput.StringInputContext context = component.run(StringInput.StringInputContext.empty());

        String result = context.getResultValue();
        if (mask) {
            result = context.getMaskedResultValue();
        }
        return "Got value " + result;
    }

    class StringInputCustomRenderer implements Function<StringInput.StringInputContext, List<AttributedString>> {
        @Override
        public List<AttributedString> apply(StringInput.StringInputContext context) {
            AttributedStringBuilder builder = new AttributedStringBuilder();
            builder.append(context.getName());
            builder.append(" ");
            if (context.getResultValue() != null) {
                builder.append(context.getResultValue());
            }
            else  {
                String input = context.getInput();
                if (StringUtils.hasText(input)) {
                    builder.append(input);
                }
                else {
                    builder.append("[Default " + context.getDefaultValue() + "]");
                }
            }
            return Arrays.asList(builder.toAttributedString());
        }
    }
}
