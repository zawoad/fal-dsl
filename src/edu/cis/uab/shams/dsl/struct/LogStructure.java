package edu.cis.uab.shams.dsl.struct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author shams
 *
 */
public class LogStructure {
		
	private List<Field> fieldList;
	private String encryptionAlgorithm;
	private String hashingAlgorithm;
	private String name;
	public LogStructure()
	{
		fieldList = new ArrayList<Field>();
	}

	public List<Field> getFieldList()
	{
		return fieldList;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	public void setEncryptionAlgorithm(String encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	public String getHashingAlgorithm() {
		return hashingAlgorithm;
	}

	public void setHashingAlgorithm(String hashingAlgorithm) {
		this.hashingAlgorithm = hashingAlgorithm.replaceAll("_", "-");
	}
	
	public boolean isUseLogChain()
	{
		return (getHashingAlgorithm() != null && !getHashingAlgorithm().isEmpty());
	}
	
	public boolean isUseEncryption()
	{
		return (getEncryptionAlgorithm() != null && !getEncryptionAlgorithm().isEmpty());
	}
	
	public void addField(FieldType type, String fieldName, boolean isIndexBased,
			int index, boolean isEncrypted)
	{
		
		switch (type) {
			case IP:
				Field<String> fieldIP = new Field<String>(fieldName, isIndexBased, index, isEncrypted, String.class);
				fieldList.add(fieldIP);
				break;
			case TEXT:
				Field<String> fieldTxt = new Field<String>(fieldName, isIndexBased, index, isEncrypted, String.class);
				fieldList.add(fieldTxt);
				break;
			case INT:
				Field<Integer> fieldInt = new Field<Integer>(fieldName, isIndexBased, index, isEncrypted, Integer.class);
				fieldList.add(fieldInt);
				break;
			case DOUBLE:
				Field<Double> fieldDbl = new Field<Double>(fieldName, isIndexBased, index, isEncrypted, Double.class);
				fieldList.add(fieldDbl);
				break;
			case TIME:
				Field<String> fieldTime = new Field<String>(fieldName, isIndexBased, index, isEncrypted, String.class);
				fieldList.add(fieldTime);
				break;
			default:
					System.out.println("Invalid Type");
					break;

		}
	}
}
