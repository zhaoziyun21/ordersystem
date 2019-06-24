package com.kssj.frame.model;

/**
* @Description: tree节点工具类
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-14 下午03:05:39
* @version V1.0
*/
public class TreeNodeModel extends BaseModel{
	private static final long serialVersionUID = 4494182621156774221L;
	
	private String id;  
    private String pId;  
    private String name;  
    private Boolean checked;  
    private Boolean open;
    private Boolean isParent;
    
	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public TreeNodeModel() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public TreeNodeModel(String id, String pId, String name, Boolean checked,
			Boolean open) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.checked = checked;
		this.open = open;
	}
    
    
}
