package org.example.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private final AtomicInteger waitingPeriod = new AtomicInteger();
    private final BlockingQueue<Task> tasks;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Server(int maxTasksPerServer){
        tasks = new ArrayBlockingQueue<>(maxTasksPerServer);
        Thread t = new Thread(this);
        t.start();
    }

    public void addTask(Task task){
        tasks.add(task);
        waitingPeriod.getAndIncrement();
    }

    public void interrupt() {
        running.set(false);
    }

    @Override
    public void run(){
        running.set(true);
        while(running.get()){
            try {
                if(!tasks.isEmpty()) {
                    int serviceTime = tasks.peek().getServiceTime();
                    while(serviceTime > 0){
                        Thread.sleep(1000);
                        serviceTime--;
                        tasks.peek().setServiceTime(serviceTime);
                    }
                    tasks.take();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("finish");
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

}
