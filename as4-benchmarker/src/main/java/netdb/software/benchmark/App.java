package netdb.software.benchmark;

import java.util.logging.Level;
import java.util.logging.Logger;

import netdb.software.benchmark.util.BenchProperties;

public class App {
	private static Logger logger = Logger.getLogger(App.class.getName());
	
	private static int action;

	static {
		action = BenchProperties.getLoader().getPropertyAsInteger(
				App.class.getName() + ".ACTION", -1);
	}

	public static void main(String[] args) {
		Benchmarker b = new Benchmarker();
		
		switch (action) {
		case 0:
			if (logger.isLoggable(Level.INFO))
				logger.info("Action: Doing nothing");
			break;
		case 1:
			// Load testbed
			if (logger.isLoggable(Level.INFO))
				logger.info("Action: Loading testbed");
			b.load();
			break;
		case 2:
			// Run benchmarks
			if (logger.isLoggable(Level.INFO))
				logger.info("Action: Benchmarking");
			b.run();
			b.report();
			break;
		default:
			throw new IllegalArgumentException("Unsupport action id: " + action);
		}
		
		if (logger.isLoggable(Level.INFO))
			logger.info("Benchmarker finished.");
	}
}
