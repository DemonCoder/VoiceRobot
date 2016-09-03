package bean;

import java.util.ArrayList;

public class MMBean {

	public int showapi_res_code;
	public String showapi_res_error;
	public ArrayList<MMBodyBean> showapi_res_body;
	
	public class MMBodyBean{
		public String thumb;
		public String title;
		public String url;
	}
}
