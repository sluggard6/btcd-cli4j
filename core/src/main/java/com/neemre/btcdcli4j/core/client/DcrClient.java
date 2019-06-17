package com.neemre.btcdcli4j.core.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.dcr.BestBlock;

public interface DcrClient {
	
	BestBlock getBestBlock() throws BitcoindException, CommunicationException;

}
