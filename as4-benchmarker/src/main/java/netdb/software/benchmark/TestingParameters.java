package netdb.software.benchmark;

import netdb.software.benchmark.util.BenchProperties;

public class TestingParameters {

	public static enum ConnectionMode {
		JDBC, STORED_PROCEDURES
	};

	public static final long BENCHMARK_INTERVAL;
	public static final long WARM_UP_INTERVAL;
	public static final int NUM_RTES;

	public static final ConnectionMode CONNECTION_MODE = ConnectionMode.STORED_PROCEDURES;

	static {
		WARM_UP_INTERVAL = BenchProperties.getLoader()
				.getPropertyAsLong(TestingParameters.class.getName() + ".WARM_UP_INTERVAL", 60000);

		BENCHMARK_INTERVAL = BenchProperties.getLoader()
				.getPropertyAsLong(TestingParameters.class.getName() + ".BENCHMARK_INTERVAL", 60000);

		NUM_RTES = BenchProperties.getLoader().getPropertyAsInteger(TestingParameters.class.getName() + ".NUM_RTES", 1);
	}
}