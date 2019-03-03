import edu.cis.uab.shams.dsl.struct.*;
public class Sample1{
	public static void main(String[] args)
	{

LogStructure netlog = new LogStructure();
netlog.setName("netlog");
netlog.addField(FieldType.IP,"fromip",true,0,false);
netlog.addField(FieldType.TEXT,"userid",true,1,false);
//netlog.addField(FieldType.TIME,"time",false,Integer.MAX_VALUE,false);
//netlog.addField(FieldType.TIME,"time2",false,Integer.MAX_VALUE,false);
//netlog.addField(FieldType.TIME,"time3",false,Integer.MAX_VALUE,false);
//netlog.setEncryptionAlgorithm("RSA");
//netlog.setHashingAlgorithm("SHA_1");

//LogStructure patientlog = new LogStructure();
//patientlog.addField(FieldType.TIME,"logtime",false,-1,false);
//patientlog.addField(FieldType.TEXT,"user",true,0,false);
//patientlog.addField(FieldType.INT,"refid",true,1,false);
//patientlog.addField(FieldType.TEXT,"message",true,2,false);
//patientlog.setHashingAlgorithm("SHA_2");

FileWatcher netlogFileWatcher = new FileWatcher();
netlogFileWatcher.setLogStructure(netlog);
netlogFileWatcher.setFileName("networkfile.txt");
netlogFileWatcher.setPubicKeyFile("shams.pub");
netlogFileWatcher.setPrivateKeyFile("shams.pk");
netlogFileWatcher.setDelimiter(";");
netlogFileWatcher.execute();

//TableWatcher patientlogTableWatcher = new TableWatcher();
//patientlogTableWatcher.setLogStructure(patientlog);
//patientlogTableWatcher.setTable("Patient");
//patientlogTableWatcher.setAction("Edit");
//patientlogTableWatcher.setMaintainHistory(true);
//patientlogTableWatcher.setMethod("updatepatient");
//patientlogTableWatcher.setParams("alice","12");
//patientlogTableWatcher.execute();

	}
}
