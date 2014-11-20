package exceptionPackage;

public class IllegalOperation extends Exception {

	private static final long serialVersionUID = 1L;
	String str1 = " ";

	public IllegalOperation(StackTraceElement[] stacktrace) {
		for (int i = 0; i < stacktrace.length; i++)
			str1 = str1 + stacktrace[i].toString() + "\n";
	}

	public IllegalOperation(String str2) {
		str1 = str2;
	}

	public String getStackTraceMessage() {
		return ("Exception message:" + str1);
	}
	
}
