package edu.cis.uab.shams;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import edu.cis.uab.shams.dsl.encryption.HandleKey;

public class SecLogger {

	//Set<E>
	
	public static void main(String[] args) {
//		setParams("aa","bb","cc","dd");
//		
//		HashSet var = new HashSet<String>();
//		var = put(var,"netLog");
//		System.out.println(var.contains("netLog"));
		
		String plainText = "hello hola abjab hello hola abjab hello hola abjab hello hola abjab"; /*Note null padding*/
		//String encryptionKey = "0123456789abcdef";
		String encryptionKey = HandleKey.readAESKey("aeskey.key");
		try {
			String enc = HandleKey.aesEncrypt(plainText, encryptionKey);
			System.out.println(enc);
			System.out.println(HandleKey.aesDecrypt(enc.getBytes(), encryptionKey));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	public static void setParams(String...strings) {
		
	}
	
	 public static HashSet<String> put(HashSet<String> env, String varName)
	   {
	   	System.out.println("Variable Set:"+varName);
	   	env = (HashSet<String>)env.clone();
	        env.add(varName);
	        env.addAll(env);
	        return env;
	   	
	   }
	 BufferedWriter open_append(String fileName, String attribute, String separator) {
	     try {
	            
	            BufferedWriter outFile;
	            File f = new File(fileName);
	            if (f.exists())  
	            {
	               f.delete();
	            }
	            outFile = new BufferedWriter(new FileWriter(fileName));
	            outFile.write(attribute + separator);
	            outFile.close(); 
	            return outFile;
	     } catch (IOException e) {
	            e.printStackTrace();
	     }  
	     return null;
	   } 
	String getHeader()
	  {
	  	String head = "import edu.cis.uab.shams.dsl.struct.Field;\n"
			+"import edu.cis.uab.shams.dsl.struct.FileWatcher;\n"
			+"import edu.cis.uab.shams.dsl.struct.LogStructure;\n"
			+"import edu.cis.uab.shams.dsl.struct.TableWatcher;\n"
			+"public class Gen1{\n"
			+"\tpublic static void main(String args)\n"
			+"\t{\n";
		return head;
	  }
}
