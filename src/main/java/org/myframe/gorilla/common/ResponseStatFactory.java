package org.myframe.gorilla.common;

public class ResponseStatFactory {

	public static final String OK = "gorilla_OK";
	public static final String VOID = "gorilla_VOID";
	public static final String ER = "gorilla_ERROR";

	public static String valueOK() {
		return OK;
	}

	public static String valueVOID() {
		return VOID;
	}

	public static boolean isVOID(Object obj) {

		if (null == obj) {
			return false;
		}
		if (obj.toString().equals(VOID)) {
			return true;
		}
		return false;
	}

}
