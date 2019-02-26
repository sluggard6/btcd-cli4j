package com.neemre.btcdcli4j.examples.client;

import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.enums.CheckLevels;
import com.neemre.btcdcli4j.examples.util.ResourceUtils;

/**A list of examples demonstrating the use of <i>bitcoind</i>'s block chain RPCs (via the JSON-RPC 
 * API).*/
public class BlockChainApi {

	public static void main(String[] args) throws Exception {
		String address = "2Mu9c8dMpkEjVrWJMgRSmB4LhKnRDJM7Rzh";
		
		CloseableHttpClient httpProvider = ResourceUtils.getHttpProvider();
		Properties nodeConfig = ResourceUtils.getNodeConfig();
		BtcdClient client = new VerboseBtcdClientImpl(httpProvider, nodeConfig);
		for(String labels: client.listLabels()) {
			System.out.println(labels);
		}
//		client.getWalletInfo();
//		client.importPrivKey("cQ4VqdFnLmwEkbb552Cw6jwXxbE4YcoftzGmp8de5gMnSGFu3iMR", "test1");
		System.out.println(client.getTransaction("ab8b6ab414273d82c15cad5563b20ec90f49bcb8b358900bc1467771c77d1dd5"));
//		String account = client.getAccount(address);
//		System.out.println(account);
//		client.getBestBlockHash();
//		client.getBlock("00000000000000e8cf3d4fab91c642f5d5bb13339613aa915a42a7f1c91ab5ba");
//		client.getBlock("00000000000000e8cf3d4fab91c642f5d5bb13339613aa915a42a7f1c91ab5ba", true);
//		client.getBlockChainInfo();
//		client.getBlockCount();
//		client.getBlockHash(345168);
//		client.getChainTips();
//		client.getDifficulty();
//		client.getMemPoolInfo();
//		client.getRawMemPool();
//		client.getRawMemPool(true);
//		client.getTxOutSetInfo();
//		client.verifyChain();
//		client.verifyChain(CheckLevels.LEVEL_4.getIdentifier());
//		client.verifyChain(CheckLevels.LEVEL_4.getIdentifier(), 1000);
	}
}