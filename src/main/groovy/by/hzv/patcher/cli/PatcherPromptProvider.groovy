package by.hzv.patcher.cli;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author dkotsubo
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class PatcherPromptProvider extends DefaultPromptProvider {

    @Override
    String getPrompt() {
        return "patcher>"
    }


    @Override
    String name() {
        return "Patcher Prompt Provider"
    }
}
