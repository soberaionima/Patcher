package by.hzv.patcher.configuration

import com.typesafe.config.Config;

import groovy.transform.Immutable;

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
@Immutable
class DfcProperties {
	String dataDir
	String securityKeystoreFile
	String docbrokerHost
	String docbrockerPort
	String globalregistryRepository
	String globalregistryUsername
	String globalregistryPassword
	String locale
	
	static DfcProperties fromConfig(Config config) {
		Config dfcProp = config.getConfig('dfc-properties')
		
		new DfcProperties(
			dfcProp.getString("dfc.data.dir"), 
			dfcProp.getString("dfc.security.keystore.file"), 
			dfcProp.getString("dfc.docbroker.host"), 
			dfcProp.getString("dfc.docbroker.port"), 
			dfcProp.getString("dfc.globalregistry.repository"), 
			dfcProp.getString("dfc.globalregistry.username"), 
			dfcProp.getString("dfc.globalregistry.password"), 
			dfcProp.getString("dfc.locale"))
			.init()	
	}
	
	DfcProperties init() {
		/* enable environment specific properties */
		File dfcPropertiesFile = writeTmpDfcPropFile()
		System.setProperty('dfc.properties.file', dfcPropertiesFile.absolutePath)
		this
	} 
	
	private File writeTmpDfcPropFile() {
		File.createTempFile("dfc",".properties").with {
			deleteOnExit()
			write this.toProperties()
			absoluteFile
		}
	}

	@Override
	public String toString() {
"""
******************** dfc.properties ***********************
${this.toProperties()}
***********************************************************
"""
	}
	
	String toProperties() {
"""
dfc.data.dir=${dataDir}
dfc.security.keystore.file=${securityKeystoreFile}
dfc.docbroker.host=${docbrokerHost}
dfc.docbroker.port=${docbrockerPort}
dfc.globalregistry.repository=${globalregistryRepository}
dfc.globalregistry.username=${globalregistryUsername}
dfc.globalregistry.password=${globalregistryPassword}
dfc.locale=${locale}
"""
	}
}
