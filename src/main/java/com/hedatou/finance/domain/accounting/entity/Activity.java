package com.hedatou.finance.domain.accounting.entity;

import static org.springframework.util.Assert.isTrue;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.ImmutableSet;
import com.hedatou.finance.infrastructure.entity.BaseEntity;
import com.hedatou.finance.infrastructure.utils.Streams;

/**
 * 实现描述：会计：事务中的独立行为
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午1:36:07
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_activity")
public class Activity extends BaseEntity {

    public enum ActivityType {
        equationEstablish, // 产生
        equationNeutralize, // 抵消
        hierarchyFork, // 分裂
        hierarchyJoin, // 合并
    }

    // ----------------------------------------
    // ----------------------------------------

    @Enumerated(EnumType.STRING)
    @NotNull
    private ActivityType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Transaction transaction;

    @OneToOne(fetch = FetchType.LAZY)
    private Activity next;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<Equation> equations;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private Set<Hierarchy> hierarchies;

    // ----------------------------------------
    // ----------------------------------------

    public Activity(Transaction transaction) {
        this.transaction = transaction;
    }

    protected Activity() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public ActivityType getType() {
        return type;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Activity getNext() {
        return next;
    }

    public void setNext(Activity next) {
        this.next = next;
    }

    public Equation getEquation() {
        return equations != null && !equations.isEmpty() ? equations.iterator().next() : null;
    }

    public Set<Hierarchy> getHierarchies() {
        return hierarchies;
    }

    // ----------------------------------------
    // ----------------------------------------

    public Activity setEstablishEquation(Source debit, Source credit) {
        isTrue(debit.getType().isDebit() && credit.getType().isCredit(), "类型不匹配");
        isTrue(debit.getAmount().compareTo(credit.getAmount()) == 0, "金额不匹配");
        isTrue(CollectionUtils.isEmpty(this.hierarchies), "行为不能同时既有层次又有置换关系");

        this.type = ActivityType.equationEstablish;
        this.equations = ImmutableSet.of(new Equation(Equation.EquationType.establish, this, debit, credit));
        return this;
    }

    public Activity setNeutralizeEquation(Source debit, Source credit) {
        isTrue(debit.getType().isDebit() && credit.getType().isCredit(), "类型不匹配");
        isTrue(debit.getAmount().compareTo(credit.getAmount()) == 0, "金额不匹配");
        isTrue(CollectionUtils.isEmpty(this.hierarchies), "行为不能同时既有层次又有置换关系");

        this.type = ActivityType.equationNeutralize;
        this.equations = ImmutableSet.of(new Equation(Equation.EquationType.neutralize, this, debit, credit));
        return this;
    }

    public Activity setForkHierarchies(Source parent, Source... children) {
        isTrue(Streams.all(children, child -> child.getType() == parent.getType()), "类型不匹配");
        isTrue(Streams.sumBigDecimal(children, Source::getAmount).compareTo(parent.getAmount()) == 0, "金额不匹配");
        isTrue(CollectionUtils.isEmpty(this.equations), "行为不能同时既有层次又有置换关系");

        this.type = ActivityType.hierarchyFork;
        this.hierarchies = Streams.mapToSet(children, child -> new Hierarchy(this, parent, child));
        return this;
    }

    public Activity setJoinHierarchies(Source child, Source... parents) {
        isTrue(Streams.all(parents, parent -> parent.getType() == child.getType()), "类型不匹配");
        isTrue(Streams.sumBigDecimal(parents, Source::getAmount).compareTo(child.getAmount()) == 0, "金额不匹配");
        isTrue(CollectionUtils.isEmpty(this.equations), "行为不能同时既有层次又有置换关系");

        this.type = Activity.ActivityType.hierarchyJoin;
        this.hierarchies = Streams.mapToSet(parents, parent -> new Hierarchy(this, parent, child));
        return this;
    }

}
