package org.example.businessLogic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public interface Strategy {
    void addTask(ArrayList<Server> servers, Task task);
}
