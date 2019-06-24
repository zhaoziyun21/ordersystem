package com.kssj.frame.exception;

/**
* @Description: Exception : Illegal Url
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-16 下午01:27:11
* @version V1.0
*/
@SuppressWarnings("serial")
public class IllegalUrlException extends RuntimeException {
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public IllegalUrlException(String message)
	{
		super(message);
		this.message=message;
	}
	
	
	/**
     * 
     */
    public IllegalUrlException() {
        super();
        // TODO Auto-generated constructor stub
    }

   /**
     * @param message
     * @param cause
     */
    public IllegalUrlException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

   /**
     * @param cause
     */
    public IllegalUrlException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
