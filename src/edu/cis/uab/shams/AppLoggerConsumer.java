package edu.cis.uab.shams;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.cis.uab.shams.dsl.dal.DBHandler;
import edu.cis.uab.shams.dsl.encryption.HandleKey;
import edu.cis.uab.shams.dsl.struct.LogAction;

public class AppLoggerConsumer {

	public static void main(String[] args)
	{
		new AppLoggerConsumer();
	}
	public AppLoggerConsumer()
	{
		auditPatientEdit("bob", 13, "alice updated data 12 patient");
		
		auditTransactionAdd("Alice", 1, "alice added new Transaction 1");
	}
	
	
	public void auditPatientEdit(String username, int refid, String message)
	{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date(System.currentTimeMillis());
			String logtime = sdf.format(now);
			String rowValue = username + refid + message + logtime;
			String currHashs = LogAction.getHashChain("useraudit","id",rowValue,"SHA-1");
			String aesKey = HandleKey.readAESKey("serveraes.key");

			username = HandleKey.aesEncrypt(username +"", aesKey);
			message = HandleKey.aesEncrypt(message +"", aesKey);

			String query = "insert into useraudit(username,refid,message,logtime,tablename,actionname,methodname,logchain,withhistory) values('" + username + "'," + refid + ",'" + message + "','" + logtime + "','Patient','Edit','updatepatient','"+currHashs+"',true)";
			DBHandler dbHandler = new DBHandler();
			dbHandler.insertData(query);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void auditTransactionAdd(String username, int refid, String message)
	{
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date(System.currentTimeMillis());
			String logtime = sdf.format(now);
			String rowValue = username + refid + message + logtime;
			String currHashs = LogAction.getHashChain("useraudit","id",rowValue,"SHA-1");
			String aesKey = HandleKey.readAESKey("serveraes.key");

			username = HandleKey.aesEncrypt(username +"", aesKey);
			message = HandleKey.aesEncrypt(message +"", aesKey);

			String query = "insert into useraudit(username,refid,message,logtime,tablename,actionname,methodname,logchain,withhistory) values('" + username + "'," + refid + ",'" + message + "','" + logtime + "','Transaction','Add','addtransaction','"+currHashs+"',false)";
			DBHandler dbHandler = new DBHandler();
			dbHandler.insertData(query);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
