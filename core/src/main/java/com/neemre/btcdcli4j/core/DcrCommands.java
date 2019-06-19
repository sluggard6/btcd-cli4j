package com.neemre.btcdcli4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum DcrCommands {
	
	GET_BEST_BLOCK("getbestblock", 0, 0),
	
	GET_NEW_ADDRESS("getnewaddress", 1, 2),
	
	NOTIFY_RECE$IVED("notifyreceived", 1, 1),
	
	NOTIFY_NEW_TRANSACTIONS("notifynewtransactions", 0,1);
	
	private final String name;
	private final int minParams;
	private final int maxParams;

}
