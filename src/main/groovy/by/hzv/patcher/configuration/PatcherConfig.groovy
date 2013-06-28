package by.hzv.patcher.configuration

import com.typesafe.config.Config;

import groovy.transform.Immutable

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since Dec 17, 2012
 */
@Immutable
class PatcherConfig {	
    EnvironmentProperties environmentProperties
    SqlConnectionDetails sqlConnectionDetails
    DctmConnectionDetails dctmConnectionDetails
    DfcProperties dfcProperties
	
	static PatcherConfig fromConfig(Config config) {
		new PatcherConfig(
			EnvironmentProperties.fromConfig(config),
			SqlConnectionDetails.fromConfig(config),
			DctmConnectionDetails.fromConfig(config),
			DfcProperties.fromConfig(config))
	}

    @Override
    String toString() {
        return '' + environmentProperties + sqlConnectionDetails + dctmConnectionDetails + dfcProperties
    }
}
