package by.hzv.patcher.configuration

import by.hzv.patcher.vo.PatchType

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
enum SqlDbType {
    ORACLE(EnumSet.allOf(PatchType)),
    MYSQL([
        PatchType.SQL,
        PatchType.SQLSYS
    ]);

	Collection<PatchType> eligiblePatchTypes
	SqlDbType(Collection<PatchType> availablePatchTypes) {
		this.eligiblePatchTypes = availablePatchTypes
	}
}
