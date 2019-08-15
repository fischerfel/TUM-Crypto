public class CustomQueryBuilder implements Callable {
    @Override
    public Object onCall(MuleEventContext eventContext) throws Exception {
        JdbcConnector c = (JdbcConnector) eventContext.getMuleContext().getRegistry().lookupConnector("JDBC_connector");

        StringBuilder query = new StringBuilder();
        String queryBase = "select * from another_table where first_table_fk in (";
        query.append(queryBase);

        int numIndices = ((ArrayList<Integer>)eventContext.getMessage().getInvocationProperty("indices")).size();
        ArrayList<String> indices = new ArrayList<String>();
        for(int i=0; i<numIndices; i++) {
                indices.add("#[flowVars.indices[" + i + "]");
        }
        query.append(StringUtils.join(indices, ", "));
        query.append(")");

        String finalQuery = query.toString();

        MessageDigest md = MessageDigest.getInstance("MD5");
        String queryDigest = String.format("%1$032X",new BigInteger(1, md.digest(finalQuery.getBytes("UTF-8"))));

        if (!c.getQueries().containsKey(queryDigest)) {
                c.getQueries().put(queryDigest, finalQuery);
        }

        eventContext.getMessage().setInvocationProperty("generatedQueryKey", queryDigest);

        return eventContext.getMessage();
    }
}
