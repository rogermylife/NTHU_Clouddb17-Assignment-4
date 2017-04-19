package netdb.software.benchmark.rte.txparamgen;

import java.util.LinkedList;
import java.util.List;

import netdb.software.benchmark.TpccConstants;
import netdb.software.benchmark.TransactionType;
import netdb.software.benchmark.util.BenchProperties;
import netdb.software.benchmark.util.RandomNonRepeatGenerator;
import netdb.software.benchmark.util.RandomValueGenerator;

public class MicrobenchmarkParamGen implements TxParamGenerator {

	private static final double WRITE_TX_RATE;
	private static final double OP_CONFLICT_RATE;

	private static final int HOT_COUNT = 1;
	private static final int COLD_COUNT = 9;
	private static final int READ_COUNT = HOT_COUNT + COLD_COUNT;

	private static final int HOT_DATA_SIZE;
	private static final int COLD_DATA_SIZE;

	static {
		OP_CONFLICT_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".OP_CONFLICT_RATE", 0.01);
		WRITE_TX_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".WRITE_TX_RATE", 0.0);

		HOT_DATA_SIZE = (int) (1.0 / OP_CONFLICT_RATE);
		COLD_DATA_SIZE = TpccConstants.NUM_ITEMS - HOT_DATA_SIZE;
	}

	@Override
	public TransactionType getTxnType() {

		return TransactionType.MICRO_BENCHMARK;
	}

	@Override
	public Object[] generateParameter() {
		RandomValueGenerator rvg = new RandomValueGenerator();
		LinkedList<Object> paramList = new LinkedList<Object>();

		// decide there is write or not
		boolean isWrite = (rvg.randomChooseFromDistribution(WRITE_TX_RATE, (1 - WRITE_TX_RATE)) == 0) ? true : false;

		// **********************
		// Start prepare params
		// **********************

		// set read count
		paramList.add(READ_COUNT);

		// randomly choose a hot data
		chooseHotData(paramList, HOT_COUNT);

		// randomly choose COLD_DATA_PER_TX data from cold dataset
		chooseColdData(paramList, COLD_COUNT);

		// write
		if (isWrite) {

			// set write count = read count
			paramList.add(READ_COUNT);

			// set the update value
			for (int i = 0; i < READ_COUNT; i++)
				paramList.add(rvg.nextDouble() * 100000);

		} else {
			// set write count to 0
			paramList.add(0);
		}

		return paramList.toArray();
	}

	private void chooseHotData(List<Object> paramList, int count) {
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(HOT_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int itemId = rg.next(); // 1 ~ size
			paramList.add(itemId);
		}

	}

	private void chooseColdData(List<Object> paramList, int count) {
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(COLD_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int itemId = HOT_DATA_SIZE + rg.next(); // {hot size} + 1 ~ {hot size} + {cold size}
			paramList.add(itemId);
		}
	}
}
