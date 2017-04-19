package netdb.software.benchmark.procedure;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class MicrobenchmarkTxnParamHelper extends StoredProcedureParamHelper {
	
	// For reading
	private int readCount;
	private int[] itemIds;
	private String[] itemName;
	
	// For writing
	private int writeCount;
	private double[] itemPrices;
	
	public int getReadCount() {
		return readCount;
	}
	
	public int getWriteCount() {
		return writeCount;
	}

	public int getItemId(int idx){
		return itemIds[idx];
	}
	
	public  void setItemName(String s , int idx){
		itemName[idx] = s;
	}
	
	public double getItemPrices(int idx){
		return itemPrices[idx];
	}

	@Override
	public void prepareParameters(Object... pars) {
		int indexCnt = 0;

		readCount = (Integer) pars[indexCnt++];
		itemIds = new int[readCount];
		itemName = new String[readCount];
		for (int i = 0; i < readCount; i++)
			itemIds[i] = (Integer) pars[indexCnt++];

		writeCount = (Integer) pars[indexCnt++];
		itemPrices = new double[writeCount];
		for (int i = 0; i < writeCount; i++)
			itemPrices[i] = (Double) pars[indexCnt++];

		if (writeCount == 0)
			setReadOnly(true);
	}

	@Override
	public SpResultSet createResultSet() {

		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		Type itemNameType = Type.VARCHAR(24);
		sch.addField("status", statusType);
		for(int i = 0 ; i <  10 ; i ++)
			sch.addField("item_name_"+i, itemNameType);

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));
		for(int i = 0 ; i < 10 ; i++)
			rec.setVal("item_name_"+i, new VarcharConstant(itemName[i], itemNameType));
	
		
		return new SpResultSet(sch, rec);
	}
}
