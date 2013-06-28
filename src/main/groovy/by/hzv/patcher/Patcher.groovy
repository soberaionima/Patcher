package by.hzv.patcher

import java.util.Map;

import by.hzv.patcher.vo.BulkExecutionResult
import by.hzv.patcher.vo.ExecutionResult

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 17, 2012
 */
interface Patcher {
    String info()
    String status(String key)
    String init()
    ExecutionResult applyPatch(String patchName, boolean force)
    BulkExecutionResult applyPendingPatches()
}
