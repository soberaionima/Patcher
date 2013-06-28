package by.hzv.patcher.configuration

import groovy.transform.Immutable
import groovy.transform.PackageScope;

import com.typesafe.config.Config

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
@Immutable
class EnvironmentProperties {
	String envName
	String osUser
	String computerName
	String patchesDir
	String logsDir
	
	
	@PackageScope static EnvironmentProperties fromConfig(Config config) {
		new EnvironmentProperties(
			System.getProperty('patcher-env-name'),
			System.getProperty("user.name"),
			computerName(),			
			config.getString('patches-dir'), 
			config.getString('logs-dir'))
	}
	
	@PackageScope static String computerName() {
		return "hostname".execute().with {
			waitFor()
			in.text.replace("\\s", ' ').trim()
		}
	}
		

	@Override
	public String toString() {
"""
****************** Configuration Info ********************
Environment name = ${envName}
OS user = ${osUser}
Patches folder = ${patchesDir}
Logs folder = ${logsDir}
***********************************************************
"""
	}
}
