package com.hedatou.finance.infrastructure.entity;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.google.common.base.MoreObjects;

/**
 * 实现描述：实体基类
 *
 * @author hechaoyi@me.com
 * @since 2016年9月13日 下午8:36:12
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreType
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.PROPERTY)
    protected int id;

    @Version
    protected int ver;

    @CreatedDate
    protected Date createdAt;

    @LastModifiedDate
    protected Date modifiedAt;

    // ----------------------------------------
    // ----------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVer() {
        return ver;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    // ----------------------------------------
    // ----------------------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseEntity other = (BaseEntity) obj;
        if (id != other.id)
            return false;
        return (id != 0);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("id", id).add("ver", ver).toString();
    }

}
