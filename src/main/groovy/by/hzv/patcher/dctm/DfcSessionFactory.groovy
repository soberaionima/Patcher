package by.hzv.patcher.dctm

import com.documentum.fc.client.DfClient
import com.documentum.fc.client.IDfClient
import com.documentum.fc.client.IDfSession
import com.documentum.fc.common.DfLoginInfo
import com.documentum.fc.common.IDfLoginInfo
import by.hzv.patcher.configuration.DctmConnectionDetails

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
class DfcSessionFactory {
	static IDfSession getSession(DctmConnectionDetails connectionDetails) {
		IDfClient client = DfClient.getLocalClient()
		IDfLoginInfo loginInfo = new DfLoginInfo()
		loginInfo.setUser(connectionDetails.user)
		loginInfo.setPassword(connectionDetails.password)

		return client.newSession(connectionDetails.repositoryName, loginInfo)
	}
}
