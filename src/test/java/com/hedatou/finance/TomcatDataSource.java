package com.hedatou.finance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;

public class TomcatDataSource {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Random RAND = new Random();
    private static final int MAX = 8;
    private static final int MIN = 2;
    private static DataSource ds;

    public static void main(String[] args) throws Exception {
        init();
        new Thread(() -> {
            while (true) {
                sleep(1000);
                status();
            }
        }).start();
        while (true) {
            borrow(RAND.nextInt((int) (MAX * 1.2)));
            sleep(RAND.nextInt(3000));
        }
    }

    private static void sleep(int millis) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(millis));
    }

    private static void init() throws SQLException {
        ds = new DataSource();
        // ds.setDriverClassName("org.h2.Driver");
        // ds.setUrl("jdbc:h2:mem:test");
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
        ds.setUsername("root");

        ds.setMaxActive(MAX); // 默认100
        ds.setMaxIdle(MAX); // 默认100
        ds.setMinIdle(MIN); // 默认10
        ds.setInitialSize(MIN); // 默认10
        ds.setMaxWait(3000); // 默认30000
        // ds.setMaxAge(6 * 3600 * 1000); // 默认0

        // ds.setTestOnBorrow(true); // 默认false
        ds.setTestWhileIdle(true); // 默认false
        ds.setValidationQuery("SELECT 1"); // 默认null
        ds.setValidationQueryTimeout(1000); // 默认-1
        ds.setValidationInterval(3000); // 默认30000
        // ds.setTimeBetweenEvictionRunsMillis(1000); // 默认5000
        // ds.setMinEvictableIdleTimeMillis(3000); // 默认60000

        ds.createPool();
    }

    private static void status() {
        System.out.println(String.format("\t status: %d(active) + %d(idle) = %d, wait: %d", ds.getActive(),
                ds.getIdle(), ds.getSize(), ds.getWaitCount()));
        if (RAND.nextInt(3) > 0)
            return;
        try (Connection conn = ds.getConnection()) {
            List<Integer> processes = Lists.newArrayList();
            try (Statement stmt = conn.createStatement()) {
                ResultSet rs = stmt.executeQuery("SHOW PROCESSLIST");
                while (rs.next()) {
                    if ("Sleep".equals(rs.getString("command")))
                        processes.add(rs.getInt("id"));
                }
            }
            if (!processes.isEmpty()) {
                try (Statement stmt = conn.createStatement()) {
                    int id = processes.get(RAND.nextInt(processes.size()));
                    stmt.execute("KILL " + id);
                    System.out.println("\t killed: " + id);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void borrow(int size) throws InterruptedException {
        System.out.println(">> job start: " + size);
        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            ListenableFuture<Connection> future = null;
            try {
                future = JdkFutureAdapters.listenInPoolThread(ds.getConnectionAsync(), executor);
            } catch (SQLException e) {
                e.printStackTrace();
                latch.countDown();
                continue;
            }
            Futures.addCallback(future, new FutureCallback<Connection>() {
                @Override
                public void onSuccess(Connection conn) {
                    try (Statement stmt = conn.createStatement()) {
                        stmt.execute("SELECT NOW()");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sleep(RAND.nextInt(5000));
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    latch.countDown();
                }
            }, executor);
        }
        latch.await();
        System.out.println("<< job finish: " + size);
    }

}
