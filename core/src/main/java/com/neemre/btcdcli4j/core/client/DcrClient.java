package com.neemre.btcdcli4j.core.client;

import java.math.BigDecimal;
import java.util.Map;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.BlockHeader;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.domain.dcr.BestBlock;

public interface DcrClient {
	
	BestBlock getBestBlock() throws BitcoindException, CommunicationException;
	
	String getNewAddress(String account) throws BitcoindException, CommunicationException;
	
	Transaction getTransaction(String txId) throws BitcoindException, CommunicationException;

	Transaction getTransaction(String txId, Boolean withWatchOnly) throws BitcoindException, CommunicationException;
	
	BlockHeader getBlockHeader(String headerHash) throws BitcoindException, CommunicationException;
	
	String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException, CommunicationException;
	
	String sendToAddress(String toAddress, BigDecimal amount, String comment) 
			throws BitcoindException, CommunicationException;

	String sendToAddress(String toAddress, BigDecimal amount, String comment, String commentTo) 
			throws BitcoindException, CommunicationException;
	
	String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses) 
			throws BitcoindException, CommunicationException;

	String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations) 
			throws BitcoindException, CommunicationException;

	String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations, 
			String comment) throws BitcoindException, CommunicationException;
	
	BigDecimal getBalance() throws BitcoindException, CommunicationException;

	BigDecimal getBalance(String account) throws BitcoindException, CommunicationException;

	BigDecimal getBalance(String account, Integer confirmations) throws BitcoindException, 
			CommunicationException;

	BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly) 
			throws BitcoindException, CommunicationException;

}
