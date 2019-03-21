package com.neemre.btcdcli4j.core.client;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
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
import com.neemre.btcdcli4j.core.domain.MiningInfo;
import com.neemre.btcdcli4j.core.domain.MultiSigAddress;
import com.neemre.btcdcli4j.core.domain.NetworkInfo;
import com.neemre.btcdcli4j.core.domain.NetworkTotals;
import com.neemre.btcdcli4j.core.domain.OmniTransaction;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.Payment;
import com.neemre.btcdcli4j.core.domain.PeerNode;
import com.neemre.btcdcli4j.core.domain.RawTransactionOverview;
import com.neemre.btcdcli4j.core.domain.RedeemScript;
import com.neemre.btcdcli4j.core.domain.SignatureResult;
import com.neemre.btcdcli4j.core.domain.SinceBlock;
import com.neemre.btcdcli4j.core.domain.Tip;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.domain.TxOutSetInfo;
import com.neemre.btcdcli4j.core.domain.WalletInfo;
import com.neemre.btcdcli4j.core.domain.enums.AddressType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface UsdtClient {

    String addMultiSigAddress(Integer minSignatures, List<String> addresses)
            throws BitcoindException, CommunicationException;

    String addMultiSigAddress(Integer minSignatures, List<String> addresses, String account)
            throws BitcoindException, CommunicationException;

    void addNode(String node, String command) throws BitcoindException, CommunicationException;

    void backupWallet(String filePath) throws BitcoindException, CommunicationException;

    MultiSigAddress createMultiSig(Integer minSignatures, List<String> addresses)
            throws BitcoindException, CommunicationException;

    String createRawTransaction(List<OutputOverview> outputs, Map<String, BigDecimal> toAddresses)
            throws BitcoindException, CommunicationException;

    RawTransactionOverview decodeRawTransaction(String hexTransaction) throws BitcoindException,
            CommunicationException;

    RedeemScript decodeScript(String hexRedeemScript) throws BitcoindException,
            CommunicationException;

    String dumpPrivKey(String address) throws BitcoindException, CommunicationException;

    void dumpWallet(String filePath) throws BitcoindException, CommunicationException;

    String encryptWallet(String passphrase) throws BitcoindException, CommunicationException;

    BigDecimal estimateFee(Integer maxBlocks) throws BitcoindException, CommunicationException;

    BigDecimal estimatePriority(Integer maxBlocks) throws BitcoindException, CommunicationException;

    String getAccount(String address) throws BitcoindException, CommunicationException;

    String getAccountAddress(String account) throws BitcoindException, CommunicationException;

    List<AddedNode> getAddedNodeInfo(Boolean withDetails) throws BitcoindException,
            CommunicationException;

    List<AddedNode> getAddedNodeInfo(Boolean withDetails, String node) throws BitcoindException,
            CommunicationException;

    List<String> getAddressesByAccount(String account) throws BitcoindException,
            CommunicationException;

    BigDecimal getBalance() throws BitcoindException, CommunicationException;

    BigDecimal getBalance(String account) throws BitcoindException, CommunicationException;

    BigDecimal getBalance(String account, Integer confirmations) throws BitcoindException,
            CommunicationException;

    BigDecimal getBalance(String account, Integer confirmations, Boolean withWatchOnly)
            throws BitcoindException, CommunicationException;

    String getBestBlockHash() throws BitcoindException, CommunicationException;

    Block getBlock(String headerHash) throws BitcoindException, CommunicationException;

    Object getBlock(String headerHash, Boolean isDecoded) throws BitcoindException,
            CommunicationException;

    BlockChainInfo getBlockChainInfo() throws BitcoindException, CommunicationException;

    Integer getBlockCount() throws BitcoindException, CommunicationException;

    String getBlockHash(Integer blockHeight) throws BitcoindException, CommunicationException;

    BlockHeader getBlockHeader(String headerHash) throws BitcoindException, CommunicationException;

    Object getBlockHeader(String headerHash, Boolean isDecoded) throws BitcoindException, CommunicationException;

    List<Tip> getChainTips() throws BitcoindException, CommunicationException;

    Integer getConnectionCount() throws BitcoindException, CommunicationException;

    BigDecimal getDifficulty() throws BitcoindException, CommunicationException;

    Boolean getGenerate() throws BitcoindException, CommunicationException;

    void setGenerate(Boolean isGenerate) throws BitcoindException, CommunicationException;

    Long getHashesPerSec() throws BitcoindException, CommunicationException;

    Info getInfo() throws BitcoindException, CommunicationException;

    MemPoolInfo getMemPoolInfo() throws BitcoindException, CommunicationException;

    MiningInfo getMiningInfo() throws BitcoindException, CommunicationException;

    NetworkTotals getNetTotals() throws BitcoindException, CommunicationException;

    BigInteger getNetworkHashPs() throws BitcoindException, CommunicationException;

    BigInteger getNetworkHashPs(Integer blocks) throws BitcoindException, CommunicationException;

    BigInteger getNetworkHashPs(Integer blocks, Integer blockHeight) throws BitcoindException,
            CommunicationException;

    NetworkInfo getNetworkInfo() throws BitcoindException, CommunicationException;

    String getNewAddress() throws BitcoindException, CommunicationException;

    String getNewAddress(String label) throws BitcoindException, CommunicationException;

    String getNewAddress(String label, AddressType addressType) throws BitcoindException, CommunicationException;

    List<PeerNode> getPeerInfo() throws BitcoindException, CommunicationException;

    String getRawChangeAddress() throws BitcoindException, CommunicationException;

    List<String> getRawMemPool() throws BitcoindException, CommunicationException;

    List<? extends Object> getRawMemPool(Boolean isDetailed) throws BitcoindException,
            CommunicationException;

    String getRawTransaction(String txId) throws BitcoindException, CommunicationException;

    Object getRawTransaction(String txId, Integer verbosity) throws BitcoindException,
            CommunicationException;

    BigDecimal getReceivedByAccount(String account) throws BitcoindException,
            CommunicationException;

    BigDecimal getReceivedByAccount(String account, Integer confirmations) throws BitcoindException,
            CommunicationException;

    BigDecimal getReceivedByAddress(String address) throws BitcoindException, CommunicationException;

    BigDecimal getReceivedByAddress(String address, Integer confirmations) throws BitcoindException,
            CommunicationException;

    Transaction getTransaction(String txId) throws BitcoindException, CommunicationException;

    Transaction getTransaction(String txId, Boolean withWatchOnly) throws BitcoindException,
            CommunicationException;

    TxOutSetInfo getTxOutSetInfo() throws BitcoindException, CommunicationException;

    BigDecimal getUnconfirmedBalance() throws BitcoindException, CommunicationException;

    WalletInfo getWalletInfo() throws BitcoindException, CommunicationException;

    String help() throws BitcoindException, CommunicationException;

    String help(String command) throws BitcoindException, CommunicationException;

    void importAddress(String address) throws BitcoindException, CommunicationException;

    void importAddress(String address, String account) throws BitcoindException,
            CommunicationException;

    void importAddress(String address, String account, Boolean withRescan) throws BitcoindException,
            CommunicationException;

    void importPrivKey(String privateKey) throws BitcoindException, CommunicationException;

    void importPrivKey(String privateKey, String label) throws BitcoindException,
            CommunicationException;

    void importPrivKey(String privateKey, String label, Boolean withRescan)
            throws BitcoindException, CommunicationException;

    void importWallet(String filePath) throws BitcoindException, CommunicationException;

    void keyPoolRefill() throws BitcoindException, CommunicationException;

    void keyPoolRefill(Integer keypoolSize) throws BitcoindException, CommunicationException;

    /**
     * @return
     * @throws BitcoindException
     * @throws CommunicationException
     * @deprecated "listaccounts is deprecated and will be removed in V0.18."
     */
    @Deprecated()
    Map<String, BigDecimal> listAccounts() throws BitcoindException, CommunicationException;

    List<String> listLabels() throws BitcoindException, CommunicationException;

    Map<String, BigDecimal> listAccounts(Integer confirmations) throws BitcoindException,
            CommunicationException;

    Map<String, BigDecimal> listAccounts(Integer confirmations, Boolean withWatchOnly)
            throws BitcoindException, CommunicationException;

    @Deprecated
    List<List<AddressOverview>> listAddressGroupings() throws BitcoindException,
            CommunicationException;

    List<OutputOverview> listLockUnspent() throws BitcoindException, CommunicationException;

    List<Account> listReceivedByAccount() throws BitcoindException, CommunicationException;

    List<Account> listReceivedByAccount(Integer confirmations) throws BitcoindException,
            CommunicationException;

    List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused)
            throws BitcoindException, CommunicationException;

    List<Account> listReceivedByAccount(Integer confirmations, Boolean withUnused,
                                        Boolean withWatchOnly) throws BitcoindException, CommunicationException;

    List<Address> listReceivedByAddress() throws BitcoindException, CommunicationException;

    List<Address> listReceivedByAddress(Integer confirmations) throws BitcoindException,
            CommunicationException;

    List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused)
            throws BitcoindException, CommunicationException;

    List<Address> listReceivedByAddress(Integer confirmations, Boolean withUnused,
                                        Boolean withWatchOnly) throws BitcoindException, CommunicationException;

    SinceBlock listSinceBlock() throws BitcoindException, CommunicationException;

    SinceBlock listSinceBlock(String headerHash) throws BitcoindException, CommunicationException;

    SinceBlock listSinceBlock(String headerHash, Integer confirmations) throws BitcoindException,
            CommunicationException;

    SinceBlock listSinceBlock(String headerHash, Integer confirmations, Boolean withWatchOnly)
            throws BitcoindException, CommunicationException;

    List<Payment> listTransactions() throws BitcoindException, CommunicationException;

    List<Payment> listTransactions(String account) throws BitcoindException, CommunicationException;

    List<Payment> listTransactions(String account, Integer count) throws BitcoindException,
            CommunicationException;

    List<Payment> listTransactions(String account, Integer count, Integer offset)
            throws BitcoindException, CommunicationException;

    List<Payment> listTransactions(String account, Integer count, Integer offset,
                                   Boolean withWatchOnly) throws BitcoindException, CommunicationException;

    List<Output> listUnspent() throws BitcoindException, CommunicationException;

    List<Output> listUnspent(Integer minConfirmations) throws BitcoindException,
            CommunicationException;

    List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations)
            throws BitcoindException, CommunicationException;

    List<Output> listUnspent(Integer minConfirmations, Integer maxConfirmations,
                             List<String> addresses) throws BitcoindException, CommunicationException;

    Boolean lockUnspent(Boolean isUnlocked) throws BitcoindException, CommunicationException;

    Boolean lockUnspent(Boolean isUnlocked, List<OutputOverview> outputs) throws BitcoindException,
            CommunicationException;

    Boolean move(String fromAccount, String toAccount, BigDecimal amount) throws BitcoindException,
            CommunicationException;

    Boolean move(String fromAccount, String toAccount, BigDecimal amount, Integer dummy,
                 String comment) throws BitcoindException, CommunicationException;

    void ping() throws BitcoindException, CommunicationException;

    Boolean prioritiseTransaction(String txId, BigDecimal deltaPriority, Long deltaFee)
            throws BitcoindException, CommunicationException;

    String sendFrom(String fromAccount, String toAddress, BigDecimal amount)
            throws BitcoindException, CommunicationException;

    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations)
            throws BitcoindException, CommunicationException;

    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations,
                    String comment) throws BitcoindException, CommunicationException;

    String sendFrom(String fromAccount, String toAddress, BigDecimal amount, Integer confirmations,
                    String comment, String commentTo) throws BitcoindException, CommunicationException;

    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses)
            throws BitcoindException, CommunicationException;

    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations)
            throws BitcoindException, CommunicationException;

    String sendMany(String fromAccount, Map<String, BigDecimal> toAddresses, Integer confirmations,
                    String comment) throws BitcoindException, CommunicationException;

    String sendRawTransaction(String hexTransaction) throws BitcoindException,
            CommunicationException;

    String sendRawTransaction(String hexTransaction, Boolean withHighFees) throws BitcoindException,
            CommunicationException;

    String sendToAddress(String toAddress, BigDecimal amount) throws BitcoindException,
            CommunicationException;

    String sendToAddress(String toAddress, BigDecimal amount, String comment)
            throws BitcoindException, CommunicationException;

    String sendToAddress(String toAddress, BigDecimal amount, String comment, String commentTo)
            throws BitcoindException, CommunicationException;

    void setAccount(String address, String account) throws BitcoindException,
            CommunicationException;

    void setGenerate(Boolean isGenerate, Integer processors) throws BitcoindException,
            CommunicationException;

    Boolean setTxFee(BigDecimal txFee) throws BitcoindException, CommunicationException;

    String signMessage(String address, String message) throws BitcoindException,
            CommunicationException;

    SignatureResult signRawTransaction(String hexTransaction) throws BitcoindException,
            CommunicationException;

    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs)
            throws BitcoindException, CommunicationException;

    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs,
                                       List<String> privateKeys) throws BitcoindException, CommunicationException;

    SignatureResult signRawTransaction(String hexTransaction, List<Output> outputs,
                                       List<String> privateKeys, String sigHashType) throws BitcoindException,
            CommunicationException;

    String stop() throws BitcoindException, CommunicationException;

    String submitBlock(String block) throws BitcoindException, CommunicationException;

    String submitBlock(String block, Map<String, Object> extraParameters) throws BitcoindException,
            CommunicationException;

    AddressInfo validateAddress(String address) throws BitcoindException, CommunicationException;

    Boolean verifyChain() throws BitcoindException, CommunicationException;

    Boolean verifyChain(Integer checkLevel) throws BitcoindException, CommunicationException;

    Boolean verifyChain(Integer checkLevel, Integer blocks) throws BitcoindException,
            CommunicationException;

    Boolean verifyMessage(String address, String signature, String message)
            throws BitcoindException, CommunicationException;

    void walletLock() throws BitcoindException, CommunicationException;

    void walletPassphrase(String passphrase, Integer authTimeout) throws BitcoindException,
            CommunicationException;

    void walletPassphraseChange(String curPassphrase, String newPassphrase)
            throws BitcoindException, CommunicationException;

    Properties getNodeConfig();

    String getNodeVersion();

    void close();

    BigDecimal omniGetBalance(String address, Integer propertyId) throws BitcoindException, CommunicationException;

    OmniTransaction omniGetTransaction(String txId) throws BitcoindException, CommunicationException;

    String omniSend(String fromAddress, String toAddress, Integer propertyId, BigDecimal amount) throws BitcoindException, CommunicationException;

    String omniFundedSend(String fromAddress, String toAddress, Integer propertyId, BigDecimal amount, String feeAddress) throws BitcoindException, CommunicationException;
}
