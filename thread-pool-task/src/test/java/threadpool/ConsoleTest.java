package threadpool;

public class ConsoleTest {
    private static Runnable[] runs = new Runnable[10];

    public static void main(String... a){
        consoleTest();
    }

    private static void consoleTest(){
        init();

        MyExecutorService executorService = MyExecutors.newFixedThreadPool(4);

        for (Runnable r: runs){
            executorService.execute(r);
        }
    }

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

    private static void init(){
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

}
