package com.hedatou.finance.domain.accounting.entity;

import java.util.Arrays;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.Assert;

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

    public static enum ActivityType {
        equation, hierarchyFork, hierarchyJoin
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

    public Activity setEquation(Source debit, Source credit) {
        Assert.isTrue(debit.getType().isDebit() && credit.getType().isCredit(), "类型不匹配");
        Assert.isTrue(debit.getAmount().compareTo(credit.getAmount()) == 0, "金额不匹配");

        this.type = ActivityType.equation;
        this.equations = ImmutableSet.of(new Equation(this, debit, credit));
        return this;
    }

    public Set<Hierarchy> getHierarchies() {
        return hierarchies;
    }

    public Activity setForkHierarchies(Source parent, Source... children) {
        Assert.isTrue(Arrays.stream(children).allMatch(child -> child.getType() == parent.getType()), "类型不匹配");
        Assert.isTrue(Arrays.stream(children).map(child -> child.getAmount()).reduce((a1, a2) -> a1.add(a2)).get()
                .compareTo(parent.getAmount()) == 0, "金额不匹配");

        this.type = ActivityType.hierarchyFork;
        this.hierarchies = Streams.mapToSet(children, child -> new Hierarchy(this, parent, child));
        return this;
    }

    public Activity setJoinHierarchies(Source child, Source... parents) {
        Assert.isTrue(Arrays.stream(parents).allMatch(parent -> parent.getType() == child.getType()), "类型不匹配");
        Assert.isTrue(Arrays.stream(parents).map(parent -> parent.getAmount()).reduce((a1, a2) -> a1.add(a2)).get()
                .compareTo(child.getAmount()) == 0, "金额不匹配");

        this.type = Activity.ActivityType.hierarchyJoin;
        this.hierarchies = Streams.mapToSet(parents, parent -> new Hierarchy(this, parent, child));
        return this;
    }

}
