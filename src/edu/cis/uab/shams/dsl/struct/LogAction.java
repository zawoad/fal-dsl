package edu.cis.uab.shams.dsl.struct;

import edu.cis.uab.shams.dsl.dal.DBHandler;
import edu.cis.uab.shams.dsl.encryption.HandleKey;

/**
 * 
 * @author shams
 *
 */
public abstract class LogAction {
	private LogStructure logStructure;
	private String pubicKeyFile;
	private String privateKeyFile;
	
	public LogAction()
	{
		
	}

	public LogStructure getLogStructure() {
		return logStructure;
	}

	public void setLogStructure(LogStructure logStructure) {
		this.logStructure = logStructure;
	}

	public String getPubicKeyFile() {
		return pubicKeyFile;
	}

	public void setPubicKeyFile(String pubicKeyFile) {
		this.pubicKeyFile = pubicKeyFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}

	public void setPrivateKeyFile(String privateKeyFile) {
		this.privateKeyFile = privateKeyFile;
	}
	
	public static String getHashChain(String tableName, String field, String rowValue, String hashAlgorithm)
	{
		String q = "select logchain from "+tableName+" where "+field+" = (select max("+field+") from "+tableName+");";
		DBHandler dbHandler = new DBHandler();
		try {
			
			String currentHash = dbHandler.getSingleData(q,"logchain");
			//System.out.println("ch:"+ currentHash);
			return HandleKey.getHash(currentHash+rowValue, hashAlgorithm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public abstract void execute();
}
