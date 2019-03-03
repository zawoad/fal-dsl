package edu.cis.uab.shams.dsl.struct;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import edu.cis.uab.shams.dsl.dal.DBHandler;
import edu.cis.uab.shams.dsl.encryption.Base64;
import edu.cis.uab.shams.dsl.encryption.HandleKey;

/**
 * 
 * @author shams
 *
 */
public class TableWatcher extends LogAction {

	private String table;
	private String action;
	private boolean maintainHistory;
	private String method;
	//private String[] params;
	@Override
	
	public void execute() {
		
		try
		{
			LogStructure logStruct = getLogStructure();
			List<Field> fieldList = logStruct.getFieldList();
			Collections.sort(fieldList);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date(System.currentTimeMillis());
			String strDateNow = sdf.format(now);
			
			
			StringBuilder sb = new StringBuilder();
			
			//method definition
			sb.append("public void audit");
			sb.append(this.getTable());
			sb.append(this.getAction());
			sb.append('(');
			String prefix = "";
			for(Field field : fieldList)
			{
				if(field.isIndexBased())
				{
					sb.append(prefix);
					if(field.getMyType().equals(String.class))
					{
						sb.append("String ");
					}
					else if(field.getMyType().equals(Integer.class))
					{
						sb.append("int ");
					}
					else if(field.getMyType().equals(Double.class))
					{
						sb.append("double ");
					}
					sb.append(field.getFieldName());
					prefix = ", ";
				}
			}
			
			//method start
			sb.append(")\n{\n");
			sb.append("\ttry {\n");
			
			//auto time field
			sb.append("\t\tSimpleDateFormat sdf = new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\");\n");
			sb.append("\t\tDate now = new Date(System.currentTimeMillis());\n");
			for(Field field : fieldList)
			{
				if(!field.isIndexBased())
				{	
					sb.append("\t\tString "+field.getFieldName()+" = sdf.format(now);\n");
				}
			}
			
			//logchain code
			prefix = "";
			if(logStruct.isUseLogChain())
			{
				sb.append("\t\tString rowValue = ");
				for(Field field : fieldList)
				{
					sb.append(prefix);
					sb.append(field.getFieldName());
					prefix = " + ";
				}
				sb.append(";\n\t\tString currHashs = LogAction.getHashChain(\"");
				sb.append(logStruct.getName());
				sb.append("\",\"id\",rowValue,\"");
				sb.append(logStruct.getHashingAlgorithm());
				sb.append("\");\n");
			}
			
			//encryption code
			boolean usingPublic = true;
			if (logStruct.isUseEncryption()) {
				if(logStruct.getEncryptionAlgorithm().equals("AES"))
				{
					sb.append("\t\tString aesKey = HandleKey.readAESKey(\"");
					if (getPubicKeyFile() != null)
					{
						sb.append(getPubicKeyFile());
					}
					else if (getPrivateKeyFile() != null)
					{
						sb.append(getPrivateKeyFile());
					}
					else
					{
						throw new Exception("Algorithm is defined, but no encryption key is set");
					}
					//sb.append(getPubicKeyFile() == null ? getPrivateKeyFile() : getPubicKeyFile());
					sb.append("\");\n\n");
				}
				else
				{
					if (getPubicKeyFile() != null) {
						sb.append("\t\tPublicKey publicKey = HandleKey.readPublicKeyFromFile(\""+getPubicKeyFile()+"\");\n\n");
						
					} else if (getPrivateKeyFile() != null)
					{
						usingPublic = false;
						sb.append("\t\tPrivateKey privateKey = HandleKey.readPrivateKeyFromFile(\""+getPrivateKeyFile()+"\");\n\n");
					}
					else
					{
						throw new Exception("Algorithm is defined, but no encryption key is set");
					}
				}
				for(Field field : fieldList)
				{
					if(field.isEncrypted())
					{
						sb.append("\t\t");
						sb.append(field.getFieldName());
						if(logStruct.getEncryptionAlgorithm().equals("AES"))
						{
							sb.append(" = HandleKey.aesEncrypt("+field.getFieldName()+" +\"\", aesKey);\n");
						}
						else
						{
							if(usingPublic)
							{
								sb.append(" = Base64.encodeBytes(HandleKey.rsaEncrypt((");
								sb.append(field.getFieldName());
								sb.append(" +\"\").getBytes(), publicKey));\n");
							}
							else
							{
								sb.append(" = Base64.encodeBytes(HandleKey.rsaSignMessage((");
								sb.append(field.getFieldName());
								sb.append(" +\"\").getBytes(), privateKey));\n");
							}
						}
					}
				}
				
			}
			
			//generate query
			sb.append("\n\t\tString query = \"insert into ");
			sb.append(logStruct.getName());
			sb.append('(');
			for(Field field : fieldList)
			{
				sb.append(field.getFieldName());
				sb.append(',');
			}
			sb.append("tablename,");
			sb.append("actionname,");
			sb.append("methodname,");
			//include logchain column if needed
			sb.append(logStruct.isUseLogChain() ? "logchain,withhistory) values(": "withhistory) values(");
			for(Field field : fieldList)
			{
				if(field.getMyType().equals(String.class))
				{
					sb.append("'\" + ");
					sb.append(field.getFieldName());
					sb.append(" + \"',");
				}
				else
				{	
					sb.append("\" + ");
					sb.append(field.getFieldName());
					sb.append(" + \",");
				}
				
			}
			sb.append("'"+ this.getTable()+"',");
			sb.append("'"+ this.getAction()+"',");
			sb.append("'"+ this.getMethod()+"',");
			//include logchain value if needed
			if(logStruct.isUseLogChain())
			{
				sb.append("'\"+currHashs+\"',");
			}
			sb.append(this.isMaintainHistory()+")\";");
			
			sb.append("\n\t\tDBHandler dbHandler = new DBHandler();\n");
			
			sb.append("\t\tdbHandler.insertData(query);\n"
				+"\t}catch (Exception e) {\n"
				+"\t\te.printStackTrace();\n"
				+"\t}\n}\n");
			
			System.out.println(sb.toString());
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	
	public void methodtogenerate(String username, int refid, String message, String logtime)
	{
		try {
			String query = "insert into useraudit(username,refid,message,logtime,tablename,actionname,methodname,withhistory) values('" + username + "'," + refid + ",'" + message + "','" + logtime + "','Patient','Edit','updatepatient',true)";
			DBHandler dbHandler = new DBHandler();
			dbHandler.insertData(query);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isMaintainHistory() {
		return maintainHistory;
	}
	public void setMaintainHistory(boolean maintainHistory) {
		this.maintainHistory = maintainHistory;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

}
