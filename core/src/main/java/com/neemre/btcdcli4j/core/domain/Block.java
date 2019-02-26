package com.neemre.btcdcli4j.core.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.neemre.btcdcli4j.core.common.Defaults;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block extends BlockHeader {

	private List<String> tx;

	public Block(String hash, Integer confirmations, Integer size, Integer height, Integer version,
			String merkleRoot, List<String> tx, Long time, Long medianTime, Long nonce, String bits, 
			BigDecimal difficulty, String chainWork, Integer nTx, String previousBlockHash, String nextBlockHash) {
		setHash(hash);
		setConfirmations(confirmations);
		setSize(size);
		setHeight(height);
		setVersion(version);
		setMerkleRoot(merkleRoot);
		setTx(tx);
		setTime(time);
		setMedianTime(medianTime);
		setNonce(nonce);
		setBits(bits);
		setDifficulty(difficulty);
		setChainWork(chainWork);
		setNTx(nTx);
		setPreviousBlockHash(previousBlockHash);
		setNextBlockHash(nextBlockHash);
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
	}
}