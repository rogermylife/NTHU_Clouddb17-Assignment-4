package netdb.software.benchmark.rte.tpcc;

import netdb.software.benchmark.TxnResultSet;
import netdb.software.benchmark.rte.RemoteTerminalEmulator;
import netdb.software.benchmark.rte.executor.StoredProcedureExecutor;
import netdb.software.benchmark.rte.executor.TransactionExecutor;
import netdb.software.benchmark.rte.txparamgen.MicrobenchmarkParamGen;

public class StoredProcedureRte extends RemoteTerminalEmulator {

	public StoredProcedureRte(Object[] connArgs) {
		super(connArgs);
	}

	@Override
	protected TxnResultSet executeTxnCycle() {
		TransactionExecutor tx = new StoredProcedureExecutor(new MicrobenchmarkParamGen());
		return tx.execute(conn);
	}

}
