package by.hzv.patcher.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.base.Joiner

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
class ConfigProvider {
	private static final Logger LOG = LoggerFactory.getLogger(ConfigProvider)
	private static final Joiner PATH_JOINER = Joiner.on('/')
	final PatcherConfig config

	ConfigProvider(String appDir) {
		config = prepareConfituration(appDir)
	}

	PatcherConfig prepareConfituration(String appDir) {
		def env = new Properties()
		LOG.info('loading patcher.properties')
		loadConfProperties(env, appDir)
		LOG.info("loading properties for env=${env['envName']}")
		loadEnvProperties(env, appDir)

		EnvironmentProperties environmentProperties = new EnvironmentProperties(
			envName: env['envName'],
			osUser: env['osUser'],
			patchesDir: env['patchesDir'],
			logsDir: env['logsDir'],
			computerName: getComputerName())

		SqlConnectionDetails sqlConnectionDetails = new SqlConnectionDetails(
			user: env['db_user'],
			password: env['db_password'],
			hostname: env['db_hostname'],
			port: env['db_port'],
			dbId: env['db_id'],
			sysUser: env['db_sys_user'],
			sysPassword: env['db_sys_password'],
			dctmUser: env['db_dctm_user'],
			dctmPassword: env['db_dctm_password'],
			dbType: SqlDbType.valueOf(env['db_type'].toUpperCase()))

		DctmConnectionDetails dctmConnectionDetails = new DctmConnectionDetails(
			user: env['dctm_user'],
			password: env['dctm_password'],
			repositoryName: env['dfc.globalregistry.repository'])

		DfcProperties dfcProperties = new DfcProperties(
			docbrokerHost: env['dfc.docbroker.host[0]'],
			docbrockerPort: env['dfc.docbroker.port[0]'],
			globalregistryRepository: env['dfc.globalregistry.repository'],
			globalregistryUsername: env['dfc.globalregistry.username'],
			globalregistryPassword: env['dfc.globalregistry.password'])

		def config = new PatcherConfig(environmentProperties, sqlConnectionDetails, dctmConnectionDetails, dfcProperties)
		LOG.info("Configuration loaded:\n${config}")
		return config
	}

	private void loadConfProperties(Properties env, String appDir) {
		def confPropsFile = new File(PATH_JOINER.join(appDir, "infrastructure", "conf", "patcher.properties"))
		confPropsFile.withReader { reader ->
			env.load(reader)
		}
	}

	private void loadEnvProperties(Properties env, String appDir) {
		try {
            def envNameOverride = System.getProperty('envName')
            if (envNameOverride != null) env.put('envName', envNameOverride)

			def p = new Properties()
			def envFolder = new File(PATH_JOINER.join(appDir, "infrastructure", "environments", env['envName']))
			def envPropertiesFile = new File(envFolder, "env.properties")
			assert envPropertiesFile.exists(),  "there are no env.properties in environments/${env['envName']}"
			envPropertiesFile.withReader { reader ->
				p.load(reader)
			}

			def dfcPropertiesFile = new File(envFolder, "dfc.properties")
			assert dfcPropertiesFile.exists(),  "there are no dfc.properties in environments/${env['envName']}"
			dfcPropertiesFile.withReader { reader ->
				p.load(reader)
			}

			env.putAll(p)
			env.put("osUser", System.getProperty("user.name"));
			env.put("appDir", appDir);
			env.put("logsDir", PATH_JOINER.join(appDir, 'infrastructure', 'logs'));
			env.put("patchesDir", PATH_JOINER.join(appDir, 'infrastructure', 'patches'));

			System.setProperty('dfc.properties.file', dfcPropertiesFile.absolutePath)

            def patchesDirOverride = System.getProperty('patchesDir')
            if (patchesDirOverride != null) env.put('patchesDir', patchesDirOverride)
		} catch (Exception e) {
			throw new Exception("Cannot load Patcher environment properties")
		}
	}

	private String getComputerName() {
		return "hostname".execute().with {
			waitFor()
			in.text.replace("\\s", ' ').trim()
		}
	}
}
