package netdb.software.benchmark;

import netdb.software.benchmark.util.BenchProperties;

/** Holds TPC-C constants. */

public class TpccConstants {
	
	// Scaling parameters
	public static final int NUM_ITEMS;
	
	static {
		NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".NUM_ITEMS", 100000);
	}
	
	// Item constants
	public static final int MIN_IM = 1;
	public static final int MAX_IM = 10000;
	public static final double MIN_PRICE = 1.00;
	public static final double MAX_PRICE = 100.00;
	public static final int MIN_I_NAME = 14;
	public static final int MAX_I_NAME = 24;
	public static final int MIN_I_DATA = 26;
	public static final int MAX_I_DATA = 50;
	public static final int MONEY_DECIMALS = 2;
	
	// Indicates "brand" items and stock in i_data and s_data.
	public static final String ORIGINAL_STRING = "ORIGINAL";
	public static final byte[] ORIGINAL_BYTES = ORIGINAL_STRING.getBytes();
}
