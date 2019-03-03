import edu.cis.uab.shams.dsl.struct.*;
public class Snort{
	public static void main(String[] args)
	{

LogStructure snortlog = new LogStructure();
snortlog.setName("snortlog");
snortlog.addField(FieldType.IP,"fromip",true,1,true);
snortlog.addField(FieldType.IP,"toip",true,3,true);
snortlog.addField(FieldType.TEXT,"logtime",true,0,false);
snortlog.setEncryptionAlgorithm("RSA");
snortlog.setHashingAlgorithm("SHA_256");

FileWatcher snortlogFileWatcher = new FileWatcher();
snortlogFileWatcher.setLogStructure(snortlog);
snortlogFileWatcher.setFileName("snortnetwork.log");
snortlogFileWatcher.setPubicKeyFile("lawpublic.key");
snortlogFileWatcher.setDelimiter(" ");
snortlogFileWatcher.execute();

	}
}
