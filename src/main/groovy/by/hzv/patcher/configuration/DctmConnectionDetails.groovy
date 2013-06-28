package by.hzv.patcher.configuration

import com.typesafe.config.Config;

import groovy.transform.Immutable;

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
@Immutable
class DctmConnectionDetails {
	String user
	String password
	String repositoryName
	
	static def DctmConnectionDetails fromConfig(Config config) {
		Config dtcmConf = config.getConfig('dctm-connection-details')
		
		new DctmConnectionDetails(
			dtcmConf.getString('user'), 
			dtcmConf.getString('password'), 
			dtcmConf.getString('repository-name'))
	}

	@Override
	public String toString() {
"""
***********   Documentum connection details   *************
dctm_user=${user}
dctm_password=${password}
dfc.globalregistry.repository=${repositoryName}
***********************************************************
"""
	}
}
