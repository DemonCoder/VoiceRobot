package bean;

import java.util.ArrayList;

public class NewsBean {
	public int code;
	public String text;
	public ArrayList<NewsBody> list;
	
	public class NewsBody{
		public String article;
		public String detailurl;
		public String icon;
		public String source;
	}
}
