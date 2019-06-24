package com.kssj.frame.exception;

/**
* @Description: Exception : session out time
* @Company:TGRF
* @author:ChenYW
* 
* @date: 2013-10-16 下午01:27:27
* @version V1.0
*/
@SuppressWarnings("serial")
public class SessionTimeException extends RuntimeException {
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public SessionTimeException(String message)
	{
		super(message);
		this.message=message;
	}
	
	/**
     * 
     */
    public SessionTimeException() {
        super();
        // TODO Auto-generated constructor stub
    }

   /**
     * @param message
     * @param cause
     */
    public SessionTimeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

   /**
     * @param cause
     */
    public SessionTimeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

}
