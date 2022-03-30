package org.example.businessLogic;

import org.example.SimulationView;
import org.example.model.Server;
import org.example.model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int minProcessingTime;
    private int maxProcessingTime;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int nrOfClients;
    private Scheduler scheduler;
    private SimulationView simulationView;
    private final List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());
    private boolean validData;
    private float averageServiceTime;

    public SimulationManager(String timeLimit, String minProcessingTime, String maxProcessingTime, String minArrivalTime, String maxArrivalTime, String nrOfClients, String nrOfQueues, String selectionPolicy){
        try{
            this.timeLimit = Integer.parseInt(timeLimit);
            this.minProcessingTime = Integer.parseInt(minProcessingTime);
            this.maxProcessingTime = Integer.parseInt(maxProcessingTime);
            this.minArrivalTime = Integer.parseInt(minArrivalTime);
            this.maxArrivalTime = Integer.parseInt(maxArrivalTime);
            this.nrOfClients = Integer.parseInt(nrOfClients);
            int nrOfQueues1 = Integer.parseInt(nrOfQueues);
            SelectionPolicy selectionPolicy1;
            if(selectionPolicy.equals("Shortest Time")){
                selectionPolicy1 = SelectionPolicy.SHORTEST_TIME;
            } else {
                selectionPolicy1 = SelectionPolicy.SHORTEST_QUEUE;
            }
            scheduler = new Scheduler(nrOfQueues1, this.nrOfClients, this.timeLimit);
            scheduler.changeStrategy(selectionPolicy1);
            generateTasks();
            System.out.println(getWaitingClients());
            simulationView = new SimulationView();
            validData = true;
            this.run();
        } catch(NumberFormatException | NullPointerException e){
            validData = false;
        }
    }

    public void generateTasks(){
        Random random = new Random();
        for(int i = 0; i < nrOfClients; i++){
            int serviceTime = random.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime;
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;
            Task task = new Task(i,arrivalTime,serviceTime);
            generatedTasks.add(task);
        }
        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        for(Task t: generatedTasks){
            averageServiceTime += t.getServiceTime();
        }
        averageServiceTime /= (float) nrOfClients;
    }

    public boolean isValidData() {
        return validData;
    }

    @Override
    public void run() {
        int currentTime = 0;
        while(currentTime<timeLimit) {
            System.out.println(currentTime);
            synchronized(generatedTasks){
                while (generatedTasks.size() > 0 && generatedTasks.get(0).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(scheduler.getServers(), generatedTasks.get(0));
                    generatedTasks.remove(generatedTasks.get(0));
                }
            }
            log(currentTime);
            simulationView.updateWaitingClients("");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scheduler.start();
            currentTime++;
        }
        for(Server s: scheduler.getServers()){
            s.interrupt();
        }
        logStatistics();
        scheduler.interrupt();
    }

    public String getWaitingClients(){
        String waitingClients = "";
        for(Task t: generatedTasks){
            waitingClients = waitingClients.concat(" (" + t.getID() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + "); ");
        }
        return waitingClients;
    }

    public void log(int currentTime) {
        try {
            FileWriter writer = new FileWriter("log.txt", true);
            writer.write("Time " + currentTime + "\n");
            writer.write("Waiting clients: " + getWaitingClients());
            for(int i = 0; i<scheduler.getServers().size(); i++){
                int queueNumber = i + 1;
                writer.write("\nQueue " + queueNumber + ":");
                if(scheduler.getServers().get(i).getTasks().size() < 1){
                    writer.write(" closed");
                } else {
                    for (Task t: scheduler.getServers().get(i).getTasks()) {
                        writer.write( "(" + t.getID() + ", " + t.getArrivalTime() + ", " + t.getServiceTime() + "); ");
                    }
                }
            }
            writer.write("\n");
            writer.close();
        } catch( IOException e ){
            e.printStackTrace();
        }
    }

    public void logStatistics(){
        try{
            FileWriter writer = new FileWriter("log.txt", true);
            writer.write("\nAverage Waiting Time: " + scheduler.getWaitingTime()/nrOfClients);
            writer.write("\nAverage Service Time: " + averageServiceTime);
            writer.write("\nPeak Hour: " + scheduler.getPeakHour());
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

}