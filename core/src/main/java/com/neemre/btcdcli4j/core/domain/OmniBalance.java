package com.neemre.btcdcli4j.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
 * @author Zihao Wang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OmniBalance {
    @Setter(AccessLevel.NONE)
    private BigDecimal balance;
    @Setter(AccessLevel.NONE)
    private BigDecimal reserved;
    @Setter(AccessLevel.NONE)
    private BigDecimal frozen;

    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }

    public void setReserved(BigDecimal reserved) {
        this.reserved = reserved.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen.setScale(Defaults.DECIMAL_SCALE, Defaults.ROUNDING_MODE);
    }
}