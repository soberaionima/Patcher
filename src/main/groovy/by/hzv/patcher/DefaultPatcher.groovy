package by.hzv.patcher

import groovy.transform.TupleConstructor

import org.apache.commons.lang3.Validate
import org.apache.commons.lang3.time.DurationFormatUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import by.hzv.patcher.audit.PatcherAudit;

import com.google.common.base.Joiner
import by.hzv.patcher.configuration.PatcherConfig
import by.hzv.patcher.dctm.DctmPatchExecutor
import by.hzv.patcher.sql.SqlPatchExecutor
import by.hzv.patcher.util.PatchFactory
import by.hzv.patcher.util.PatchNameParser
import by.hzv.patcher.vo.BulkExecutionResult
import by.hzv.patcher.vo.ExecutionResult
import by.hzv.patcher.vo.PatchType

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 17, 2012
 */
@TupleConstructor
class DefaultPatcher implements Patcher {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPatcher)
    private static final Joiner COLLECTION_ELEMENTS_JOINER = Joiner.on('\n')

    PatcherConfig config
    PatcherAudit patcherAudit

    @Override
    String info() {
        LOG.info('>>> info()')
        return config.toString()
    }

    @Override
    String init() {
        LOG.info('>>> init()')
        patcherAudit.init()
        "Patcher has been configured for ${config.environmentProperties.envName} environment"
    }

    @Override
    String status(String key) {
        try {
            LOG.info(">>> status(${key})")
            def sb = new StringBuilder()
            return COLLECTION_ELEMENTS_JOINER.join(patcherAudit.status(key))
        } catch (Exception e) {
            LOG.error(e.getMessage(), e)
        }
    }

    @Override
    ExecutionResult applyPatch(String patchName, boolean force) {
        def patchesDir = config.environmentProperties.patchesDir
        def patchFile = new File(patchesDir, patchName)

        Validate.isTrue(patchFile.exists(),
                "Patch with name ${patchName} does not exists in ${patchesDir}")

        def patchNameParseResult  = PatchNameParser.parse(patchName)
        def dbType = config.sqlConnectionDetails.dbType
        assert dbType.eligiblePatchTypes.contains(patchNameParseResult.patchType),
                "Patch of type ${patchNameParseResult.patchType} is not eligible for ${dbType} DB type"
        if (!force) {
            patcherAudit.checkPatchNumber(patchNameParseResult.number)
        }

        long start = System.currentTimeMillis()
        def patch = PatchFactory.of(patchFile)

        def result
        if (patch.patchType == PatchType.DQL) {
            result = DctmPatchExecutor.execute(patch, config.dctmConnectionDetails)
        } else {
            result = SqlPatchExecutor.execute(patch, config.sqlConnectionDetails)
        }

        def duration = DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - start)
        patcherAudit.log(result, duration)
        return result
    }

    @Override
    BulkExecutionResult applyPendingPatches() {
		LOG.info('>>> applyPendingPatches()')
        Collection<String> pendingPatches = patcherAudit.status('PENDING')
		LOG.info("Pending patches: ${pendingPatches}")
        BulkExecutionResult bulkResult = new BulkExecutionResult();

        for(String patchName : pendingPatches) {
            ExecutionResult result = applyPatch(patchName, false)
            bulkResult.addExectionResult(result)

            if (!result.success) {
                return bulkResult
            }
        }
        return bulkResult
    }
}