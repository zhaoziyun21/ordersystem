package com.kssj.frame.se.command;

/**
* @Description: a speller of the sql a single sort statement
* 
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2014-1-3 下午03:17:04
* @version V1.0
*/
public class SeOrderSpeller{
	
	private String property;
	private String order = "desc";
	
	/** spell type:ORDER / CONDITION */
	private SeSpellerType spellType = SeSpellerType.ORDER;
	
	public SeOrderSpeller(){
		
	}
	/**
	 * 	Constructor
	 * 
	 * @param property  :(Sort Field)
	 * @param order 	:sort-type( asc,desc )
	 */
	public SeOrderSpeller(String property,String order){
		this.property = property;
		this.order = order;
	}
	/**①
	 *  spell "order by" sql statement
	 *  
	 * @return order by 语句
	 */
	public String spell(){
		return " " + property + " " + order + " " ;
	}
	
	/**②
	 *  SQL : get the type of the speller 
	 * 
	 * @return sql speller Type
	 */
	public SeSpellerType getSpellType() {
		return spellType;
	}
	
	public String getProperty() {
		return property;
	}
	
	public void setProperty(String property) {
		this.property = property;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	
}
