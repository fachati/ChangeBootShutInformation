package com.mobiwire.BootInformation;

import java.io.Serializable;

public class APN implements Serializable {
	private String id;
	private String apn;
	private String type;
	private String name;
	private String userName;
	private String password;
	private String mnc;
	private String mcc;



	public APN() {

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		APN apn1 = (APN) o;

		if (!id.equals(apn1.id)) return false;
		if (!apn.equals(apn1.apn)) return false;
		if (!type.equals(apn1.type)) return false;
		if (!name.equals(apn1.name)) return false;
		if (!userName.equals(apn1.userName)) return false;
		if (!password.equals(apn1.password)) return false;
		if (!mnc.equals(apn1.mnc)) return false;
		return mcc.equals(apn1.mcc);

	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + apn.hashCode();
		result = 31 * result + type.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + userName.hashCode();
		result = 31 * result + password.hashCode();
		result = 31 * result + mnc.hashCode();
		result = 31 * result + mcc.hashCode();
		return result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApn() {
		return apn;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	@Override
	public String toString() {
		return "APN{" +
				"id='" + id + '\'' +
				", apn='" + apn + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", userName='" + userName + '\'' +
				", password='" + password + '\'' +
				", mnc='" + mnc + '\'' +
				", mcc='" + mcc + '\'' +
				'}';
	}
}
