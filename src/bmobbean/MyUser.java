package bmobbean;
import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
	private String signature;//签名
	private String gender;//性别
	private String nickname;//昵称
	private String portraitUrl;//头像地址
	private boolean vip;
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPortraitUrl() {
		return portraitUrl;
	}
	public void setPortraitUrl(String portraitUrl) {
		this.portraitUrl = portraitUrl;
	}
	public boolean isVip() {
		return vip;
	}
	public void setVip(boolean vip) {
		this.vip = vip;
	}
}
