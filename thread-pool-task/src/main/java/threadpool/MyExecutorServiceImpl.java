package threadpool;

import threadpool.exceptions.WorkQueueIsFullException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MyExecutorServiceImpl implements MyExecutorService {

    private Queue<Runnable> workQueue = new LinkedList<>();
    private Map<Long, Thread> threadPool = new HashMap<>();

    private int workQueueSize;
    private int poolSize;

    private boolean broken = false;

    MyExecutorServiceImpl(int poolSize, int workQueueSize) {
        this.workQueueSize = workQueueSize;
        this.poolSize = poolSize;
        fillPool();
    }

    public void execute(Runnable command) {
        if(workQueue.size() >= workQueueSize){
            throw new WorkQueueIsFullException();
        }
        repairPool();
        synchronized (workQueue) {
            workQueue.add(command);
            workQueue.notifyAll();
        }
    }

    public void shutdownNow() {
        threadPool.values().forEach(Thread::interrupt);
        threadPool = new HashMap<>();
    }

    private Runnable getWork(){
        while(true) {
            synchronized (workQueue) {
                if (workQueue.size() <= 0) {
                    try {
                    workQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        broken = true;
                    }
                } else {
                    return workQueue.poll();
                }
            }
        }
    }

    private void fillPool(){
        if(threadPool.size() < poolSize){
            for(int i = 0; i < poolSize - threadPool.size(); i++){
                Thread th = new ExecutorThread();
                threadPool.put(th.getId(), th);
                th.start();
            }
        }
    }

    private void deleteDeadFormPool(){
        for(Thread th: threadPool.values()){
            if(th.getState() == Thread.State.TERMINATED){
                threadPool.remove(th.getId());
            }
        }
    }

    private void repairPool(){
        if(broken) {
            deleteDeadFormPool();
            fillPool();
            broken = false;
        }
    }

    private boolean anyThreadWaiting(){
        return threadPool.values().stream().anyMatch(th->th.getState().equals(Thread.State.TIMED_WAITING));
    }

    private class ExecutorThread extends Thread{
        @Override
        public void run() {
            try {
                while (!isInterrupted()){
                    Runnable runnable = getWork();
                    runnable.run();
                }
            }catch (Exception e){
                e.printStackTrace();
                broken = true;
            }finally{
                finish();
            }
        }
        private void finish(){
            // finishes something
        }
    }
}
