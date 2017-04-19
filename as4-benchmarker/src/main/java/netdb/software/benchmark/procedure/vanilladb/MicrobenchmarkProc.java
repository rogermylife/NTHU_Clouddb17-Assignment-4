package netdb.software.benchmark.procedure.vanilladb;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.concurrency.LockAbortException;

import netdb.software.benchmark.procedure.MicrobenchmarkTxnParamHelper;

public class MicrobenchmarkProc implements StoredProcedure {
	private static Logger logger = Logger.getLogger(MicrobenchmarkProc.class.getName());
	
	private MicrobenchmarkTxnParamHelper paramHelper = new MicrobenchmarkTxnParamHelper();
	private Transaction tx;

	@Override
	public void prepare(Object... pars) {
		paramHelper.prepareParameters(pars);
	}

	@Override
	public SpResultSet execute() {
		tx = VanillaDb.txMgr().newTransaction(Connection.TRANSACTION_SERIALIZABLE,
				false);
		try {
			executeSql();
			tx.commit();
		} catch (LockAbortException lbe) {
			tx.rollback();
			paramHelper.setCommitted(false);
			if (logger.isLoggable(Level.WARNING))
				logger.warning(lbe.getMessage());
		} catch (Exception e) {
			tx.rollback();
			paramHelper.setCommitted(false);
			e.printStackTrace();
		}
		return paramHelper.createResultSet();
	}

	protected void executeSql() {
		
		for (int i = 0; i < paramHelper.getReadCount(); i++) {
			String name = "";

			String sql = "SELECT i_name FROM item WHERE i_id = "
					+ paramHelper.getItemId(i);
			Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
			Scan s = p.open();
			s.beforeFirst();
			if (s.next()) {
				name = (String) s.getVal("i_name").asJavaVal();
			} else
				throw new RuntimeException();

			paramHelper.setItemName(name, i);

			s.close();
		}
		
		for (int idx = 0; idx < paramHelper.getWriteCount(); idx++) {
			String sql = "UPDATE item SET i_price = "
					+ paramHelper.getItemPrices(idx) +  " WHERE i_id ="
					+ paramHelper.getItemId(idx);
			VanillaDb.newPlanner().executeUpdate(sql, tx);
		}
	}
}
