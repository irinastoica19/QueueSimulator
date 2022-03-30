package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public class ConcreteStrategyQueue implements Strategy{

    public void addTask(ArrayList<Server> servers, Task task){
        ArrayList<Integer> serverSizes = new ArrayList<>();
        for (Server server : servers) {
            int size = server.getTasks().size();
            serverSizes.add(size);
        }
        Integer minServerSize = serverSizes.get(0);
        int position = 0;
        for(int i = 0; i < serverSizes.size(); i++){
            if(minServerSize > serverSizes.get(i)){
                minServerSize = serverSizes.get(i);
                position = i;
            }
        }
        servers.get(position).addTask(task);
    }

}