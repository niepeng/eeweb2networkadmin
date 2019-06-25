//package com.chengqianyun.eeweb2networkadmin.test.other;
//
//import com.google.common.util.concurrent.RateLimiter;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class RateLimiterTest {
//
//    public static long prev = 0;
//
//    public static void main(String[] args) {
//        // 限流器流速：2 个请求 / 秒
//        RateLimiter limiter = RateLimiter.create(2.0);
//
//        // 执行任务的线程池
//        ExecutorService es = Executors.newFixedThreadPool(1);
//        // 记录上一次执行时间
//        prev = System.nanoTime();
//
//        // 测试执行 20 次
//        for (int i = 0; i < 20; i++) {
//            // 限流器限流
//            limiter.acquire();
//            // 提交任务异步执行
//            es.execute(() -> {
//                long cur = System.nanoTime();
//                // 打印时间间隔：毫秒
//                System.out.println((cur - prev) / 1000_000);
//                prev = cur;
//            });
//        }
//    }
//
//
//}
