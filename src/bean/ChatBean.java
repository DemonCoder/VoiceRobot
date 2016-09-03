package bean;

public class ChatBean {
	public ChatBean(int type,String text,String url,String attachPIC){
		this.type = type;
		this.text = text;
		this.url = url;
		this.attachPIC = attachPIC;
	}
	public int type = -1;
	public String text = "";
	public String url = "";
	public String attachPIC = "";
}
