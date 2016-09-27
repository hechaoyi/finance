package com.hedatou.finance.domain.accounting.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hedatou.finance.infrastructure.entity.BaseEntity;

/**
 * 实现描述：会计：资源之间的置换关系
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午3:00:50
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_equation")
public class Equation extends BaseEntity {

    public enum EquationType {
        establish, neutralize
    }

    // ----------------------------------------
    // ----------------------------------------

    @Enumerated(EnumType.STRING)
    @NotNull
    private EquationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Source debit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Source credit;

    // ----------------------------------------
    // ----------------------------------------

    public Equation(EquationType type, Activity activity, Source debit, Source credit) {
        this.type = type;
        this.activity = activity;
        this.debit = debit;
        this.credit = credit;
    }

    protected Equation() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public EquationType getType() {
        return type;
    }

    public Activity getActivity() {
        return activity;
    }

    public Source getDebit() {
        return debit;
    }

    public void setDebit(Source debit) {
        this.debit = debit;
    }

    public Source getCredit() {
        return credit;
    }

    public void setCredit(Source credit) {
        this.credit = credit;
    }

}
