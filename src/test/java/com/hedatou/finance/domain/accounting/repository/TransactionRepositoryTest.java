package com.hedatou.finance.domain.accounting.repository;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.hedatou.finance.domain.accounting.entity.Activity;
import com.hedatou.finance.domain.accounting.entity.Equation;
import com.hedatou.finance.domain.accounting.entity.Source;
import com.hedatou.finance.domain.accounting.entity.Source.SourceType;
import com.hedatou.finance.domain.accounting.entity.Transaction;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private EquationRepository equationRepository;

    @Test
    @Commit
    public void test1() {
        Transaction tx = new Transaction("abc");

        Source wallet = new Source(SourceType.asset, BigDecimal.valueOf(10));
        Source me = new Source(SourceType.equity, BigDecimal.valueOf(10));
        tx.addActivity().setEquation(wallet, me);

        repository.save(tx);
    }

    @Test
    public void test2() {
        System.out.println(1);
        Activity act = activityRepository.findOne(1);
        System.out.println(2);
        Transaction tx = act.getTransaction();
        System.out.println(3);
        tx.getId();
        System.out.println(4);
    }

    @Test
    public void test3() {
        System.out.println(5);
        Equation eq = equationRepository.findOne(1);
        System.out.println(6);
        Activity act = eq.getActivity();
        System.out.println(7);
        act.getId();
        System.out.println(8);
    }

}
