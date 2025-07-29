import java.util.concurrent.*;
import java.util.*;

public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 1. 自定义线程池（推荐实际用法）
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                       // 核心线程数
                4,                       // 最大线程数
                60,                      // 空闲线程最大存活时间
                TimeUnit.SECONDS,        // 时间单位
                new LinkedBlockingQueue<>(2), // 阻塞队列容量2
                Executors.defaultThreadFactory(), // 默认线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：抛异常
        );

        // 2. 创建一批任务（使用 Callable 带返回值）
        List<Callable<Integer>> taskList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            taskList.add(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + taskId);
                TimeUnit.SECONDS.sleep(1);
                return taskId * taskId;
            });
        }

        // 3. 提交任务并收集 Future
        List<Future<Integer>> resultList = new ArrayList<>();
        for (Callable<Integer> task : taskList) {
            Future<Integer> future = executor.submit(task);
            resultList.add(future);
        }

        // 4. 获取返回结果
        for (Future<Integer> future : resultList) {
            Integer result = future.get(); // 会阻塞直到任务执行完
            System.out.println("收到结果: " + result);
        }

        // 5. 关闭线程池
        executor.shutdown();
    }
}
