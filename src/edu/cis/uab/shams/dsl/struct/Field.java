package edu.cis.uab.shams.dsl.struct;
/**
 * 
 * @author shams
 *
 */
public class Field<T> implements Comparable<Field>{
	
	//private T fieldValue;
	private String fieldName;
	private boolean isIndexBased;
	private int index;
	private boolean isEncrypted;
	
	private final Class<T> type;
	
	public Field(String fieldName, boolean isIndexBased,
			int index, boolean isEncrypted, Class<T> type) {
		this.fieldName = fieldName;
		this.isIndexBased = isIndexBased;
		this.index = index;
		this.isEncrypted = isEncrypted;
		this.type = type;
	}
	
	public Class<T> getMyType() {
        return this.type;
    }

//	public T getFieldValue() {
//		return fieldValue;
//	}
//
//	public void setFieldValue(T fieldValue) {
//		this.fieldValue = fieldValue;
//	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public boolean isIndexBased() {
		return isIndexBased;
	}

	public void setIndexBased(boolean isIndexBased) {
		this.isIndexBased = isIndexBased;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isEncrypted() {
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

//	public String toString()
//	{
//		return fieldValue+"";
//	}
	@Override
	public int compareTo(Field otherField) {
		// TODO Auto-generated method stub
		return this.index - otherField.index;
	}	
	
	
}
