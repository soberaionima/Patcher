package by.hzv.patcher.configuration;

import static org.junit.Assert.*

import org.mockito.Matchers;

import spock.lang.Specification

import com.google.common.base.Strings
import com.typesafe.config.Config

class EnvironmentPropertiesSpec extends ConfigurationSpec {
	def "environment properties should be properly created from config" () {
		given:
			Config config = configFixture()
			TypesafeConfigLoader.initEnvironmentName(config)
		when:
			EnvironmentProperties props = EnvironmentProperties.fromConfig(config)
		then:
			props.envName == 'dev'
		and:
			props.osUser == System.getProperty("user.name")
		and:
			!(Strings.isNullOrEmpty(props.computerName))
		and:
			props.patchesDir.endsWith 'infrastucture/patches'
		and:
			props.logsDir.endsWith 'infrastucture/logs'		
	}
	
	def "system property should override env-name property specified in application.conf" () {
		given:
			Config config = configFixture()
			System.setProperty("patcher-env-name", "uat")
		when:
			EnvironmentProperties envProp = EnvironmentProperties.fromConfig(config)
		then:
			envProp.envName == 'uat'
			
		cleanup:
			System.getProperties().remove("patcher-env-name")
	}
}
