package edu.cis.uab.shams.dsl.struct;

public class KeyValue<V> {

	private V value;
	private String key;
	private final Class<V> type;
	public KeyValue(String key, V val, Class<V> type)
	{
		this.key= key;
		this.value = val;
		this.type = type;
	}
	public String getKey()
	{
		return key;
	}
	public V getValue()
	{
		return value;
	}
	public Class<V> getMyType() {
        return this.type;
    }
}
