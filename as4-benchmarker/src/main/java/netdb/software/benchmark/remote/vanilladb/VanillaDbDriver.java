package netdb.software.benchmark.remote.vanilladb;

import java.sql.SQLException;

import org.vanilladb.core.remote.storedprocedure.SpDriver;

import netdb.software.benchmark.remote.SutConnection;
import netdb.software.benchmark.remote.SutDriver;
import netdb.software.benchmark.util.BenchProperties;

public class VanillaDbDriver implements SutDriver {

	private static final String SERVER_IP;

	static {
		SERVER_IP = BenchProperties.getLoader().getPropertyAsString(
				VanillaDbDriver.class.getName() + ".SERVER_IP", "127.0.0.1");
	}

	public SutConnection connectToSut(Object... args) throws SQLException {
		try {
			SpDriver driver = new SpDriver();
			return new VanillaDbConnection(driver.connect(SERVER_IP, null));
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}
}
