package by.hzv.patcher.cli

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.springframework.shell.Bootstrap

/**
 *
 * @author Pavel Porubov
 */
class Main {
    private static final Pattern SYSTEM_PROPERTY_PATTERN = Pattern.compile('^-D(.+?)=(.+)$')

    static String[] processArgs(final String[] args, final Properties props) {
        ArrayList newArgs = new ArrayList()
        for (String arg : args) {
            Matcher am = SYSTEM_PROPERTY_PATTERN.matcher(arg)
            if (am.matches()) {
                props.put(am.group(1), am.group(2))
            } else {
                newArgs.add(arg)
            }
        }
        return newArgs.toArray(new String[0])
    }

    public static void main(final String[] args) {
        Bootstrap.main(processArgs(args, System.properties))
    }
}
