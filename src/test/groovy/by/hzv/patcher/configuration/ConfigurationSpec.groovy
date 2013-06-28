package by.hzv.patcher.configuration

import java.io.File;

import spock.lang.Specification;

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 17 θώνÿ 2013 γ.
 */
class ConfigurationSpec extends Specification {
	private static final String APP_HOME_KEY = "APP_HOME"
	
	def setupSpec() {
		if (!System.hasProperty(APP_HOME_KEY)) {
			System.setProperty(APP_HOME_KEY, "/some/dummy/path")
		}
	}
	
	def cleanupSpec() {
		System.getProperties().remove(APP_HOME_KEY)
	}
	
	/* helper methods */
	File getAppConfFileFixture() {
		String configContent =
		"""
			patcher-config {
				env-name = qa
			
				qa {
					sql-connection-details {
						dctm-user = qa-sqldctmuser
						dctm-password = qa-password			
					}
				}

				uat {
					
				}
			}
		"""
		
		File.createTempFile("application",".conf").with {
			deleteOnExit()
			write configContent
			absoluteFile
		}
	}
	
	Config configFixture() {
		ConfigFactory.load().getConfig("patcher-config")
	}
}
