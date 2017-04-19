package netdb.software.benchmark.rte.executor.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;

import netdb.software.benchmark.TransactionType;
import netdb.software.benchmark.rte.txparamgen.TxParamGenerator;
import netdb.software.benchmark.rte.txparamgen.MicrobenchmarkParamGen;
import netdb.software.benchmark.util.JdbcService;

public class MicrobenchmarkExecutor extends JdbcTxnExecutor {
	
	private String[] itemName = new String[10];
	private int[] itemIds = new int[10];
	private double[] itemPrices = new double[10];
	private boolean isCommitted;
	Connection conn;
	
	@Override
	protected void prepareParams() {
		
		TxParamGenerator pg = new MicrobenchmarkParamGen();
		Object[] param = pg.generateParameter();
		
		for (int idx = 0; idx < 10; idx++)
			itemIds[idx] = (Integer) param[idx];
		
		for (int idx = 0; idx < 10; idx++)
			itemPrices[idx] = (Double) param[10+idx];
	}

	@Override
	protected void executeSql() {
		
		conn = JdbcService.connect();
		try {

			conn.setAutoCommit(false);
			
			Statement stm = JdbcService.createStatement(conn);
			
			ResultSet rs = null;
			for (int i = 0; i < 10; i++) {
				String sql = "SELECT i_name FROM item WHERE i_id = "
						+ itemIds[i];
				rs = JdbcService.executeQuery(sql, stm);
				rs.beforeFirst();
				if (rs.next()) {
					itemName[i] = rs.getString("i_name");
				} else
					throw new RuntimeException();
				rs.close();
				
			}
				
			for (int idx = 0; idx < 10; idx++) {
				String sql = "UPDATE item SET i_price = "
						+ itemPrices[idx] +  " WHERE i_id ="
						+ itemIds[idx];
				stm.executeUpdate(sql);
			}
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JdbcService.disconnect(conn);
		}
		
	}

	@Override
	protected SpResultSet createResultSet() {
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		Type itemNameType = Type.VARCHAR(24);
		sch.addField("status", statusType);
		for (int i = 0; i < 10; i++)
			sch.addField("item_name_" + i, itemNameType);

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));
		for (int i = 0; i < 10; i++)
			rec.setVal("item_name_" + i, new VarcharConstant(itemName[i],
					itemNameType));
		

		return new SpResultSet(sch, rec);
	}

	@Override
	protected TransactionType getTrasactionType() {
		return TransactionType.MICRO_BENCHMARK;
	}

}
