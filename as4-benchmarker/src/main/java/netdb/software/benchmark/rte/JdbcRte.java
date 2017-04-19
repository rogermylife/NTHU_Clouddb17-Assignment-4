package netdb.software.benchmark.rte;

import netdb.software.benchmark.TxnResultSet;
import netdb.software.benchmark.rte.executor.jdbc.JdbcTxnExecutor;
import netdb.software.benchmark.rte.executor.jdbc.MicrobenchmarkExecutor;

public class JdbcRte extends RemoteTerminalEmulator {
	
	public JdbcRte(Object... args) {
		super(args);
	}

	@Override
	public TxnResultSet executeTxnCycle() {
		JdbcTxnExecutor txnExecutor = new MicrobenchmarkExecutor();
		return txnExecutor.execute();
	}

}
