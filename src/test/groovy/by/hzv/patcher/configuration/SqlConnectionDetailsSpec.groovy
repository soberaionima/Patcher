package by.hzv.patcher.configuration;

import static org.junit.Assert.*

import com.typesafe.config.Config;

import spock.lang.Specification

class SqlConnectionDetailsSpec extends ConfigurationSpec {
	
	def "sql connection details should be properly created from config" () {
		given:
			Config config = configFixture()
		when:
			SqlConnectionDetails conf = SqlConnectionDetails.fromConfig(config)
		then:
				conf.user == 'localuser'
			and:
				conf.password == 'localpassword'
			and:
				conf.sysUser == 'sysuser'
			and:
				conf.sysPassword == 'syspassword'
			and: 
				conf.hostname == 'localhost'
			and: 
				conf.port == '9000'
			and:
				conf.dbId == 'dbsid'
			and: 
				conf.dbType == SqlDbType.MYSQL
	}
}
