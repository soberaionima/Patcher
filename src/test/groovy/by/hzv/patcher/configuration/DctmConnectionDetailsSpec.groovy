package by.hzv.patcher.configuration;

import static org.junit.Assert.*
import spock.lang.Specification

import com.typesafe.config.Config

class DctmConnectionDetailsSpec extends ConfigurationSpec {
	def "connection details should be properly created from config" () {
		given:			
			Config config = configFixture()			
		when:
			DctmConnectionDetails details = DctmConnectionDetails.fromConfig(config)
		then:
			details.user == 'dctmuser'
		and: 
			details.password == 'dctmpassword'
		and:
			details.repositoryName == 'localrepo'
			
	}
}
