package com.neemre.btcdcli4j.core.client;

import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.DcrCommands;
import com.neemre.btcdcli4j.core.domain.dcr.BestBlock;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpc2ClientImpl;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClient;

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

}
