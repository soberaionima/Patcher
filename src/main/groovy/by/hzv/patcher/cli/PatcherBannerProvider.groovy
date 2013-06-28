package by.hzv.patcher.cli;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * @author dkotsubo
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PatcherBannerProvider extends DefaultBannerProvider implements CommandMarker {

    @CliCommand(value = ["version"], help = "Displays current CLI version")
    public String getBanner() {
"""
=======================================
*                                     *
*              Patcher                *
*                                     *
=======================================
Version: ${this.getVersion()}"""
    }

    String getVersion() {
        return "2.0.0.RC"
    }

    String getWelcomeMessage() {
        return "Welcome to Patcher CLI"
    }

    @Override
    String name() {
        return "Patcher"
    }
}