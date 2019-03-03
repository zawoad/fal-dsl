package edu.cis.uab.shams.dsl.dal;

public class LocationProof {
	private String location;
	private String locAuthority;
	private String proofTime;
	private String lsSignHash;
	private String asSignHash;
	private long id;
	
	public LocationProof(String location, String locAuthority, String proofTime) {
		this.location = location;
		this.locAuthority = locAuthority;
		this.proofTime = proofTime;
	}
	public LocationProof()
	{
		
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLocAuthority() {
		return locAuthority;
	}
	public void setLocAuthority(String locAuthority) {
		this.locAuthority = locAuthority;
	}
	public String getProofTime() {
		return proofTime;
	}
	public void setProofTime(String proofTime) {
		this.proofTime = proofTime;
	}
	
	
	public String getLsSignHash() {
		return lsSignHash;
	}
	public void setLsSignHash(String lsSignHash) {
		this.lsSignHash = lsSignHash;
	}
	public String getAsSignHash() {
		return asSignHash;
	}
	public void setAsSignHash(String asSignHash) {
		this.asSignHash = asSignHash;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
//	@Override
//	  public String toString() {
//	    return location + " | "+ locAuthority + " | " + proofTime;
//	  }
}
