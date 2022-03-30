package org.example.businessLogic;

import org.example.model.Task;
import org.example.model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Scheduler implements Runnable{
    private final List<Server> servers = new ArrayList<>();
    private Strategy strategy;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean start = new AtomicBoolean(false);
    private final int timeLimit;
    private int peakHour = 0;
    private int maxNrOfClients = 0;
    private float waitingTime = 0;

    public Scheduler(int maxNoServers, int maxTasksPerServer, int timeLimit){
        this.timeLimit = timeLimit;
        for(int i = 0; i < maxNoServers; i++){
            Server newServer = new Server(maxTasksPerServer);
            servers.add(newServer);
        }
        Thread schedulerThread = new Thread(this);
        schedulerThread.start();
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        } else {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(List<Server> servers, Task task){
        strategy.addTask((ArrayList<Server>) servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void start(){
        start.set(true);
    }

    public void interrupt() {
        running.set(false);
    }

    public int getPeakHour() {
        return peakHour;
    }

    public float getWaitingTime() {
        return waitingTime;
    }

    private void updateNrOfClients(int currentTime){
        int nrOfCurrentClients = 0;
        for(Server s: servers){
            for(Task ignored : s.getTasks()){
                nrOfCurrentClients++;
            }
        }
        if(nrOfCurrentClients > maxNrOfClients){
            maxNrOfClients = nrOfCurrentClients;
            peakHour = currentTime;
        }
    }

    private float updateWaitingTime(float waitingTime){
        for(Server s: servers){
            if(s.getTasks().size() > 1){
                waitingTime++;
            }
        }
        return waitingTime;
    }

    @Override
    public void run() {
        running.set(true);
        while(running.get()) {
            if (start.get()) {
                int currentTime = 0;
                while(currentTime < timeLimit){
                    updateNrOfClients(currentTime);
                    waitingTime = updateWaitingTime(waitingTime);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentTime++;
                }
            }
        }
    }
}
