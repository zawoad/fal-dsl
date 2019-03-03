package edu.cis.uab.shams.dsl.struct;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import edu.cis.uab.shams.dsl.dal.DBHandler;
import edu.cis.uab.shams.dsl.encryption.Base64;
import edu.cis.uab.shams.dsl.encryption.HandleKey;

/**
 * 
 * @author shams
 *
 */
public class CopyOfFileWatcher extends LogAction {

	private String fileName;
	
	@Override
	public void execute() {
		
		File file = new File(fileName);
		
		try {
			BufferedReader is = new BufferedReader(new FileReader(file));
			
			List<Vector> data = new ArrayList<Vector>();
			
			LogStructure logStruct = getLogStructure();
			List<Field> fieldList = logStruct.getFieldList();
			Collections.sort(fieldList);
			int index=0;
			boolean usingPublic = true;
			
			PublicKey publicKey = null;
			PrivateKey privateKey = null;
			String aesKey = "";
			
			if(logStruct.isUseEncryption())
			{
				if (logStruct.getEncryptionAlgorithm().equals("AES")) 
				{
					aesKey = HandleKey.readAESKey(getPubicKeyFile() == null ? getPrivateKeyFile() : getPubicKeyFile());
				}
				else 
				{
					if (getPubicKeyFile() == null) 
					{
						usingPublic = false;
						privateKey = HandleKey.readPrivateKeyFromFile(getPrivateKeyFile());

					} else {
						publicKey = HandleKey.readPublicKeyFromFile(getPubicKeyFile());
					}
				}
			}
			while(true)
			{
				Vector<KeyValue> row = new Vector<KeyValue>();
				
				String line = is.readLine();
				if(line == null || line.isEmpty())
					break;
				String[] rowElem = line.split(" ");
				index = 0;
				for(; index <fieldList.size(); index++)
				{
					Field field = fieldList.get(index);
					if(field.isEncrypted())
					{
						if(logStruct.getEncryptionAlgorithm().equals("AES"))
						{
							row.add(new KeyValue<String>(field.getFieldName(), HandleKey.aesEncrypt(rowElem[index], aesKey), String.class));
						}
						else
						{
							if(usingPublic)
							{
								row.add(new KeyValue<String>(field.getFieldName(), Base64.encodeBytes(HandleKey.rsaEncrypt(rowElem[index].getBytes(), publicKey)), String.class));
							}
							else
							{
								row.add(new KeyValue<String>(field.getFieldName(), Base64.encodeBytes(HandleKey.rsaSignMessage(rowElem[index].getBytes(), privateKey)), String.class));
							}
						}
					}
					else
					{
						row.add(setKeyValue(fieldList.get(index), rowElem[index]));
					}
				}
				for(int i=index; i<fieldList.size(); i++)
				{
					row.add(setCurrentTime(fieldList.get(i)));
				}
				
				data.add(row);
			}
			
			saveFiledsToDatabase(data, logStruct.getName(), logStruct.isUseLogChain());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

	public void saveFiledsToDatabase(List<Vector> data, String logStructName, boolean useLogChain)
	{
		try {
			
			DBHandler dbHandler = new DBHandler();
			
			List<String> queryList = new ArrayList<String>();
			for (Vector row : data) {
				String q = "insert into " + logStructName + "(";
				for (int i = 0; i < row.size(); i++) {
					KeyValue kv = (KeyValue) row.get(i);
					q += kv.getKey() + ",";
				}
				q = useLogChain ? q + "logchain) values (" : q.substring(0, q.length() - 1) + ") values (";
				String rowValue = "";
				for (int i = 0; i < row.size(); i++) {
					KeyValue kv = (KeyValue) row.get(i);
					q += kv.getMyType().equals(String.class) ? "'" + kv.getValue() + "'," : kv.getValue() + ",";
					
					if(useLogChain)
					{
						rowValue += kv.getValue();
					}

				}
				//System.out.println("rv:"+rowValue);
				if(useLogChain)
				{
					String currHashs = getHashChain(getLogStructure().getName(),"id",rowValue,getLogStructure().getHashingAlgorithm());
					q = q + "'"+currHashs + "');";
				}
				else
				{
					q = q.substring(0, q.length() - 1) + ");";
				}
				//queryList.add(q);
				
				//System.out.println(q);
				//System.out.println("--------");
				
				dbHandler.insertData(q);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public KeyValue setKeyValue(Field field, String value)
	{
		
		if(field.getMyType().equals(Integer.class))
		{
			return new KeyValue<Integer>(field.getFieldName(), Integer.parseInt(value), Integer.class);
			//System.out.println("Integer");
		}
		
		else if(field.getMyType().equals(String.class))
		{
			return new KeyValue<String>(field.getFieldName(), value, String.class);
		}
		else if(field.getMyType().equals(Double.class))
		{
			return new KeyValue<Double>(field.getFieldName(), Double.parseDouble(value), Double.class);
		}
		
		return null; 
	}
	
	public KeyValue setCurrentTime(Field field)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date(System.currentTimeMillis());
		
		return new KeyValue<String>(field.getFieldName(), sdf.format(now), String.class);
		//field.setFieldValue(now);
	}
	

}
