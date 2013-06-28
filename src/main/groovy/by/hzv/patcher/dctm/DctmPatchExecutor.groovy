package by.hzv.patcher.dctm

import com.documentum.fc.client.DfQuery
import com.documentum.fc.client.IDfCollection
import com.documentum.fc.client.IDfQuery
import com.documentum.fc.client.IDfSession
import com.documentum.fc.common.DfException
import com.documentum.fc.common.IDfAttr
import by.hzv.patcher.configuration.DctmConnectionDetails
import by.hzv.patcher.vo.ExecutionResult
import by.hzv.patcher.vo.Patch

class DctmPatchExecutor {
    @Override
    static ExecutionResult execute(Patch patch, DctmConnectionDetails connectionDetails) {
        IDfSession session = getSession(connectionDetails)
        def out = new StringBuilder()

        try {
            session.beginTrans()
            out.append("Script will be running under ${connectionDetails.user} on ${connectionDetails.repositoryName})")

            IDfQuery dq = new DfQuery()
            for (String query : patch.queryList) {
                logExecution(query, out)
                dq.setDQL(query)
                IDfCollection col = dq.execute(session, IDfQuery.DF_EXEC_QUERY)
                logResult(col, out)
            }

            session.commitTrans()
            return new ExecutionResult(true, patch.patchName, out.toString(), connectionDetails.user)
        } catch (DfException ex) {
            out.append(ex.toString())
            session.abortTrans()

            return new ExecutionResult(false, patch.patchName, out.toString(), connectionDetails.user)
        } finally {
            if (session != null) {
                session.disconnect()
            }
        }
    }

    private static IDfSession getSession(DctmConnectionDetails connectionDetails) {
        DfcSessionFactory.getSession(connectionDetails)
    }

    private static void logExecution(String query, StringBuilder out) {
        def execOut = new StringBuilder()
        execOut.append("\n")
        execOut.append("executing:").append("\n")
        execOut.append("-------------------------------").append("\n")
        execOut.append(query)
        execOut.append("-------------------------------").append("\n")
        out.append(execOut)
    }

    private static void logResult(IDfCollection col, StringBuilder out) {
        try {
            boolean caption = false
            StringBuilder row = new StringBuilder()
            while (col.next()) {
                row.setLength(0)
                StringBuilder captionOut = caption ? null : new StringBuilder()
                for (int i = 0; i < col.getAttrCount(); i++) {
                    IDfAttr attr = col.getAttr(i)
                    if (captionOut != null) {
                        if (i != 0) {
                            captionOut.append(" |")
                        }
                        captionOut.append(attr.getName())
                    }
                    if (i != 0) {
                        row.append(" |")
                    }
                    String attrName = attr.getName()
                    switch (attr.getDataType()) {
                        case IDfAttr.DM_BOOLEAN:
                            row.append(col.getBoolean(attrName))
                            break
                        case IDfAttr.DM_DOUBLE:
                            row.append(col.getDouble(attrName))
                            break
                        case IDfAttr.DM_ID:
                            row.append(col.getId(attrName).toString())
                            break
                        case IDfAttr.DM_INTEGER:
                            row.append(col.getInt(attrName))
                            break
                        case IDfAttr.DM_TIME:
                            row.append(col.getTime(attrName).toString())
                            break
                    }
                }
                if (!caption) {
                    caption = true
                    out.append(captionOut.toString()).append("\n")
                }
                out.append(row.toString()).append("\n")
            }
            col.close()
        } catch (DfException ex) {
            out.append("\n${ex.toString()}")
        }
    }
}
