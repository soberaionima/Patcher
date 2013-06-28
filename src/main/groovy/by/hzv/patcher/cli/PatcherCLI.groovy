package by.hzv.patcher.cli;

import org.apache.commons.lang3.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.core.CommandMarker
import org.springframework.shell.core.annotation.CliCommand
import org.springframework.shell.core.annotation.CliOption
import org.springframework.stereotype.Component

import by.hzv.patcher.Patcher

/**
 * @author dkotsubo
 *
 */
@Component
class PatcherCLI implements CommandMarker {

    @Autowired
    private Patcher patcher

    @CliCommand(value = "info", help = "Prints configuration details")
    String info() {
        return patcher.info()
    }

    @CliCommand(value = "status", help = "Prints last successfully applied patch")
    String status(@CliOption(key = ["key"],
							 specifiedDefaultValue="ALL",
							 unspecifiedDefaultValue="LAST",
                             help = "Prints status based on key value. Available options: 'LAST', 'ALL', 'PENDING'")
                             final String key) {

    	return patcher.status(key)
    }

    @CliCommand(value = "init", help = "Initializes audit table in DB")
    String init() {
        return patcher.init()
    }

    @CliCommand(value = "apply", help = "Apply all pending patches")
    String apply(@CliOption(key = ["patch"],
                            specifiedDefaultValue = "",
                            help = "Apply patch with specified name")
							final String patchName,
                 @CliOption(key = ["force"],
                            specifiedDefaultValue = "true",
                            unspecifiedDefaultValue = "false",
                            help = "Specifies if patch validation should be skipped. Makes sense only in conjuction with '--patch' key")
				            final boolean force) {

        if (patchName != null) {
			Validate.notEmpty(patchName, "Patch name should be provided")
        	return patcher.applyPatch(patchName, force).toString()
        } else {
        	return patcher.applyPendingPatches().toString()
        }
    }
}