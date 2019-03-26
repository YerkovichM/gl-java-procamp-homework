package threadpool;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import threadpool.exceptions.WorkQueueIsFullException;

public class MyExecutorsTest {

    private static Runnable[] runs = new Runnable[10];

    private static Runnable generateRun(String s){
        return () -> {
            for (int i = 1; i <= 100; i++) {
                for (int j = 0; j < i; j++) {
                    System.out.print(s);
                }
                System.out.println();
            }
        };
    }

    @Before
    public void init(){
        runs[0] = generateRun("a");
        runs[1] = generateRun("b");
        runs[2] = generateRun("c");
        runs[3] = generateRun("d");
        runs[4] = generateRun("e");
        runs[5] = generateRun("f");
        runs[6] = generateRun("k");
        runs[7] = generateRun("p");
        runs[8] = generateRun("o");
        runs[9] = generateRun("s");
    }




    @Test(expected = WorkQueueIsFullException.class)
    public void toMuchWorkTest(){
        MyExecutorService executorService = MyExecutors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            for (Runnable r: runs){
                executorService.execute(r);
            }
        }
    }

}
