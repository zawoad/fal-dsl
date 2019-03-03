import edu.cis.uab.shams.dsl.struct.*;
public class Patientapplog{
	public static void main(String[] args)
	{

LogStructure useraudit = new LogStructure();
useraudit.setName("useraudit");
useraudit.addField(FieldType.TIME,"logtime",false,Integer.MAX_VALUE,false);
useraudit.addField(FieldType.TEXT,"username",true,0,true);
useraudit.addField(FieldType.INT,"refid",true,1,false);
useraudit.addField(FieldType.TEXT,"message",true,2,true);
useraudit.setEncryptionAlgorithm("AES");
useraudit.setHashingAlgorithm("SHA_1");

TableWatcher userauditPatientTableWatcher = new TableWatcher();
userauditPatientTableWatcher.setLogStructure(useraudit);
userauditPatientTableWatcher.setTable("Patient");
userauditPatientTableWatcher.setAction("Edit");
userauditPatientTableWatcher.setMaintainHistory(true);
userauditPatientTableWatcher.setMethod("updatepatient");
userauditPatientTableWatcher.setPrivateKeyFile("serveraes.key");

userauditPatientTableWatcher.execute();

TableWatcher userauditTransactionTableWatcher = new TableWatcher();
userauditTransactionTableWatcher.setLogStructure(useraudit);
userauditTransactionTableWatcher.setTable("Transaction");
userauditTransactionTableWatcher.setAction("Add");
userauditTransactionTableWatcher.setMethod("addtransaction");
userauditTransactionTableWatcher.setPrivateKeyFile("serveraes.key");

userauditTransactionTableWatcher.execute();

	}
}
