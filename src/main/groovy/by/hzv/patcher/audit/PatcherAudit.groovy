package by.hzv.patcher.audit

import groovy.io.FileType
import groovy.sql.Sql
import groovy.transform.TupleConstructor

import java.text.SimpleDateFormat

import org.apache.commons.lang3.Validate
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import by.hzv.patcher.audit.PatcherAudit;

import by.hzv.patcher.configuration.PatcherConfig
import by.hzv.patcher.configuration.SqlDbType
import by.hzv.patcher.sql.SqlFactory
import by.hzv.patcher.util.PatchNameParser
import by.hzv.patcher.vo.ExecutionResult

import static by.hzv.patcher.util.PatchNameParser.*

@TupleConstructor
class PatcherAudit {
	private static final Logger LOG = LoggerFactory.getLogger(PatcherAudit)

    PatcherConfig config

    String init() {
        String query =
			(config.sqlConnectionDetails.dbType == SqlDbType.ORACLE) ? getOracleInitQuery() : getMySqlInitQuery()
        Sql sql = getSql()
        sql.executeUpdate(query)
    }

    Collection<String> status(String key) {
        def res = []

        switch (key) {
            case 'ALL':
                getSql().rows(getAllEntriesQuery()).each { row ->
                    res.add(row.patchName)
                }
                break
            case 'LAST':
                def last = getSql().firstRow(getLastSuccessfulEntryQuery())
                if (last != null) {
                    res.add(last.patchName)
                }
                break
            case 'PENDING':
                checkForPendingPatches(res)
                break
            default:
                throw new IllegalArgumentException()
                break
        }

        return res
    }

    def checkForPendingPatches(Collection<String> res) {
        def lpnumber = getLastPatchNumber()

        def patchesDir = getPatchesDir()
        patchesDir.eachFile(FileType.FILES) { file ->
            def num = parse(file.name).number
            if (num > lpnumber) {
                res.add(file.name)
            }
        }

        res.sort {
            p1, p2 -> parse(p1).number <=> parse(p2).number
        }
    }

    public File getPatchesDir() {
        return new File(config.environmentProperties.patchesDir)
    }

    public Integer getLastPatchNumber() {
        def lastPatchName = status('LAST')
        return lastPatchName.empty ? 0 : parse(lastPatchName.first()).number
    }

    private void checkPatchNumber(Integer patchNumber) {
		LOG.info(">>> checkPatchNumber(${patchNumber})")
        Integer lastSuccessfulPatchNum = getLastPatchNumber()
		LOG.info("lastSuccessfulPatchNum = ${lastSuccessfulPatchNum}")
        if (lastSuccessfulPatchNum >= 0) {
            def expectedPatchNum = lastSuccessfulPatchNum + 1

			LOG.info("Validate.isTrue ${patchNumber} == ${expectedPatchNum}")
            Validate.isTrue patchNumber == expectedPatchNum,
"""
Last successfully applied patch number was ${lastSuccessfulPatchNum}.
Next patch number should be ${expectedPatchNum}
"""
        }
    }

    void log(ExecutionResult result, String duration) {
        logDb(result, duration)
        logFile(result)
    }

    private void logDb(ExecutionResult result, String duration) {
        Sql sql = getSql()

        def params = [
            generateNextId(sql),
            result.patchName,
            config.environmentProperties.computerName,
            result.success ? 'success' : 'error',
            result.text,
            config.environmentProperties.osUser,
            result.dbUser,
            duration,
            config.environmentProperties.envName
        ]
        sql.execute(getInsertQueryString(), params)
    }

    private String generateNextId(Sql sql) {
        def res = sql.firstRow('SELECT MAX(id) as maxId FROM VcPatchAudit')
        return (res?.maxId ?: 0) + 1
    }

    private void logFile(ExecutionResult result) {
        String timestamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())
        def logFile = new File(config.environmentProperties.logsDir, "${result.patchName}_log_${timestamp}.txt")

        logFile.withWriter {
            it.writeLine("Status: ${result.success ? 'Success' : 'Error'}\n${result.text}")
        }
    }

    private Sql getSql() {
        def cd = config.sqlConnectionDetails
        return SqlFactory.getSql(cd, cd.user, cd.password)
    }

    private String getOracleInitQuery() {
"""
CREATE TABLE VcPatchAudit(
  id NUMBER NOT NULL,
  patchName Varchar2(255 ) NOT NULL,
  envName Varchar2(20 ) NOT NULL,
  computerName Varchar2(255 ) NOT NULL,
  osUser Varchar2(255 ) NOT NULL,
  dbUser Varchar2(255 ) NOT NULL,
  creationTime TIMESTAMP NOT NULL,
  duration Varchar2(20 ) NOT NULL,
  status Varchar2(20 ) NOT NULL,
  scriptOutput CLOB NOT NULL,
  PRIMARY KEY (id)
)
"""
    }

    private String getMySqlInitQuery() {
"""
CREATE TABLE VcPatchAudit(
  id BIGINT NOT NULL,
  patchName Varchar(255 ) NOT NULL,
  envName Varchar(20 ) NOT NULL,
  computerName Varchar(255 ) NOT NULL,
  osUser Varchar(255 ) NOT NULL,
  dbUser Varchar(255 ) NOT NULL,
  creationTime TIMESTAMP NOT NULL,
  duration Varchar(20 ) NOT NULL,
  status Varchar(20 ) NOT NULL,
  scriptOutput LONGTEXT NOT NULL,
  PRIMARY KEY (id)
)
"""
    }

    private String getInsertQueryString () {
"""
 insert into VcPatchAudit(id, patchName, computerName, creationTime, status, scriptOutput, osUser, dbUser, duration, envName)
 values (?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)
"""
    }

    private String getAllEntriesQuery() {
        'select patchName from VcPatchAudit order by id DESC'
    }

    private String getLastSuccessfulEntryQuery() {
        "select patchName from VcPatchAudit where status='success' order by id DESC"
    }
}