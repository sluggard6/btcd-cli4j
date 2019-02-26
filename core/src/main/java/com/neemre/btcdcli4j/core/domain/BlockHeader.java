package com.neemre.btcdcli4j.core.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockHeader extends Entity {
	
	protected String hash;
	protected Integer confirmations;
	protected Integer size;
	protected Integer height;
	protected Integer version;
	protected String versionHex;
	@JsonProperty("merkleroot")
	protected String merkleRoot;
	protected Long time;
	@JsonProperty("mediantime")
	protected Long medianTime;
	protected Long nonce;
	protected String bits;
	@Setter(AccessLevel.NONE)
	protected BigDecimal difficulty;
	@JsonProperty("chainwork")
	protected String chainWork;
	protected Integer nTx;
	@JsonProperty("previousblockhash")
	protected String previousBlockHash;
	@JsonProperty("nextblockhash")
	protected String nextBlockHash;

}
