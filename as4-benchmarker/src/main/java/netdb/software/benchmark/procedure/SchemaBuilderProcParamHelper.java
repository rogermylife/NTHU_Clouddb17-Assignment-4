package netdb.software.benchmark.procedure;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class SchemaBuilderProcParamHelper extends StoredProcedureParamHelper {

	private final String TPCC_TABLES_DDL[] = {
			"CREATE TABLE item ( i_id INT, i_im_id INT, i_name VARCHAR(24), "
					+ "i_price DOUBLE, i_data VARCHAR(50) )" };
	private final String TPCC_INDEXES_DDL[] = {
			"CREATE INDEX idx_items ON item (i_id)" };

	public String[] getTableSchemas() {
		return TPCC_TABLES_DDL;
	}

	public String[] getIndexSchemas() {
		return TPCC_INDEXES_DDL;
	}

	@Override
	public void prepareParameters(Object... pars) {
		// nothing to do
	}

	@Override
	public SpResultSet createResultSet() {
		// create schema
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		sch.addField("status", statusType);

		// create record
		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));

		// create result set
		return new SpResultSet(sch, rec);
	}

}
