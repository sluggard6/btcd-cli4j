package com.neemre.btcdcli4j.core.client;

import java.util.List;

public interface DcrWsClient {
	
	void notifyBlocks();
	
	void notifyNewTransactions();
	
	void notifyNewTransactions(Boolean verbose);
	
	void notifyReceived(List<String> addresses);

}
