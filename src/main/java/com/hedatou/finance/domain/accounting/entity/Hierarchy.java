package com.hedatou.finance.domain.accounting.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.hedatou.finance.infrastructure.entity.BaseEntity;

/**
 * 实现描述：会计：资源之间的层次关系
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午2:58:10
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_hierarchy")
public class Hierarchy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Source parent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Source child;

    // ----------------------------------------
    // ----------------------------------------

    public Hierarchy(Activity activity, Source parent, Source child) {
        this.activity = activity;
        this.parent = parent;
        this.child = child;
    }

    protected Hierarchy() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public Activity getActivity() {
        return activity;
    }

    public Source getParent() {
        return parent;
    }

    public void setParent(Source parent) {
        this.parent = parent;
    }

    public Source getChild() {
        return child;
    }

    public void setChild(Source child) {
        this.child = child;
    }

}
