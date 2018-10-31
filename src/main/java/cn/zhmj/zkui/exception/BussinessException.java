package cn.zhmj.zkui.exception;

public class BussinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public BussinessException(Object message) {
		super(String.valueOf(message));
	}
}
