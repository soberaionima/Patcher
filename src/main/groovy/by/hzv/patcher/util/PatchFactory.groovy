package by.hzv.patcher.util

import by.hzv.patcher.util.PatchNameParser.PatchNameParseResult
import by.hzv.patcher.vo.Patch
import by.hzv.patcher.vo.PatchType

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 18, 2012
 */
class PatchFactory {
    private static final QueryExtractor sqlQueryExtractor = new QueryExtractor('/')
    private static final QueryExtractor dctmQueryExtractor = new QueryExtractor('go')

    static Patch of(File patchFile) {
        String patchName = patchFile.name
        PatchNameParseResult parseResult = PatchNameParser.parse(patchName)
        PatchType patchType = parseResult.patchType

        def queryList
        switch (parseResult.patchType) {
            case PatchType.SQL:
            case PatchType.SQLSYS:
            case PatchType.SQLDCTM:
                queryList = sqlQueryExtractor.getQueries(patchFile.text)
                break
            case PatchType.DQL:
                queryList = dctmQueryExtractor.getQueries(patchFile.text)
                break
            default:
                throw new Exception("invalid patch type: ${patchType}")
        }

        Patch patch = new Patch(
                patchName: patchName,
                patchType: patchType,
                queryList: queryList)
    }
}
