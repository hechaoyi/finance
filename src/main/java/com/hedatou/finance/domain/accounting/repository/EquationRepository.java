package com.hedatou.finance.domain.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hedatou.finance.domain.accounting.entity.Equation;

/**
 * 实现描述：会计：资源之间的置换关系
 *
 * @author hechaoyi@me.com
 * @since 2016年9月14日 下午8:36:28
 */
public interface EquationRepository extends JpaRepository<Equation, Integer> {
}
