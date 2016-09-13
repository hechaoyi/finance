package com.hedatou.finance.domain.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hedatou.finance.domain.accounting.entity.Transaction;

/**
 * 实现描述：会计：事务
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 上午9:43:31
 */
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
