package com.pluu.webtoon.item;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 웹툰 기본 정보
 * Created by PLUUSYSTEM-NEW on 2015-11-16.
 */
public class BaseToonInfo implements Parcelable {
	private final String id;
	private String title;
	private String image;
	private String updateDate;
	private String rate;
	private boolean adult = false;
	private boolean loginNeed = false;
	private Status status = Status.NONE;

	public BaseToonInfo(String id) {
		this.id = id;
	}

	public BaseToonInfo(BaseToonInfo item) {
		this.id = item.id;
		this.title = item.title;
		this.image = item.image;
		this.updateDate = item.updateDate;
		this.rate = item.rate;
		this.adult = item.adult;
		this.loginNeed = item.loginNeed;
		this.status = item.status;
	}

	public String getToonId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getImage() {
		return image;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public String getRate() {
		return rate;
	}

	public boolean isAdult() {
		return adult;
	}

	public boolean isLoginNeed() {
		return loginNeed;
	}

	public Status getStatus() {
		return status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public boolean isLock() {
		return loginNeed || adult;
	}

	@Override
	public String toString() {
		return "BaseToonInfo{" +
			"id='" + id + '\'' +
			", title='" + title + '\'' +
			", image='" + image + '\'' +
			", updateDate='" + updateDate + '\'' +
			", rate='" + rate + '\'' +
			", adult=" + adult +
			", loginNeed=" + loginNeed +
			", status=" + status +
			'}';
	}

	public void setLoginNeed(boolean loginNeed) {
		this.loginNeed = loginNeed;
	}

	public void setIsAdult(boolean isAdult) {
		this.adult = isAdult;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int describeContents() { return 0; }

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.title);
		dest.writeString(this.image);
		dest.writeString(this.updateDate);
		dest.writeString(this.rate);
		dest.writeByte(adult ? (byte) 1 : (byte) 0);
		dest.writeByte(loginNeed ? (byte) 1 : (byte) 0);
		dest.writeInt(this.status == null ? -1 : this.status.ordinal());
	}

	protected BaseToonInfo(Parcel in) {
		this.id = in.readString();
		this.title = in.readString();
		this.image = in.readString();
		this.updateDate = in.readString();
		this.rate = in.readString();
		this.adult = in.readByte() != 0;
		this.loginNeed = in.readByte() != 0;
		int tmpStatus = in.readInt();
		this.status = tmpStatus == -1 ? null : Status.values()[tmpStatus];
	}

	public static final Creator<BaseToonInfo> CREATOR = new Creator<BaseToonInfo>() {
		public BaseToonInfo createFromParcel(Parcel source) {return new BaseToonInfo(source);}

		public BaseToonInfo[] newArray(int size) {return new BaseToonInfo[size];}
	};
}
