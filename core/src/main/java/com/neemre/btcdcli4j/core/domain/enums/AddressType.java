package com.neemre.btcdcli4j.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import com.neemre.btcdcli4j.core.common.Errors;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public enum AddressType {
	
	LEGACY("legacy"),
	P2SH_SEGWIT("p2sh-segwit"),
	BECH32("bech32");
	
	
	private final String name;
	
	@JsonValue
	public String getName() {
		return name;
	}
	
	@JsonCreator
	public static AddressType forName(String name) {
		if (name != null) {
			for (AddressType addressType : AddressType.values()) {
				if (name.equals(addressType.getName())) {
					return addressType;
				}
			}
		}
		throw new IllegalArgumentException(Errors.ARGS_BTCD_CHAINTYPE_UNSUPPORTED.getDescription());
	}

}
