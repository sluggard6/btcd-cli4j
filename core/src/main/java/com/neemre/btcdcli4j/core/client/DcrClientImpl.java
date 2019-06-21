package com.neemre.btcdcli4j.core.client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.Commands;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.DcrCommands;
import com.neemre.btcdcli4j.core.common.Defaults;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.BlockHeader;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.domain.dcr.BestBlock;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpc2ClientImpl;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClient;
import com.neemre.btcdcli4j.core.util.CollectionUtils;
import com.neemre.btcdcli4j.core.util.NumberUtils;

public class DcrClientImpl implements DcrClient {
	
	private static final Logger LOG = LoggerFactory.getLogger(DcrClientImpl.class);

	private ClientConfigurator configurator;
	private JsonRpcClient rpcClient;

	public DcrClientImpl(CloseableHttpClient httpProvider, Properties nodeConfig)
			throws BitcoindException, CommunicationException {
		initialize();
		rpcClient = new JsonRpc2ClientImpl(configurator.checkHttpProvider(httpProvider), 
				configurator.checkNodeConfig(nodeConfig));
	}
	
	private void initialize() {
		LOG.info(">> initialize(..): initiating the 'bitcoind' core wrapper");
		configurator = new ClientConfigurator();
	}

	@Override
	public BestBlock getBestBlock() throws BitcoindException, CommunicationException {
		String bestBlockJson = rpcClient.execute(DcrCommands.GET_BEST_BLOCK.getName());
		BestBlock bestBlock = rpcClient.getMapper().mapToEntity(bestBlockJson, 
				BestBlock.class);
		return bestBlock;
	}
	
	@Override
	public String getNewAddress(String account) throws BitcoindException, CommunicationException {
		String addressJson = rpcClient.execute(DcrCommands.GET_NEW_ADDRESS.getName(), account);
		String address = rpcClient.getParser().parseString(addressJson);
		return address;
	}
	
	@Override
	public Transaction getTransaction(String txId) throws BitcoindException, 
			CommunicationException {
		String transactionJson = rpcClient.execute(Commands.GET_TRANSACTION.getName(), txId);
		Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
				Transaction.class);
		return transaction;
	}

	@Override
	public Transaction getTransaction(String txId, Boolean withWatchOnly) throws BitcoindException,
			CommunicationException {
		List<Object> params = CollectionUtils.asList(txId, withWatchOnly);
		String transactionJson = rpcClient.execute(Commands.GET_TRANSACTION.getName(), params);
		Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
				Transaction.class);
		return transaction;
	}
	
	@Override
	public BlockHeader getBlockHeader(String headerHash) throws BitcoindException, CommunicationException {
		String blockJson = rpcClient.execute(Commands.GET_BLOCK_HEADER.getName(), headerHash);
		BlockHeader blockHeader = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
		return blockHeader;
	}
	
	@Override
	public String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException, 
			CommunicationException {
		amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
		List<Object> params = CollectionUtils.asList(toAddress, amount);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendToAddress(String toAddress, BigDecimal amount, String comment) 
			throws BitcoindException, CommunicationException {
		amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
		List<Object> params = CollectionUtils.asList(toAddress, amount, comment);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendToAddress(String toAddress, BigDecimal amount, String comment, 
			String commentTo) throws BitcoindException, CommunicationException {
		amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
		List<Object> params = CollectionUtils.asList(toAddress, amount, comment, commentTo);
		String transactionIdJson = rpcClient.execute(Commands.SEND_TO_ADDRESS.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses) 
			throws BitcoindException, CommunicationException {
		toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses,	
			Integer confirmations) throws BitcoindException, CommunicationException {
		toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses, confirmations);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}

	@Override
	public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses,
			Integer confirmations, String comment) throws BitcoindException, 
			CommunicationException {
		toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
		List<Object> params = CollectionUtils.asList(fromAccount, toAddresses, confirmations,
				comment);
		String transactionIdJson = rpcClient.execute(Commands.SEND_MANY.getName(), params);
		String transactionId = rpcClient.getParser().parseString(transactionIdJson);
		return transactionId;
	}
	
	@Override
	public BigDecimal getBalance() throws BitcoindException, CommunicationException {
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName());
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}
	
	@Override
	public BigDecimal getBalance(String account) throws BitcoindException, CommunicationException {
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), account);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}

	@Override
	public BigDecimal getBalance(String account, Integer confirmations) throws BitcoindException, 
			CommunicationException {
		List<Object> params = CollectionUtils.asList(account, confirmations);
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), params);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}

	@Override
	public BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly)
			throws BitcoindException, CommunicationException {
		List<Object> params = CollectionUtils.asList(account, confirmations, withWatchOnly);
		String balanceJson = rpcClient.execute(Commands.GET_BALANCE.getName(), params);
		BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
		return balance;
	}

	@Override
	public Map<String, BigDecimal> listAccounts() throws BitcoindException, CommunicationException {
		String accountsJson = rpcClient.execute(DcrCommands.LIST_ACCOUNTS.getName());
		Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson, String.class, BigDecimal.class);
		return accounts;
	}

	@Override
	public void createNewAccount(String account) throws BitcoindException, CommunicationException {
		String accountJson = rpcClient.execute(DcrCommands.CREATE_NEW_ACCOUNT.getName(), account);
	}

	@Override
	public List<String> getAddressesByAccount(String account) throws BitcoindException, CommunicationException {
		String addressesJson = rpcClient.execute(DcrCommands.GET_ADDRESSES_BY_ACCOUNT.getName(), account);
		List<String> addresses = rpcClient.getMapper().mapToList(addressesJson, String.class);
		return addresses;
	}

}
