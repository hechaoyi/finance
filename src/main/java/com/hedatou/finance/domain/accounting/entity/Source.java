package com.hedatou.finance.domain.accounting.entity;

import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.hedatou.finance.infrastructure.entity.BaseEntity;

/**
 * 实现描述：会计：资源，最小单位
 * 资产 + 支出 = 负债 + 权益 + 收入
 * Asset + Expense = Liability + Equity + Revenue
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午3:02:11
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_source")
public class Source extends BaseEntity {

    public enum SourceType {
        asset, // 资产，有增有减
        expense, // 支出，只增不减
        liability, // 债务，有增有减
        equity, // 权益，有增有减
        revenue; // 收入，只增不减

        public boolean isDebit() {
            return this == asset || this == expense;
        }

        public boolean isCredit() {
            return this == liability || this == equity || this == revenue;
        }
    }

    public enum SourceStatus {
        present, deceased
    }

    // ----------------------------------------
    // ----------------------------------------

    @Enumerated(EnumType.STRING)
    @NotNull
    private SourceType type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SourceStatus status;

    @Length(max = 20)
    private String name;

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    // ----------------------------------------
    // ----------------------------------------

    public Source(SourceType type, String name, BigDecimal amount) {
        this.type = type;
        this.status = SourceStatus.present;
        this.name = name;
        this.amount = amount;
    }

    public Source(SourceType type, BigDecimal amount) {
        this(type, null, amount);
    }

    protected Source() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public SourceType getType() {
        return type;
    }

    public SourceStatus getStatus() {
        return status;
    }

    public void setStatus(SourceStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
