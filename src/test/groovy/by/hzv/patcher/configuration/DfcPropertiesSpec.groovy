package by.hzv.patcher.configuration;

import static org.junit.Assert.*;

import org.junit.Test;

import com.typesafe.config.Config;

import spock.lang.Specification;

class DfcPropertiesSpec extends ConfigurationSpec {

	def "dfc properties should be properly created from config" () {
		given:
			Config config = configFixture()
		when:
			DfcProperties conf = DfcProperties.fromConfig(config)
		then:
				conf.dataDir == "${System.getProperty('java.io.tmpdir')}/dfcDataDir_dctm.wiley.ru_1487"
			and:
				conf.securityKeystoreFile == "${conf.dataDir}/dfc.keystore"
			and:
				conf.docbrokerHost == 'dctm.wiley.ru'
			and:
				conf.docbrockerPort == '1487'
			and:
				conf.globalregistryRepository == 'jqa_dev_korolev'
			and:
				conf.globalregistryUsername == 'dm_bof_registry'
			and:
				conf.globalregistryPassword == 'dmbofpw'
			and:
				conf.locale == 'en'			
	}
	
	def "dfc properties should init environment specific dfc.properties" () {
		given:
			Config config = configFixture()
		when:
			DfcProperties conf = DfcProperties.fromConfig(config)
		then:
				System.getProperties().containsKey('dfc.properties.file')
			and:
				new File(System.getProperty('dfc.properties.file')).exists()
	}
}
