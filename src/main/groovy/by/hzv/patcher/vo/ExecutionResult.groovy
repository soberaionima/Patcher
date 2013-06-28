package by.hzv.patcher.vo

import groovy.transform.Immutable


/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 18, 2012
 */
@Immutable
class ExecutionResult {
	Boolean success
	String patchName;
	String text
	String dbUser

	@Override
	String toString() {
		if (success) {
			return String.format("Patch <%s> has been executed successfully", patchName)
		} else {
			throw new PatchExecutionException(String.format("Execution of patch <%s> failed. Cause:\n ${text} ", patchName))
		}
	}
}
