package com.neemre.btcdcli4j.examples;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class MainTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String encoding = new String(Base64.encodeBase64(StringUtils.getBytesUtf8("test:test")));
		System.out.println(encoding);
	}

}
