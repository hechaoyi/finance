package com.hedatou.finance.domain.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hedatou.finance.domain.accounting.entity.Activity;

/**
 * 实现描述：会计：事务中的独立行为
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午8:36:28
 */
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
