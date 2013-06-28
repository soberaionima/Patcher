package by.hzv.patcher.vo

import java.util.ArrayList;
import java.util.Collection;


/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 18, 2012
 */
class BulkExecutionResult {
	private final Collection<ExecutionResult> results = []
	private String desciption = "Success"

	void addExectionResult(ExecutionResult result) {
		results.add(result)

		if (!result.success) {
			desciption = result.toString()
		}
	}

	boolean isSuccessful() {
		return !containsFailedResult();
	}

	private boolean containsFailedResult() {
		for (ExecutionResult r : results) {
			if (!r.success) {
				return true;
			}
		}
		return false;
	}

	@Override
	String toString() {
		if (results.empty) {
			return 'There are no pending patches'
		}

		return desciption;
	}
}
