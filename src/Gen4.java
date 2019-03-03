import edu.cis.uab.shams.dsl.struct.*;
public class Gen4{
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

		TableWatcher userauditTableWatcher = new TableWatcher();
		userauditTableWatcher.setLogStructure(useraudit);
		userauditTableWatcher.setTable("Patient");
		userauditTableWatcher.setAction("Edit");
		userauditTableWatcher.setMaintainHistory(true);
		userauditTableWatcher.setMethod("updatepatient");
		userauditTableWatcher.setPrivateKeyFile("serveraes.key");

		userauditTableWatcher.execute();

	}
}
