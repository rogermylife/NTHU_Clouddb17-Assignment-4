package netdb.software.benchmark.procedure.vanilladb;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import netdb.software.benchmark.TpccConstants;
import netdb.software.benchmark.util.DoublePlainPrinter;
import netdb.software.benchmark.util.RandomValueGenerator;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

/**
 * The test bed loader for VanillaDB. This loader will populate the specified
 * number of warehouses into single vanilla-core DB server.
 */
public class TestbedLoaderProc implements StoredProcedure {
	private static Logger logger = Logger.getLogger(TestbedLoaderProc.class
			.getName());
	public Transaction tx;
	public RandomValueGenerator rg;
	private boolean isCommitted;

	public TestbedLoaderProc() {
		rg = new RandomValueGenerator();
	}

	@Override
	public void prepare(Object... pars) {
		// do nothing
	}

	@Override
	public SpResultSet execute() {
		loadTestbed();

		Schema sch = new Schema();
		Type t = Type.VARCHAR(10);
		sch.addField("status", t);
		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, t));
		return new SpResultSet(sch, rec);
	}

	private void loadTestbed() {
		// turn off logging set value to speed up loading process
		RecoveryMgr.enableLogging(false);

		tx = VanillaDb.txMgr().newTransaction(Connection.TRANSACTION_SERIALIZABLE,
				false);
		isCommitted = true;
		try {
			generateItems();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			isCommitted = false;
		}
		tx.commit();

		RecoveryMgr.enableLogging(true);

		// create a checkpoint
		CheckpointTask cpt = new CheckpointTask();
		cpt.createCheckpoint();
	}

	private void generateItems() {
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating " + TpccConstants.NUM_ITEMS
					+ " items !");
		int iid, iimid;
		String iname, idata;
		double iprice;
		String sql;
		for (int i = 1; i <= TpccConstants.NUM_ITEMS; i++) {
			iid = i;
			iimid = rg.number(TpccConstants.MIN_IM, TpccConstants.MAX_IM);
			iname = rg.randomAString(TpccConstants.MIN_I_NAME,
					TpccConstants.MAX_I_NAME);
			iprice = rg.fixedDecimalNumber(TpccConstants.MONEY_DECIMALS,
					TpccConstants.MIN_PRICE, TpccConstants.MAX_PRICE);

			// I_DATA
			idata = rg.randomAString(TpccConstants.MIN_I_DATA,
					TpccConstants.MAX_I_DATA);
			if (Math.random() < 0.1)
				idata = fillOriginal(idata);

			sql = "INSERT INTO item(i_id, i_im_id, i_name, i_price, i_data) VALUES ("
					+ iid
					+ ", "
					+ iimid
					+ ", '"
					+ iname
					+ "', "
					+ DoublePlainPrinter.toPlainString(iprice)
					+ ", '"
					+ idata
					+ "' )";
			int result = VanillaDb.newPlanner().executeUpdate(sql, tx);
			if (result <= 0)
				throw new RuntimeException();
			
			if (i % 10000 == 0 && logger.isLoggable(Level.FINE))
				logger.info(i + " item records has been inserted !");
		}
	}
	
	private String fillOriginal(String data) {
		int originalLength = TpccConstants.ORIGINAL_STRING.length();
		int position = rg.number(0, data.length() - originalLength);
		String out = data.substring(0, position)
				+ TpccConstants.ORIGINAL_STRING
				+ data.substring(position + originalLength);
		return out;
	}
}