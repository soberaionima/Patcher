package by.hzv.patcher.configuration

import groovy.transform.PackageScope

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 14 θώνÿ 2013 γ.
 */
class TypesafeConfigLoader {
	static Config load(String appConfigFilePath) {
		File appConfigFile = new File(appConfigFilePath).getCanonicalFile()
		assert appConfigFile.exists(), "${appConfigFile.absolutePath} does not exist"
		
		Config defaultConfig = getDefaultConfig(appConfigFile)
		initEnvironmentName(defaultConfig)		
		Config patcherConfig = getEnvironmentSpecificConfig(defaultConfig, System.getProperty('patcher-env-name'))
	}
	
	@PackageScope static Config getDefaultConfig(File appConfFile) {		
		def appConfig = ConfigFactory.parseFile(appConfFile)
		def originalConfig = ConfigFactory.load(appConfig)
		originalConfig.checkValid(ConfigFactory.defaultReference(), 'patcher-config')
		
		originalConfig.getConfig("patcher-config")
	}
	
	/* enable testability here */
	@PackageScope static Config getEnvironmentSpecificConfig(defaultConfig, envName) {
		Config patcherConfig = defaultConfig
			.getConfig(envName)
			.withFallback(defaultConfig)
	}
	
	@PackageScope static void initEnvironmentName(Config config) {
		if (!System.getProperties().containsKey('patcher-env-name')) {
			System.setProperty('patcher-env-name', config.getString('env-name'))
		}	
	}
}
