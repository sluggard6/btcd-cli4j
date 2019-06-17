package com.neemre.btcdcli4j.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum DcrCommands {
	
	GET_BEST_BLOCK("getbestblock", 0, 0);
	
	private final String name;
	private final int minParams;
	private final int maxParams;

}
