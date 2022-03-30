package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public class ConcreteStrategyTime implements Strategy{

    public void addTask(ArrayList<Server> servers, Task task){
        ArrayList<Integer> totalWaitingTimes = new ArrayList<>();
        for (Server server : servers) {
            int waitingTime = 0;
            for (Task t : server.getTasks()) {
                waitingTime += t.getServiceTime();
            }
            totalWaitingTimes.add(waitingTime);
        }
        Integer minWaitingTime = totalWaitingTimes.get(0);
        int position = 0;
        for(int i = 0; i < totalWaitingTimes.size(); i++){
            if(minWaitingTime > totalWaitingTimes.get(i)){
                minWaitingTime = totalWaitingTimes.get(i);
                position = i;
            }
        }
        servers.get(position).addTask(task);
    }

}