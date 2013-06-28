package by.hzv.patcher.configuration

import java.io.File

import spock.lang.MockingApi;
import spock.lang.Specification

import com.typesafe.config.Config

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 14 θώνÿ 2013 γ.
 */
class TypesafeConfigLoaderSpec extends ConfigurationSpec {
			
	def setupSpec() {
		System.setProperty('config.trace', 'loads')
	}
	
	def "default config should be loaded properly and have default values" () {
		given:				
			File appConfFile = getAppConfFileFixture()
		when:
			Config config = TypesafeConfigLoader.getDefaultConfig(getAppConfFileFixture())			
		then:
		    config.getString('sql-connection-details.dctm-user') == 'sqldctmuser'
		and:
			config.getString('sql-connection-details.dctm-password') == 'password'
	}
	
	def "application config should override default values" () {
		given:			
			def config = TypesafeConfigLoader.getDefaultConfig(getAppConfFileFixture())	
		when: 
			Config appConfig = TypesafeConfigLoader.getEnvironmentSpecificConfig(config, 'qa')
		then:
			appConfig.getString('env-name') == 'qa'
		and: 
			appConfig.getString('sql-connection-details.dctm-user') == 'qa-sqldctmuser'
		and: 
			appConfig.getString('sql-connection-details.dctm-password') == 'qa-password'
	}
	
	def "load method should return properly merged config" () {
		given:
			String confFilePath = getAppConfFileFixture().absolutePath
		when:
			Config patcherConfig = TypesafeConfigLoader.load(confFilePath)
			println patcherConfig.root().render()
		then: 
			patcherConfig.getString("env-name") == 'qa'
		and:
			patcherConfig.getString('sql-connection-details.dctm-password') == 'qa-password'
		and:
			patcherConfig.getString('dfc-properties.dfc.docbroker.host') == 'dctm.wiley.ru'		
	}
}
