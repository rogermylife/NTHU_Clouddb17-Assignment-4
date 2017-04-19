package netdb.software.benchmark.server;

import netdb.software.benchmark.TestingParameters;

public class StartUp {
	public static String dirName;

	public static void main(String[] args) throws Exception {
		dirName = args[0];

		if (TestingParameters.CONNECTION_MODE == TestingParameters.ConnectionMode.JDBC)
			new VanillaDbJdbcStartUp().startup(args);
		else
			new VanillaDbSpStartUp().startup(args);
	}
}