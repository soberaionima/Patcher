package by.hzv.patcher.cli

import spock.lang.Specification

/**
 * @author <a href="mailto:dkotsubo@wiley.com">Dmitry Kotsubo</a>
 * @since 17 θώνÿ 2013 γ.
 */
class MainSpec extends Specification {
	
	def "Main class should properly propaget -D* args to System properties" () {
		when:
			String[] actualArgs = Main.processArgs(args, System.properties)
		then:		
			(actualArgs as List).equals(expectedArgs)
			for (Map.Entry<String, String> pe : propagatedSysProp.entrySet) {
				pe.value == System.getProperty(pe.key)
			}
			
		where:
			args << [['info', '-DenvName=local', '-DpatchesDir=etc\test patches 4 MySQL'] as String[]] 
			expectedArgs << [['info']]
			propagatedSysProp <<  [[envName:'local', patchesDir:'etc\test patches 4 MySQL']]				
	}
}
