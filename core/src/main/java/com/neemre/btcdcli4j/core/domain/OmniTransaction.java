package com.neemre.btcdcli4j.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.neemre.btcdcli4j.core.common.Defaults;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author: Zihao Wang
 * @Date: 2019/3/18 14:27
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmniTransaction {
    @JsonProperty("txid")
    private String txId;
    @JsonProperty("sendingaddress")
    private String sendingAddress;
    @JsonProperty("referenceaddress")
    private String referenceAddress;
    @Setter(AccessLevel.NONE)
    private BigDecimal fee;
    @JsonProperty("ismine")
    private Boolean isMine;
    private Integer version;
    @JsonProperty("type_int")
    private Integer typeInt;
    private String type;
    @JsonProperty("propertyid")
    private Integer propertyId;
    private Boolean divisible;
    @Setter(AccessLevel.NONE)
    private BigDecimal amount;
    private Boolean valid;
    @JsonProperty("blockhash")
    private String blockHash;
    @JsonProperty("blocktime")
    private Long blockTime;
    @JsonProperty("positioninblock")
    private Integer positionInBlock;
    private Integer block;
    private Integer confirmations;

    public void setFee(BigDecimal fee) {
        this.fee = fee.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}
