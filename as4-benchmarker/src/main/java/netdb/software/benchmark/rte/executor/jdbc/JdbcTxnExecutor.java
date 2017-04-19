package netdb.software.benchmark.rte.executor.jdbc;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;

import netdb.software.benchmark.TransactionType;
import netdb.software.benchmark.TxnResultSet;
import netdb.software.benchmark.remote.SutResultSet;
import netdb.software.benchmark.remote.vanilladb.VanillaDbResultSet;

public abstract class JdbcTxnExecutor {

	/**
	 * Prepare the parameters for further execution.
	 */
	protected abstract void prepareParams();

	protected abstract void executeSql();

	protected abstract SpResultSet createResultSet();

	protected abstract TransactionType getTrasactionType();

	public TxnResultSet execute() {
		TxnResultSet rs = new TxnResultSet();
		rs.setTxnType(getTrasactionType());

		// generate parameters
		prepareParams();

		// send txn request and start measure txn response time
		long txnRT = System.nanoTime();
		executeSql();
		SutResultSet result = new VanillaDbResultSet(createResultSet());

		// measure txn response time
		txnRT = System.nanoTime() - txnRT;

		// display output
//		System.out.println(getTrasactionType() + " " + result.outputMsg());
		
		rs.setTxnIsCommited(result.isCommitted());
		rs.setTxnResponseTimeNs(txnRT);
		rs.setTxnEndTime();

		return rs;
	}
}
