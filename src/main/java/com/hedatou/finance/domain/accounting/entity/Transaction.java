package com.hedatou.finance.domain.accounting.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.collect.Sets;
import com.hedatou.finance.infrastructure.entity.BaseEntity;
import com.hedatou.finance.infrastructure.utils.Streams;

/**
 * 实现描述：会计：事务
 *
 * @author hechaoyi@me.com
 * @since 2016年9月13日 下午8:58:22
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_transaction")
public class Transaction extends BaseEntity {

    @NotBlank
    @Length(max = 20)
    private String name;

    @NotNull
    @Past
    private Date occurredAt;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    @NotEmpty
    private Set<Activity> activities;

    // ----------------------------------------
    // ----------------------------------------

    public Transaction(String name, Date occurredAt) {
        this.name = name;
        this.occurredAt = occurredAt;
    }

    public Transaction(String name) {
        this(name, new Date());
    }

    protected Transaction() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    // ----------------------------------------
    // ----------------------------------------

    public Activity addActivity() {
        if (this.activities == null)
            this.activities = Sets.newHashSet();
        Activity activity = new Activity(this);
        Streams.find(this.activities, act -> act.getNext() == null).ifPresent(tail -> tail.setNext(activity));
        this.activities.add(activity);
        return activity;
    }

}
