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
	
	NOTIFY_BLOCKS("notifyblocks", 0, 0),
	
	NOTIFY_RECEIVED("notifyreceived", 1, 1),
	
	NOTIFY_NEW_TRANSACTIONS("notifynewtransactions", 0,1), 
	
	LIST_ACCOUNTS("listaccounts", 0, 0), 
	
	CREATE_NEW_ACCOUNT("createnewaccount", 1, 1), 
	
	GET_ADDRESSES_BY_ACCOUNT("getaddressesbyaccount", 1, 1);
	
	private final String name;
	private final int minParams;
	private final int maxParams;

}
