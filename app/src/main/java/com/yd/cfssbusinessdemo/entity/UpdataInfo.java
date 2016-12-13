package com.yd.cfssbusinessdemo.entity;

/**
 * 
* @ClassName: UpdataInfo 
* @Description: 检测版本更新
* @author Yin_Juan
* @date 2016年6月21日 上午11:03:45 
*
 */
public class UpdataInfo {
	
	private String version;//版本号
	private String description;  //版本描述
    private String url;  	//服务器地址
    
	public UpdataInfo() {
		super();
	}
	
	

	public UpdataInfo(String version, String description, String url) {
		super();
		this.version = version;
		this.description = description;
		this.url = url;
	}



	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
    
	

}
