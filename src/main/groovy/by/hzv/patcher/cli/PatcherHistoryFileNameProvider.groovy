package by.hzv.patcher.cli;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * @author dkotsubo
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class PatcherHistoryFileNameProvider extends DefaultHistoryFileNameProvider {

    String getHistoryFileName() {
        return "patcherHistory.log"
    }

    @Override
    String name() {
        return "Patcher history file name provider"
    }
}
