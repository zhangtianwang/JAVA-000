package java0.conc0303;

import com.sun.xml.internal.ws.util.CompletedFuture;

import java.util.concurrent.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        int result = method3();

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");

        // 然后退出main线程
    }

    /**
     * Future
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static Integer method1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() {
                return sum();
            }
        });
        executorService.shutdown();
        Integer res = future.get();
        System.out.println(res);
        return res;
    }

    /**
     * FutureTask
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static Integer method2() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        new Thread(futureTask).start();
        Integer res = futureTask.get();
        return res;
    }

    /**
     *  CompletableFuture
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static Integer method3() throws ExecutionException, InterruptedException {
        Integer res = CompletableFuture.supplyAsync(() -> sum()).get();
        System.out.println("结果：" + res);
        return res;
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2)
            return 1;
        return fibo(a - 1) + fibo(a - 2);
    }
}
