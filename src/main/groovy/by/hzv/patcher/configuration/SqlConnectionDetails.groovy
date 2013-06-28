package by.hzv.patcher.configuration

import com.typesafe.config.Config;

import groovy.transform.Immutable;

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 23.02.2013
 */
@Immutable
class SqlConnectionDetails {
	String user
	String password
	String sysUser
	String sysPassword
	String dctmUser
	String dctmPassword
	String hostname
	String port
	String dbId	
	SqlDbType dbType
	
	static def SqlConnectionDetails fromConfig(Config config) {
		Config sqlConf = config.getConfig('sql-connection-details')
		
		new SqlConnectionDetails(
			sqlConf.getString('user'), 
			sqlConf.getString('password'),
			sqlConf.getString('sys-user'),
			sqlConf.getString('sys-password'),
			sqlConf.getString('dctm-user'),
			sqlConf.getString('dctm-password'),
			sqlConf.getString('hostname'),
			sqlConf.getString('port'),
			sqlConf.getString('db-id'),
			SqlDbType.valueOf(sqlConf.getString('db-type').toUpperCase()))
	}

	@Override
	public String toString() {
"""
********************  SQL connection details  *****************
user=${user}
password=${password}
hostname=${hostname}
port=${port}
db_id=${dbId}
db_type=${dbType}
********************  SQL SYS credentials  ********************
sys_user=${sysUser}
sys_password=${sysPassword}
********************  SQL DCTM credentials  *******************
dctm_user=${dctmUser}
dctm_password=${dctmPassword}
***************************************************************
"""
	}
}
