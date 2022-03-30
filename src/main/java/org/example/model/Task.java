package org.example.model;

public class Task {
    private final Integer ID;
    private final Integer arrivalTime;
    private Integer serviceTime;

    public Task(int ID, int arrivalTime, int serviceTime){
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public Integer getID() {
        return ID;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public Integer getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }
}
