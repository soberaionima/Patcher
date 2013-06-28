package by.hzv.patcher.util

import org.apache.commons.lang3.Validate

class QueryExtractor {
    private delimeter = "/" //default delimeter
    private lineComments = ["//", "--"] //supported line comment
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    public QueryExtractor() {}

    public QueryExtractor(String delimeter) {
        this.delimeter = delimeter
    }


    public Collection<String> getQueries(String patch) {
        Collection<String> queryList = []
        StringBuilder queryBuilder = new StringBuilder()

        patch.eachLine{ line ->
            handleLine(line, queryBuilder, queryList)
        }

        if (!patch.empty && queryList.empty)
            throw new IllegalArgumentException("invalid patch text: commands should be seperated by ${delimeter}")

        return queryList
    }


    private void handleLine(String line, StringBuilder queryBuilder, Collection<String> queryList) {
        def trimmedLine = line.trim()

        if (lineIsComment(trimmedLine)) {
            //skip for now
        } else if (queryIsReady(trimmedLine)) {
            queryList.add(queryBuilder.toString())
            queryBuilder.setLength(0)
        } else if (!trimmedLine.empty) {
            queryBuilder.append(line)
            queryBuilder.append(LINE_SEPARATOR)
        }
    }

    private boolean queryIsReady(String trimmedLine) {
        return trimmedLine.equalsIgnoreCase(delimeter)
    }

    private boolean lineIsComment(String trimmedLine) {
        for (String lc : lineComments) {
            if (trimmedLine.startsWith(lc)) return true
        }
        return false
    }
}
