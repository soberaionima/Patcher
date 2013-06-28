package by.hzv.patcher.sql

import groovy.sql.Sql

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import by.hzv.patcher.configuration.SqlConnectionDetails
import by.hzv.patcher.vo.ExecutionResult
import by.hzv.patcher.vo.Patch
import by.hzv.patcher.vo.PatchType


/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
class SqlPatchExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(SqlPatchExecutor)

    @Override
    static ExecutionResult execute(Patch patch, SqlConnectionDetails connectionDetails) {
        def user
        def password

        switch (patch.patchType) {
            case PatchType.SQL:
                user = connectionDetails.user
                password = connectionDetails.password
                break
            case PatchType.SQLSYS:
                user = connectionDetails.sysUser + " as sysdba"
                password = connectionDetails.sysPassword
                break
            case PatchType.SQLDCTM:
                user = connectionDetails.dctmUser
                password = connectionDetails.dctmPassword
                break
        }

        Sql sql = SqlFactory.getSql(connectionDetails, user, password)
        def out = new StringBuilder()
        try {
            out.append("Script will be running under ${user} on ${connectionDetails.hostname}\n")

            patch.queryList.each {
                def query = patch.patchType == PatchType.SQL ? it.replace("&ORADCTM.", connectionDetails.dctmUser) : it
                logExecution(query, out)
                int count = sql.executeUpdate(query)
                logResult(count, out)
            }
            return new ExecutionResult(Boolean.TRUE, patch.patchName, out.toString(), user)
        } catch (Exception ex) {
            out.append(ex.toString())
            return new ExecutionResult(Boolean.FALSE, patch.patchName, out.toString(), user)
        }
    }

    private static void logExecution(String query, StringBuilder out) {
        def execOut = new StringBuilder()
        execOut.append("\n")
        execOut.append("executing:").append("\n")
        execOut.append("-------------------------------").append("\n")
        execOut.append(query)
        execOut.append("-------------------------------")
        execOut.append("\n")
        out.append(execOut)
    }

    private static void logResult(int count, StringBuilder out) {
        def resOut = new StringBuilder()
        resOut.append("rows [updated/deleted/inserted]").append("\n")
        resOut.append(count).append("\n")
        out.append(resOut)
    }
}
