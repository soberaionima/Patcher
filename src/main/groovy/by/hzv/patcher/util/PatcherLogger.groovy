package by.hzv.patcher.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PatcherLogger {
    private static final Logger LOG = LoggerFactory.getLogger(PatcherLogger)

    public void logException(final Exception ex) {
        LOG.error(ex.message, ex)
    }
}
