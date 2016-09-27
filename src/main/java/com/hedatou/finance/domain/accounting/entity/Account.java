package com.hedatou.finance.domain.accounting.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.hedatou.finance.infrastructure.entity.BaseEntity;

/**
 * 实现描述：会计：账号
 *
 * @author hechaoyi@me.com
 * @since 2016年9月27日 下午5:16:56
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "acct_account")
public class Account extends BaseEntity {

    @NotEmpty
    @Length(max = 20)
    private String name;

    // ----------------------------------------
    // ----------------------------------------

    public Account(String name) {
        this.name = name;
    }

    protected Account() {
    }

    // ----------------------------------------
    // ----------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
