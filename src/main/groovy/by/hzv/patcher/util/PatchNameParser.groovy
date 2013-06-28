package by.hzv.patcher.util

import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.Validate

import by.hzv.patcher.vo.PatchType

/**
 * Checks the patch name correspondence to the patch name pattern
 *
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 *
 */
class PatchNameParser {
    // patch_<patch_number>_<patch_type>_<build_revision>_<patch_name>@<optional_derective>.<patch_extension>
    private static final PATCH_PATHNAME = /^(?:(.+)[\/\\])?patch_(\d+)_(DQL|SQLDCTM|SQLSYS|SQL)_(\d+)_([a-zA-Z0-9]+)(@PROD_ONLY)?\.(sql|dql)$/
    private static final int PATCH_PATH = 1
    private static final int PATCH_NUMBER = 2
    private static final int PATCH_TYPE = 3
    private static final int PATCH_REVISION = 4
    private static final int PATCH_NAME = 5
    private static final int PATCH_DIRECTIVE = 6
    private static final int PATCH_EXT = 7

    static class PatchNameParseResult {
        String path
        Integer number
        PatchType patchType
        String revision
        String name
        String directive
        String ext
    }

    static PatchNameParseResult parse(String patchFileName) {
        def matcher = (patchFileName =~ PATCH_PATHNAME)
		Validate.isTrue(matcher.matches(), 'patch name does not match naming convention')

		return new PatchNameParseResult(
			path: matcher[0][PATCH_PATH],
			number: parsePatchNumer(matcher[0][PATCH_NUMBER]),
			patchType: PatchType.valueOf(matcher[0][PATCH_TYPE]),
			revision: matcher[0][PATCH_REVISION],
			name: matcher[0][PATCH_NAME],
			directive: matcher[0][PATCH_DIRECTIVE],
			ext: matcher[0][PATCH_EXT]
        )
    }

    private static Integer parsePatchNumer(String patchNumber) {
        return Integer.valueOf(StringUtils.stripStart(patchNumber, "0"))
    }
}
