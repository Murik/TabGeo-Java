package main.java;

import com.neovisionaries.i18n.CountryCode;

/**
 * Created by murik on 17.11.15.
 */
public class Unpack {
	int ip;
	int offset;
	int country_ID;


	public Integer getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getCountry_ID() {
		return country_ID;
	}

	public void setCountry_ID(int country_ID) {
		this.country_ID = country_ID;
	}

	public Unpack() {
	}


	@Override
	public String toString() {
		return "Unpack{" +
				"offset=" + offset +
				", ip=" + ip +
				", country_ID=" + country_ID +
				'}';
	}

	public CountryCode getCountryCode(){
		return CountryCode.getByCode(Iso.values()[country_ID].toString());
	}
}
