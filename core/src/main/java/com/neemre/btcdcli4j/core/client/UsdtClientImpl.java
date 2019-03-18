package com.neemre.btcdcli4j.core.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.UsdtCommands;
import com.neemre.btcdcli4j.core.common.DataFormats;
import com.neemre.btcdcli4j.core.common.Defaults;
import com.neemre.btcdcli4j.core.domain.Account;
import com.neemre.btcdcli4j.core.domain.AddedNode;
import com.neemre.btcdcli4j.core.domain.Address;
import com.neemre.btcdcli4j.core.domain.AddressInfo;
import com.neemre.btcdcli4j.core.domain.AddressOverview;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.BlockChainInfo;
import com.neemre.btcdcli4j.core.domain.BlockHeader;
import com.neemre.btcdcli4j.core.domain.Info;
import com.neemre.btcdcli4j.core.domain.MemPoolInfo;
import com.neemre.btcdcli4j.core.domain.MemPoolTransaction;
import com.neemre.btcdcli4j.core.domain.MiningInfo;
import com.neemre.btcdcli4j.core.domain.MultiSigAddress;
import com.neemre.btcdcli4j.core.domain.NetworkInfo;
import com.neemre.btcdcli4j.core.domain.NetworkTotals;
import com.neemre.btcdcli4j.core.domain.OmniBalance;
import com.neemre.btcdcli4j.core.domain.OmniTransaction;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.Payment;
import com.neemre.btcdcli4j.core.domain.PeerNode;
import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.neemre.btcdcli4j.core.domain.RawTransactionOverview;
import com.neemre.btcdcli4j.core.domain.RedeemScript;
import com.neemre.btcdcli4j.core.domain.SignatureResult;
import com.neemre.btcdcli4j.core.domain.SinceBlock;
import com.neemre.btcdcli4j.core.domain.Tip;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.domain.TxOutSetInfo;
import com.neemre.btcdcli4j.core.domain.WalletInfo;
import com.neemre.btcdcli4j.core.domain.enums.AddressType;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClient;
import com.neemre.btcdcli4j.core.jsonrpc.client.JsonRpcClientImpl;
import com.neemre.btcdcli4j.core.util.CollectionUtils;
import com.neemre.btcdcli4j.core.util.NumberUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class UsdtClientImpl implements UsdtClient {

    private static final Logger LOG = LoggerFactory.getLogger(UsdtClientImpl.class);

    private ClientConfigurator configurator;
    private JsonRpcClient rpcClient;


    public UsdtClientImpl(Properties nodeConfig) throws BitcoindException, CommunicationException {
        this(null, nodeConfig);
    }

    public UsdtClientImpl(CloseableHttpClient httpProvider, Properties nodeConfig)
            throws BitcoindException, CommunicationException {
        initialize();
        rpcClient = new JsonRpcClientImpl(configurator.checkHttpProvider(httpProvider),
                configurator.checkNodeConfig(nodeConfig));
        configurator.checkNodeVersion(getNetworkInfo().getVersion());
        configurator.checkNodeHealth((Block) getBlock(getBestBlockHash(), true));
    }

    public UsdtClientImpl(String rpcUser, String rpcPassword) throws BitcoindException,
            CommunicationException {
        this(null, null, rpcUser, rpcPassword);
    }

    public UsdtClientImpl(CloseableHttpClient httpProvider, String rpcUser, String rpcPassword)
            throws BitcoindException, CommunicationException {
        this(httpProvider, null, null, rpcUser, rpcPassword);
    }

    public UsdtClientImpl(String rpcHost, Integer rpcPort, String rpcUser, String rpcPassword)
            throws BitcoindException, CommunicationException {
        this((String) null, rpcHost, rpcPort, rpcUser, rpcPassword);
    }

    public UsdtClientImpl(CloseableHttpClient httpProvider, String rpcHost, Integer rpcPort,
                          String rpcUser, String rpcPassword) throws BitcoindException, CommunicationException {
        this(httpProvider, null, rpcHost, rpcPort, rpcUser, rpcPassword);
    }

    public UsdtClientImpl(String rpcProtocol, String rpcHost, Integer rpcPort, String rpcUser,
                          String rpcPassword) throws BitcoindException, CommunicationException {
        this(rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, null);
    }

    public UsdtClientImpl(CloseableHttpClient httpProvider, String rpcProtocol, String rpcHost,
                          Integer rpcPort, String rpcUser, String rpcPassword) throws BitcoindException,
            CommunicationException {
        this(httpProvider, rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, null);
    }

    public UsdtClientImpl(String rpcProtocol, String rpcHost, Integer rpcPort, String rpcUser,
                          String rpcPassword, String httpAuthScheme) throws BitcoindException,
            CommunicationException {
        this(null, rpcProtocol, rpcHost, rpcPort, rpcUser, rpcPassword, httpAuthScheme);
    }

    public UsdtClientImpl(CloseableHttpClient httpProvider, String rpcProtocol, String rpcHost,
                          Integer rpcPort, String rpcUser, String rpcPassword, String httpAuthScheme)
            throws BitcoindException, CommunicationException {
        this(httpProvider, new ClientConfigurator().toProperties(rpcProtocol, rpcHost, rpcPort,
                rpcUser, rpcPassword, httpAuthScheme));
    }

    @Override
    public String addMultiSigAddress(Integer minSignatures, List<String> addresses)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(minSignatures, addresses);
        String multiSigAddressJson = rpcClient.execute(UsdtCommands.ADD_MULTI_SIG_ADDRESS.getName(),
                params);
        String multiSigAddress = rpcClient.getParser().parseString(multiSigAddressJson);
        return multiSigAddress;
    }

    @Override
    public String addMultiSigAddress(Integer minSignatures, List<String> addresses,
                                     String account) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(minSignatures, addresses, account);
        String multiSigAddressJson = rpcClient.execute(UsdtCommands.ADD_MULTI_SIG_ADDRESS.getName(),
                params);
        String multiSigAddress = rpcClient.getParser().parseString(multiSigAddressJson);
        return multiSigAddress;
    }

    @Override
    public void addNode(String node, String command) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(node, command);
        rpcClient.execute(UsdtCommands.ADD_NODE.getName(), params);
    }

    @Override
    public void backupWallet(String filePath) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.BACKUP_WALLET.getName(), filePath);
    }

    @Override
    public MultiSigAddress createMultiSig(Integer minSignatures, List<String> addresses)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(minSignatures, addresses);
        String multiSigAddressJson = rpcClient.execute(UsdtCommands.CREATE_MULTI_SIG.getName(), params);
        MultiSigAddress multiSigAddress = rpcClient.getMapper().mapToEntity(multiSigAddressJson,
                MultiSigAddress.class);
        return multiSigAddress;
    }

    @Override
    public String createRawTransaction(List<OutputOverview> outputs,
                                       Map<String, BigDecimal> toAddresses) throws BitcoindException, CommunicationException {
        toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
        List<Object> params = CollectionUtils.asList(outputs, toAddresses);
        String hexTransactionJson = rpcClient.execute(UsdtCommands.CREATE_RAW_TRANSACTION.getName(),
                params);
        String hexTransaction = rpcClient.getParser().parseString(hexTransactionJson);
        return hexTransaction;
    }

    @Override
    public RawTransactionOverview decodeRawTransaction(String hexTransaction)
            throws BitcoindException, CommunicationException {
        String rawTransactionJson = rpcClient.execute(UsdtCommands.DECODE_RAW_TRANSACTION.getName(),
                hexTransaction);
        RawTransactionOverview rawTransaction = rpcClient.getMapper().mapToEntity(
                rawTransactionJson, RawTransactionOverview.class);
        return rawTransaction;
    }

    @Override
    public RedeemScript decodeScript(String hexRedeemScript) throws BitcoindException,
            CommunicationException {
        String redeemScriptJson = rpcClient.execute(UsdtCommands.DECODE_SCRIPT.getName(),
                hexRedeemScript);
        RedeemScript redeemScript = rpcClient.getMapper().mapToEntity(redeemScriptJson,
                RedeemScript.class);
        redeemScript.setHex(hexRedeemScript);
        return redeemScript;
    }

    @Override
    public String dumpPrivKey(String address) throws BitcoindException, CommunicationException {
        String privateKeyJson = rpcClient.execute(UsdtCommands.DUMP_PRIV_KEY.getName(), address);
        String privateKey = rpcClient.getParser().parseString(privateKeyJson);
        return privateKey;
    }

    @Override
    public void dumpWallet(String filePath) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.DUMP_WALLET.getName(), filePath);
    }

    @Override
    public String encryptWallet(String passphrase) throws BitcoindException,
            CommunicationException {
        String noticeMsgJson = rpcClient.execute(UsdtCommands.ENCRYPT_WALLET.getName(), passphrase);
        String noticeMsg = rpcClient.getParser().parseString(noticeMsgJson);
        return noticeMsg;
    }

    @Override
    public BigDecimal estimateFee(Integer maxBlocks) throws BitcoindException,
            CommunicationException {
        String estimatedFeeJson = rpcClient.execute(UsdtCommands.ESTIMATE_FEE.getName(), maxBlocks);
        BigDecimal estimatedFee = rpcClient.getParser().parseBigDecimal(estimatedFeeJson);
        return estimatedFee;
    }

    @Override
    public BigDecimal estimatePriority(Integer maxBlocks) throws BitcoindException,
            CommunicationException {
        String estimatedPriorityJson = rpcClient.execute(UsdtCommands.ESTIMATE_PRIORITY.getName(),
                maxBlocks);
        BigDecimal estimatedPriority = rpcClient.getParser().parseBigDecimal(estimatedPriorityJson);
        return estimatedPriority;
    }

    @Override
    public String getAccount(String address) throws BitcoindException, CommunicationException {
        String accountJson = rpcClient.execute(UsdtCommands.GET_ACCOUNT.getName(), address);
        String account = rpcClient.getParser().parseString(accountJson);
        return account;
    }

    @Override
    public String getAccountAddress(String account) throws BitcoindException,
            CommunicationException {
        String addressJson = rpcClient.execute(UsdtCommands.GET_ACCOUNT_ADDRESS.getName(), account);
        String address = rpcClient.getParser().parseString(addressJson);
        return address;
    }

    @Override
    public List<AddedNode> getAddedNodeInfo(Boolean withDetails) throws BitcoindException,
            CommunicationException {
        String addedNodesJson = rpcClient.execute(UsdtCommands.GET_ADDED_NODE_INFO.getName(),
                withDetails);
        List<AddedNode> addedNodes = rpcClient.getMapper().mapToList(addedNodesJson,
                AddedNode.class);
        return addedNodes;
    }

    @Override
    public List<AddedNode> getAddedNodeInfo(Boolean withDetails, String node)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(withDetails, node);
        String addedNodesJson = rpcClient.execute(UsdtCommands.GET_ADDED_NODE_INFO.getName(), params);
        List<AddedNode> addedNodes = rpcClient.getMapper().mapToList(addedNodesJson,
                AddedNode.class);
        return addedNodes;
    }

    @Override
    public List<String> getAddressesByAccount(String account) throws BitcoindException,
            CommunicationException {
        String addressesJson = rpcClient.execute(UsdtCommands.GET_ADDRESSES_BY_ACCOUNT.getName(),
                account);
        List<String> addresses = rpcClient.getMapper().mapToList(addressesJson, String.class);
        return addresses;
    }

    @Override
    public BigDecimal getBalance() throws BitcoindException, CommunicationException {
        String balanceJson = rpcClient.execute(UsdtCommands.GET_BALANCE.getName());
        BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
        return balance;
    }

    @Override
    public BigDecimal getBalance(String account) throws BitcoindException, CommunicationException {
        String balanceJson = rpcClient.execute(UsdtCommands.GET_BALANCE.getName(), account);
        BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
        return balance;
    }

    @Override
    public BigDecimal getBalance(String account, Integer confirmations) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(account, confirmations);
        String balanceJson = rpcClient.execute(UsdtCommands.GET_BALANCE.getName(), params);
        BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
        return balance;
    }

    @Override
    public BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(account, confirmations, withWatchOnly);
        String balanceJson = rpcClient.execute(UsdtCommands.GET_BALANCE.getName(), params);
        BigDecimal balance = rpcClient.getParser().parseBigDecimal(balanceJson);
        return balance;
    }

    @Override
    public String getBestBlockHash() throws BitcoindException, CommunicationException {
        String headerHashJson = rpcClient.execute(UsdtCommands.GET_BEST_BLOCK_HASH.getName());
        String headerHash = rpcClient.getParser().parseString(headerHashJson);
        return headerHash;
    }

    @Override
    public Block getBlock(String headerHash) throws BitcoindException, CommunicationException {
        String blockJson = rpcClient.execute(UsdtCommands.GET_BLOCK.getName(), headerHash);
        Block block = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
        return block;
    }

    @Override
    public Object getBlock(String headerHash, Boolean isDecoded) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(headerHash, isDecoded);
        String blockJson = rpcClient.execute(UsdtCommands.GET_BLOCK.getName(), params);
        if (isDecoded) {
            Block block = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
            return block;
        } else {
            String block = rpcClient.getParser().parseString(blockJson);
            return block;
        }
    }

    @Override
    public BlockChainInfo getBlockChainInfo() throws BitcoindException, CommunicationException {
        String blockChainInfoJson = rpcClient.execute(UsdtCommands.GET_BLOCK_CHAIN_INFO.getName());
        BlockChainInfo blockChainInfo = rpcClient.getMapper().mapToEntity(blockChainInfoJson,
                BlockChainInfo.class);
        return blockChainInfo;
    }

    @Override
    public Integer getBlockCount() throws BitcoindException, CommunicationException {
        String blockHeightJson = rpcClient.execute(UsdtCommands.GET_BLOCK_COUNT.getName());
        Integer blockHeight = rpcClient.getParser().parseInteger(blockHeightJson);
        return blockHeight;
    }

    @Override
    public String getBlockHash(Integer blockHeight) throws BitcoindException,
            CommunicationException {
        String headerHashJson = rpcClient.execute(UsdtCommands.GET_BLOCK_HASH.getName(), blockHeight);
        String headerHash = rpcClient.getParser().parseString(headerHashJson);
        return headerHash;
    }

    @Override
    public BlockHeader getBlockHeader(String headerHash) throws BitcoindException, CommunicationException {
        String blockJson = rpcClient.execute(UsdtCommands.GET_BLOCK_HEADER.getName(), headerHash);
        BlockHeader blockHeader = rpcClient.getMapper().mapToEntity(blockJson, Block.class);
        return blockHeader;
    }

    @Override
    public Object getBlockHeader(String headerHash, Boolean isDecoded) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(headerHash, isDecoded);
        String blockJson = rpcClient.execute(UsdtCommands.GET_BLOCK_HEADER.getName(), params);
        if (isDecoded) {
            BlockHeader blockHeader = rpcClient.getMapper().mapToEntity(blockJson, BlockHeader.class);
            return blockHeader;
        } else {
            String blockHeader = rpcClient.getParser().parseString(blockJson);
            return blockHeader;
        }
    }

    @Override
    public List<Tip> getChainTips() throws BitcoindException, CommunicationException {
        String chainTipsJson = rpcClient.execute(UsdtCommands.GET_CHAIN_TIPS.getName());
        List<Tip> chainTips = rpcClient.getMapper().mapToList(chainTipsJson, Tip.class);
        return chainTips;
    }

    @Override
    public Integer getConnectionCount() throws BitcoindException, CommunicationException {
        String connectionCountJson = rpcClient.execute(UsdtCommands.GET_CONNECTION_COUNT.getName());
        Integer connectionCount = rpcClient.getParser().parseInteger(connectionCountJson);
        return connectionCount;
    }

    @Override
    public BigDecimal getDifficulty() throws BitcoindException, CommunicationException {
        String difficultyJson = rpcClient.execute(UsdtCommands.GET_DIFFICULTY.getName());
        BigDecimal difficulty = rpcClient.getParser().parseBigDecimal(difficultyJson);
        return difficulty;
    }

    @Override
    public Boolean getGenerate() throws BitcoindException, CommunicationException {
        String isGenerateJson = rpcClient.execute(UsdtCommands.GET_GENERATE.getName());
        Boolean isGenerate = rpcClient.getParser().parseBoolean(isGenerateJson);
        return isGenerate;
    }

    @Override
    public void setGenerate(Boolean isGenerate) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.SET_GENERATE.getName(), isGenerate);
    }

    @Override
    public Long getHashesPerSec() throws BitcoindException, CommunicationException {
        String hashesPerSecJson = rpcClient.execute(UsdtCommands.GET_HASHES_PER_SEC.getName());
        Long hashesPerSec = rpcClient.getParser().parseLong(hashesPerSecJson);
        return hashesPerSec;
    }

    @Override
    @Deprecated()
    public Info getInfo() throws BitcoindException, CommunicationException {
        throw new UnsupportedOperationException("This call was removed in version 0.16.0.");
//		String infoJson = rpcClient.execute(UsdtCommands.GET_INFO.getName());
//		Info info = rpcClient.getMapper().mapToEntity(infoJson, Info.class);
//		return info;
    }

    @Override
    public MemPoolInfo getMemPoolInfo() throws BitcoindException, CommunicationException {
        String memPoolInfoJson = rpcClient.execute(UsdtCommands.GET_MEM_POOL_INFO.getName());
        MemPoolInfo memPoolInfo = rpcClient.getMapper().mapToEntity(memPoolInfoJson,
                MemPoolInfo.class);
        return memPoolInfo;
    }

    @Override
    public MiningInfo getMiningInfo() throws BitcoindException, CommunicationException {
        String miningInfoJson = rpcClient.execute(UsdtCommands.GET_MINING_INFO.getName());
        MiningInfo miningInfo = rpcClient.getMapper().mapToEntity(miningInfoJson, MiningInfo.class);
        return miningInfo;
    }

    @Override
    public NetworkTotals getNetTotals() throws BitcoindException, CommunicationException {
        String netTotalsJson = rpcClient.execute(UsdtCommands.GET_NET_TOTALS.getName());
        NetworkTotals netTotals = rpcClient.getMapper().mapToEntity(netTotalsJson,
                NetworkTotals.class);
        return netTotals;
    }

    @Override
    public BigInteger getNetworkHashPs() throws BitcoindException, CommunicationException {
        String networkHashPsJson = rpcClient.execute(UsdtCommands.GET_NETWORK_HASH_PS.getName());
        BigInteger networkHashPs = rpcClient.getParser().parseBigInteger(networkHashPsJson);
        return networkHashPs;
    }

    @Override
    public BigInteger getNetworkHashPs(Integer blocks) throws BitcoindException,
            CommunicationException {
        String networkHashPsJson = rpcClient.execute(UsdtCommands.GET_NETWORK_HASH_PS.getName(), blocks);
        BigInteger networkHashPs = rpcClient.getParser().parseBigInteger(networkHashPsJson);
        return networkHashPs;
    }

    @Override
    public BigInteger getNetworkHashPs(Integer blocks, Integer blockHeight) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(blocks, blockHeight);
        String networkHashPsJson = rpcClient.execute(UsdtCommands.GET_NETWORK_HASH_PS.getName(), params);
        BigInteger networkHashPs = rpcClient.getParser().parseBigInteger(networkHashPsJson);
        return networkHashPs;
    }

    @Override
    public NetworkInfo getNetworkInfo() throws BitcoindException, CommunicationException {
        String networkInfoJson = rpcClient.execute(UsdtCommands.GET_NETWORK_INFO.getName());
        NetworkInfo networkInfo = rpcClient.getMapper().mapToEntity(networkInfoJson,
                NetworkInfo.class);
        return networkInfo;
    }

    @Override
    public String getNewAddress() throws BitcoindException, CommunicationException {
        String addressJson = rpcClient.execute(UsdtCommands.GET_NEW_ADDRESS.getName());
        String address = rpcClient.getParser().parseString(addressJson);
        return address;
    }

    @Override
    public String getNewAddress(String label) throws BitcoindException, CommunicationException {
        String addressJson = rpcClient.execute(UsdtCommands.GET_NEW_ADDRESS.getName(), label);
        String address = rpcClient.getParser().parseString(addressJson);
        return address;
    }

    @Override
    public String getNewAddress(String label, AddressType addressType) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(label, addressType);
        String addressJson = rpcClient.execute(UsdtCommands.GET_NEW_ADDRESS.getName(), params);
        String address = rpcClient.getParser().parseString(addressJson);
        return address;
    }

    @Override
    public List<PeerNode> getPeerInfo() throws BitcoindException, CommunicationException {
        String peerInfoJson = rpcClient.execute(UsdtCommands.GET_PEER_INFO.getName());
        List<PeerNode> peerInfo = rpcClient.getMapper().mapToList(peerInfoJson, PeerNode.class);
        return peerInfo;
    }

    @Override
    public String getRawChangeAddress() throws BitcoindException, CommunicationException {
        String addressJson = rpcClient.execute(UsdtCommands.GET_RAW_CHANGE_ADDRESS.getName());
        String address = rpcClient.getParser().parseString(addressJson);
        return address;
    }

    @Override
    public List<String> getRawMemPool() throws BitcoindException, CommunicationException {
        String memPoolTxnsJson = rpcClient.execute(UsdtCommands.GET_RAW_MEM_POOL.getName());
        List<String> memPoolTxns = rpcClient.getMapper().mapToList(memPoolTxnsJson, String.class);
        return memPoolTxns;
    }

    @Override
    public List<? extends Object> getRawMemPool(Boolean isDetailed) throws BitcoindException,
            CommunicationException {
        String memPoolTxnsJson = rpcClient.execute(UsdtCommands.GET_RAW_MEM_POOL.getName(), isDetailed);
        if (isDetailed) {
            Map<String, MemPoolTransaction> memPoolTxns = rpcClient.getMapper().mapToMap(
                    memPoolTxnsJson, String.class, MemPoolTransaction.class);
            for (Map.Entry<String, MemPoolTransaction> memPoolTxn : memPoolTxns.entrySet()) {
                memPoolTxn.getValue().setTxId(memPoolTxn.getKey());
            }
            return new ArrayList<MemPoolTransaction>(memPoolTxns.values());
        } else {
            List<String> memPoolTxns = rpcClient.getMapper().mapToList(memPoolTxnsJson,
                    String.class);
            return memPoolTxns;
        }
    }

    @Override
    public String getRawTransaction(String txId) throws BitcoindException, CommunicationException {
        String hexTransactionJson = rpcClient.execute(UsdtCommands.GET_RAW_TRANSACTION.getName(), txId);
        String hexTransaction = rpcClient.getParser().parseString(hexTransactionJson);
        return hexTransaction;
    }

    @Override
    public Object getRawTransaction(String txId, Integer verbosity) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(txId, verbosity);
        String transactionJson = rpcClient.execute(UsdtCommands.GET_RAW_TRANSACTION.getName(), params);
        if (verbosity == DataFormats.HEX.getCode()) {
            String hexTransaction = rpcClient.getParser().parseString(transactionJson);
            return hexTransaction;
        } else {
            RawTransaction rawTransaction = rpcClient.getMapper().mapToEntity(transactionJson,
                    RawTransaction.class);
            return rawTransaction;
        }
    }

    @Override
    public BigDecimal getReceivedByAccount(String account) throws BitcoindException,
            CommunicationException {
        String totalReceivedJson = rpcClient.execute(UsdtCommands.GET_RECEIVED_BY_ACCOUNT.getName(),
                account);
        BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
        return totalReceived;
    }

    @Override
    public BigDecimal getReceivedByAccount(String account, Integer confirmations)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(account, confirmations);
        String totalReceivedJson = rpcClient.execute(UsdtCommands.GET_RECEIVED_BY_ACCOUNT.getName(),
                params);
        BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
        return totalReceived;
    }

    @Override
    public BigDecimal getReceivedByAddress(String address) throws BitcoindException,
            CommunicationException {
        String totalReceivedJson = rpcClient.execute(UsdtCommands.GET_RECEIVED_BY_ADDRESS.getName(),
                address);
        BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
        return totalReceived;
    }

    @Override
    public BigDecimal getReceivedByAddress(String address, Integer confirmations)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(address, confirmations);
        String totalReceivedJson = rpcClient.execute(UsdtCommands.GET_RECEIVED_BY_ADDRESS.getName(),
                params);
        BigDecimal totalReceived = rpcClient.getParser().parseBigDecimal(totalReceivedJson);
        return totalReceived;
    }

    @Override
    public Transaction getTransaction(String txId) throws BitcoindException,
            CommunicationException {
        String transactionJson = rpcClient.execute(UsdtCommands.GET_TRANSACTION.getName(), txId);
        Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
                Transaction.class);
        return transaction;
    }

    @Override
    public Transaction getTransaction(String txId, Boolean withWatchOnly) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(txId, withWatchOnly);
        String transactionJson = rpcClient.execute(UsdtCommands.GET_TRANSACTION.getName(), params);
        Transaction transaction = rpcClient.getMapper().mapToEntity(transactionJson,
                Transaction.class);
        return transaction;
    }

    @Override
    public TxOutSetInfo getTxOutSetInfo() throws BitcoindException, CommunicationException {
        String txnOutSetInfoJson = rpcClient.execute(UsdtCommands.GET_TX_OUT_SET_INFO.getName());
        TxOutSetInfo txnOutSetInfo = rpcClient.getMapper().mapToEntity(txnOutSetInfoJson,
                TxOutSetInfo.class);
        return txnOutSetInfo;
    }

    @Override
    public BigDecimal getUnconfirmedBalance() throws BitcoindException, CommunicationException {
        String unconfirmedBalanceJson = rpcClient.execute(UsdtCommands.GET_UNCONFIRMED_BALANCE.getName());
        BigDecimal unconfirmedBalance = rpcClient.getParser().parseBigDecimal(unconfirmedBalanceJson);
        return unconfirmedBalance;
    }

    @Override
    public WalletInfo getWalletInfo() throws BitcoindException, CommunicationException {
        String walletInfoJson = rpcClient.execute(UsdtCommands.GET_WALLET_INFO.getName());
        WalletInfo walletInfo = rpcClient.getMapper().mapToEntity(walletInfoJson, WalletInfo.class);
        return walletInfo;
    }

    @Override
    public String help() throws BitcoindException, CommunicationException {
        String helpJson = rpcClient.execute(UsdtCommands.HELP.getName());
        String help = rpcClient.getParser().parseString(helpJson);
        return help;
    }

    @Override
    public String help(String command) throws BitcoindException, CommunicationException {
        String helpJson = rpcClient.execute(UsdtCommands.HELP.getName(), command);
        String help = rpcClient.getParser().parseString(helpJson);
        return help;
    }

    @Override
    public void importAddress(String address) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.IMPORT_ADDRESS.getName(), address);
    }

    @Override
    public void importAddress(String address, String account) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(address, account);
        rpcClient.execute(UsdtCommands.IMPORT_ADDRESS.getName(), params);
    }

    @Override
    public void importAddress(String address, String account, Boolean withRescan)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(address, account, withRescan);
        rpcClient.execute(UsdtCommands.IMPORT_ADDRESS.getName(), params);
    }

    @Override
    public void importPrivKey(String privateKey) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.IMPORT_PRIV_KEY.getName(), privateKey);
    }

    @Override
    public void importPrivKey(String privateKey, String label) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(privateKey, label);
        rpcClient.execute(UsdtCommands.IMPORT_PRIV_KEY.getName(), params);
    }

    @Override
    public void importPrivKey(String privateKey, String label, Boolean withRescan)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(privateKey, label, withRescan);
        rpcClient.execute(UsdtCommands.IMPORT_PRIV_KEY.getName(), params);
    }

    @Override
    public void importWallet(String filePath) throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.IMPORT_WALLET.getName(), filePath);
    }

    @Override
    public void keyPoolRefill() throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.KEY_POOL_REFILL.getName());
    }

    @Override
    public void keyPoolRefill(Integer keypoolSize) throws BitcoindException,
            CommunicationException {
        rpcClient.execute(UsdtCommands.KEY_POOL_REFILL.getName(), keypoolSize);
    }

    @Override
    public Map<String, BigDecimal> listAccounts() throws BitcoindException, CommunicationException {
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_ACCOUNTS.getName());
        Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson,
                String.class, BigDecimal.class);
        accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
        return accounts;
    }

    @Override
    public List<String> listLabels() throws BitcoindException, CommunicationException {
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_LABELS.getName());
        List<String> lables = rpcClient.getMapper().mapToList(accountsJson, String.class);
        return lables;
    }

    @Override
    public Map<String, BigDecimal> listAccounts(Integer confirmations) throws BitcoindException,
            CommunicationException {
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_ACCOUNTS.getName(), confirmations);
        Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson,
                String.class, BigDecimal.class);
        accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
        return accounts;
    }

    @Override
    public Map<String, BigDecimal> listAccounts(Integer confirmations, Boolean withWatchOnly)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(confirmations, withWatchOnly);
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_ACCOUNTS.getName(), params);
        Map<String, BigDecimal> accounts = rpcClient.getMapper().mapToMap(accountsJson,
                String.class, BigDecimal.class);
        accounts = NumberUtils.setValueScale(accounts, Defaults.DECIMAL_SCALE);
        return accounts;
    }

    @Override
    public List<List<AddressOverview>> listAddressGroupings() throws BitcoindException,
            CommunicationException {
        String groupingsJson = rpcClient.execute(UsdtCommands.LIST_ADDRESS_GROUPINGS.getName());
        List<List<AddressOverview>> groupings = rpcClient.getMapper().mapToNestedLists(1,
                groupingsJson, AddressOverview.class);
        return groupings;
    }

    @Override
    public List<OutputOverview> listLockUnspent() throws BitcoindException, CommunicationException {
        String lockedOutputsJson = rpcClient.execute(UsdtCommands.LIST_LOCK_UNSPENT.getName());
        List<OutputOverview> lockedOutputs = rpcClient.getMapper().mapToList(lockedOutputsJson,
                OutputOverview.class);
        return lockedOutputs;
    }

    @Override
    public List<Account> listReceivedByAccount() throws BitcoindException, CommunicationException {
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ACCOUNT.getName());
        List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
        return accounts;
    }

    @Override
    public List<Account> listReceivedByAccount(Integer confirmations) throws BitcoindException,
            CommunicationException {
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ACCOUNT.getName(),
                confirmations);
        List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
        return accounts;
    }

    @Override
    public List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(confirmations, withUnused);
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ACCOUNT.getName(),
                params);
        List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
        return accounts;
    }

    @Override
    public List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused,
                                               Boolean withWatchOnly) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(confirmations, withUnused, withWatchOnly);
        String accountsJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ACCOUNT.getName(),
                params);
        List<Account> accounts = rpcClient.getMapper().mapToList(accountsJson, Account.class);
        return accounts;
    }

    @Override
    public List<Address> listReceivedByAddress() throws BitcoindException, CommunicationException {
        String addressesJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ADDRESS.getName());
        List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
        return addresses;
    }

    @Override
    public List<Address> listReceivedByAddress(Integer confirmations) throws BitcoindException,
            CommunicationException {
        String addressesJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ADDRESS.getName(),
                confirmations);
        List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
        return addresses;
    }

    @Override
    public List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(confirmations, withUnused);
        String addressesJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ADDRESS.getName(),
                params);
        List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
        return addresses;
    }

    @Override
    public List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused,
                                               Boolean withWatchOnly) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(confirmations, withUnused, withWatchOnly);
        String addressesJson = rpcClient.execute(UsdtCommands.LIST_RECEIVED_BY_ADDRESS.getName(),
                params);
        List<Address> addresses = rpcClient.getMapper().mapToList(addressesJson, Address.class);
        return addresses;
    }

    @Override
    public SinceBlock listSinceBlock() throws BitcoindException, CommunicationException {
        String sinceBlockJson = rpcClient.execute(UsdtCommands.LIST_SINCE_BLOCK.getName());
        SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
        return sinceBlock;
    }

    @Override
    public SinceBlock listSinceBlock(String headerHash) throws BitcoindException,
            CommunicationException {
        String sinceBlockJson = rpcClient.execute(UsdtCommands.LIST_SINCE_BLOCK.getName(), headerHash);
        SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
        return sinceBlock;
    }

    @Override
    public SinceBlock listSinceBlock(String headerHash, Integer confirmations)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(headerHash, confirmations);
        String sinceBlockJson = rpcClient.execute(UsdtCommands.LIST_SINCE_BLOCK.getName(), params);
        SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
        return sinceBlock;
    }

    @Override
    public SinceBlock listSinceBlock(String headerHash, Integer confirmations,
                                     Boolean withWatchOnly) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(headerHash, confirmations, withWatchOnly);
        String sinceBlockJson = rpcClient.execute(UsdtCommands.LIST_SINCE_BLOCK.getName(), params);
        SinceBlock sinceBlock = rpcClient.getMapper().mapToEntity(sinceBlockJson, SinceBlock.class);
        return sinceBlock;
    }

    @Override
    public List<Payment> listTransactions() throws BitcoindException, CommunicationException {
        String paymentsJson = rpcClient.execute(UsdtCommands.LIST_TRANSACTIONS.getName());
        List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
        return payments;
    }

    @Override
    public List<Payment> listTransactions(String account) throws BitcoindException,
            CommunicationException {
        String paymentsJson = rpcClient.execute(UsdtCommands.LIST_TRANSACTIONS.getName(), account);
        List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
        return payments;
    }

    @Override
    public List<Payment> listTransactions(String account, Integer count) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(account, count);
        String paymentsJson = rpcClient.execute(UsdtCommands.LIST_TRANSACTIONS.getName(), params);
        List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
        return payments;
    }

    @Override
    public List<Payment> listTransactions(String account, Integer count, Integer offset)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(account, count, offset);
        String paymentsJson = rpcClient.execute(UsdtCommands.LIST_TRANSACTIONS.getName(), params);
        List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
        return payments;
    }

    @Override
    public List<Payment> listTransactions(String account, Integer count, Integer offset,
                                          Boolean withWatchOnly) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(account, count, offset, withWatchOnly);
        String paymentsJson = rpcClient.execute(UsdtCommands.LIST_TRANSACTIONS.getName(), params);
        List<Payment> payments = rpcClient.getMapper().mapToList(paymentsJson, Payment.class);
        return payments;
    }

    @Override
    public List<Output> listUnspent() throws BitcoindException, CommunicationException {
        String unspentOutputsJson = rpcClient.execute(UsdtCommands.LIST_UNSPENT.getName());
        List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
                Output.class);
        return unspentOutputs;
    }

    @Override
    public List<Output> listUnspent(Integer minConfirmations) throws BitcoindException,
            CommunicationException {
        String unspentOutputsJson = rpcClient.execute(UsdtCommands.LIST_UNSPENT.getName(),
                minConfirmations);
        List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
                Output.class);
        return unspentOutputs;
    }

    @Override
    public List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(minConfirmations, maxConfirmations);
        String unspentOutputsJson = rpcClient.execute(UsdtCommands.LIST_UNSPENT.getName(), params);
        List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
                Output.class);
        return unspentOutputs;
    }

    @Override
    public List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations,
                                    List<String> addresses) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(minConfirmations, maxConfirmations, addresses);
        String unspentOutputsJson = rpcClient.execute(UsdtCommands.LIST_UNSPENT.getName(), params);
        List<Output> unspentOutputs = rpcClient.getMapper().mapToList(unspentOutputsJson,
                Output.class);
        return unspentOutputs;
    }

    @Override
    public Boolean lockUnspent(Boolean isUnlocked) throws BitcoindException,
            CommunicationException {
        String isSuccessJson = rpcClient.execute(UsdtCommands.LOCK_UNSPENT.getName(), isUnlocked);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public Boolean lockUnspent(Boolean isUnlocked, List<OutputOverview> outputs)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(isUnlocked, outputs);
        String isSuccessJson = rpcClient.execute(UsdtCommands.LOCK_UNSPENT.getName(), params);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public Boolean move(String fromAccount, String toAccount, BigDecimal amount)
            throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAccount, amount);
        String isSuccessJson = rpcClient.execute(UsdtCommands.MOVE.getName(), params);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public Boolean move(String fromAccount, String toAccount, BigDecimal amount, Integer dummy,
                        String comment) throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAccount, amount, dummy, comment);
        String isSuccessJson = rpcClient.execute(UsdtCommands.MOVE.getName(), params);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public void ping() throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.PING.getName());
    }

    @Override
    public Boolean prioritiseTransaction(String txId, BigDecimal deltaPriority, Long deltaFee)
            throws BitcoindException, CommunicationException {
        deltaPriority = deltaPriority.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(txId, deltaPriority, deltaFee);
        String isSuccessJson = rpcClient.execute(UsdtCommands.PRIORITISE_TRANSACTION.getName(), params);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount)
            throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_FROM.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount,
                           Integer confirmations) throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_FROM.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount,
                           Integer confirmations, String comment) throws BitcoindException,
            CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations,
                comment);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_FROM.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendFrom(String fromAccount, String toAddress, BigDecimal amount,
                           Integer confirmations, String comment, String commentTo) throws BitcoindException,
            CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddress, amount, confirmations,
                comment, commentTo);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_FROM.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses)
            throws BitcoindException, CommunicationException {
        toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddresses);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_MANY.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses,
                           Integer confirmations) throws BitcoindException, CommunicationException {
        toAddresses = NumberUtils.setValueScale(toAddresses, Defaults.DECIMAL_SCALE);
        List<Object> params = CollectionUtils.asList(fromAccount, toAddresses, confirmations);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_MANY.getName(), params);
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
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_MANY.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendRawTransaction(String hexTransaction) throws BitcoindException,
            CommunicationException {
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_RAW_TRANSACTION.getName(),
                hexTransaction);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendRawTransaction(String hexTransaction, Boolean withHighFees)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(hexTransaction, withHighFees);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_RAW_TRANSACTION.getName(),
                params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException,
            CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(toAddress, amount);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_TO_ADDRESS.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendToAddress(String toAddress, BigDecimal amount, String comment)
            throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(toAddress, amount, comment);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_TO_ADDRESS.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public String sendToAddress(String toAddress, BigDecimal amount, String comment,
                                String commentTo) throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(toAddress, amount, comment, commentTo);
        String transactionIdJson = rpcClient.execute(UsdtCommands.SEND_TO_ADDRESS.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    @Override
    public void setAccount(String address, String account) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(address, account);
        rpcClient.execute(UsdtCommands.SET_ACCOUNT.getName(), params);
    }

    @Override
    public void setGenerate(Boolean isGenerate, Integer processors) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(isGenerate, processors);
        rpcClient.execute(UsdtCommands.SET_GENERATE.getName(), params);
    }

    @Override
    public Boolean setTxFee(BigDecimal txFee) throws BitcoindException, CommunicationException {
        txFee = txFee.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        String isSuccessJson = rpcClient.execute(UsdtCommands.SET_TX_FEE.getName(), txFee);
        Boolean isSuccess = rpcClient.getParser().parseBoolean(isSuccessJson);
        return isSuccess;
    }

    @Override
    public String signMessage(String address, String message) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(address, message);
        String signatureJson = rpcClient.execute(UsdtCommands.SIGN_MESSAGE.getName(), params);
        String signature = rpcClient.getParser().parseString(signatureJson);
        return signature;
    }

    @Override
    public SignatureResult signRawTransaction(String hexTransaction) throws BitcoindException,
            CommunicationException {
        String signatureResultJson = rpcClient.execute(UsdtCommands.SIGN_RAW_TRANSACTION.getName(),
                hexTransaction);
        SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson,
                SignatureResult.class);
        return signatureResult;
    }

    @Override
    public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(hexTransaction, outputs);
        String signatureResultJson = rpcClient.execute(UsdtCommands.SIGN_RAW_TRANSACTION.getName(),
                params);
        SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson,
                SignatureResult.class);
        return signatureResult;
    }

    @Override
    public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs,
                                              List<String> privateKeys) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(hexTransaction, outputs, privateKeys);
        String signatureResultJson = rpcClient.execute(UsdtCommands.SIGN_RAW_TRANSACTION.getName(),
                params);
        SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson,
                SignatureResult.class);
        return signatureResult;
    }

    @Override
    public SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs,
                                              List<String> privateKeys, String sigHashType) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(hexTransaction, outputs, privateKeys,
                sigHashType);
        String signatureResultJson = rpcClient.execute(UsdtCommands.SIGN_RAW_TRANSACTION.getName(),
                params);
        SignatureResult signatureResult = rpcClient.getMapper().mapToEntity(signatureResultJson,
                SignatureResult.class);
        return signatureResult;
    }

    @Override
    public String stop() throws BitcoindException, CommunicationException {
        String noticeMsgJson = rpcClient.execute(UsdtCommands.STOP.getName());
        String noticeMsg = rpcClient.getParser().parseString(noticeMsgJson);
        return noticeMsg;
    }

    @Override
    public String submitBlock(String block) throws BitcoindException, CommunicationException {
        String blockStatusJson = rpcClient.execute(UsdtCommands.SUBMIT_BLOCK.getName(), block);
        String blockStatus = rpcClient.getParser().parseString(blockStatusJson);
        return blockStatus;
    }

    @Override
    public String submitBlock(String block, Map<String, Object> extraParameters)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(block, extraParameters);
        String blockStatusJson = rpcClient.execute(UsdtCommands.SUBMIT_BLOCK.getName(), params);
        String blockStatus = rpcClient.getParser().parseString(blockStatusJson);
        return blockStatus;
    }

    @Override
    public AddressInfo validateAddress(String address) throws BitcoindException,
            CommunicationException {
        String addressInfoJson = rpcClient.execute(UsdtCommands.VALIDATE_ADDRESS.getName(), address);
        AddressInfo addressInfo = rpcClient.getMapper().mapToEntity(addressInfoJson,
                AddressInfo.class);
        return addressInfo;
    }

    @Override
    public Boolean verifyChain() throws BitcoindException, CommunicationException {
        String isChainValidJson = rpcClient.execute(UsdtCommands.VERIFY_CHAIN.getName());
        Boolean isChainValid = rpcClient.getParser().parseBoolean(isChainValidJson);
        return isChainValid;
    }

    @Override
    public Boolean verifyChain(Integer checkLevel) throws BitcoindException, CommunicationException {
        String isChainValidJson = rpcClient.execute(UsdtCommands.VERIFY_CHAIN.getName(), checkLevel);
        Boolean isChainValid = rpcClient.getParser().parseBoolean(isChainValidJson);
        return isChainValid;
    }

    @Override
    public Boolean verifyChain(Integer checkLevel, Integer blocks) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(checkLevel, blocks);
        String isChainValidJson = rpcClient.execute(UsdtCommands.VERIFY_CHAIN.getName(), params);
        Boolean isChainValid = rpcClient.getParser().parseBoolean(isChainValidJson);
        return isChainValid;
    }

    @Override
    public Boolean verifyMessage(String address, String signature, String message)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(address, signature, message);
        String isSigValidJson = rpcClient.execute(UsdtCommands.VERIFY_MESSAGE.getName(), params);
        Boolean isSigValid = rpcClient.getParser().parseBoolean(isSigValidJson);
        return isSigValid;
    }

    @Override
    public void walletLock() throws BitcoindException, CommunicationException {
        rpcClient.execute(UsdtCommands.WALLET_LOCK.getName());
    }

    @Override
    public void walletPassphrase(String passphrase, Integer authTimeout) throws BitcoindException,
            CommunicationException {
        List<Object> params = CollectionUtils.asList(passphrase, authTimeout);
        rpcClient.execute(UsdtCommands.WALLET_PASSPHRASE.getName(), params);
    }

    @Override
    public void walletPassphraseChange(String curPassphrase, String newPassphrase)
            throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(curPassphrase, newPassphrase);
        rpcClient.execute(UsdtCommands.WALLET_PASSPHRASE_CHANGE.getName(), params);
    }

    @Override
    public Properties getNodeConfig() {
        return configurator.getNodeConfig();
    }

    @Override
    public String getNodeVersion() {
        return configurator.getNodeVersion();
    }

    @Override
    public synchronized void close() {
        LOG.info(">> close(..): closing the 'bitcoind' core wrapper");
        rpcClient.close();
    }

    @Override
    public BigDecimal omniGetBalance(String address, Integer propertyId) throws BitcoindException, CommunicationException {
        List<Object> params = CollectionUtils.asList(address, propertyId);
        String balanceJson = rpcClient.execute(UsdtCommands.OMNI_GETBALANCE.getName(), params);
        OmniBalance omniBalance = rpcClient.getMapper().mapToEntity(balanceJson,
                OmniBalance.class);
        return omniBalance.getBalance();
    }

    @Override
    public OmniTransaction omniGetTransaction(String txId) throws BitcoindException, CommunicationException {
        String transaction = rpcClient.execute(UsdtCommands.OMNI_GETTRANSACTION.getName(), txId);
        OmniTransaction omniTransaction = rpcClient.getMapper().mapToEntity(transaction,
                OmniTransaction.class);
        return omniTransaction;
    }

    @Override
    public String omniSend(String fromAddress, String toAddress, Integer propertyId, BigDecimal amount) throws BitcoindException, CommunicationException {
        amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
        List<Object> params = CollectionUtils.asList(fromAddress, toAddress, propertyId, amount);
        String transactionIdJson = rpcClient.execute(UsdtCommands.OMNI_SEND.getName(), params);
        String transactionId = rpcClient.getParser().parseString(transactionIdJson);
        return transactionId;
    }

    private void initialize() {
        LOG.info(">> initialize(..): initiating the 'bitcoind' core wrapper");
        configurator = new ClientConfigurator();
    }
}