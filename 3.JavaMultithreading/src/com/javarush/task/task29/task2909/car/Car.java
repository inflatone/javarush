package com.javarush.task.task29.task2909.car;

import java.util.Date;

public abstract class Car {
    static public final int TRUCK = 0;
    static public final int SEDAN = 1;
    static public final int CABRIOLET = 2;
    public double summerFuelConsumption;
    public double winterFuelConsumption;
    public double winterWarmingUp;
    double fuel;
    private int type;

    private boolean driverAvailable;
    private int numberOfPassengers;

    protected Car(int type, int numberOfPassengers) {
        this.type = type;
        this.numberOfPassengers = numberOfPassengers;
    }

    public static Car create(int type, int numberOfPassengers) {
        Car result = null;
        if (type == TRUCK) {
            result = new Truck(numberOfPassengers);
        } else if (type == SEDAN) {
            result = new Sedan(numberOfPassengers);
        } else if (type == CABRIOLET) {
            result = new Cabriolet(numberOfPassengers);
        }
        return result;
    }

    public void fill(double numberOfLiters) throws Exception {
        if (numberOfLiters < 0)
            throw new Exception();
        fuel += numberOfLiters;
    }

    public boolean isSummer(Date date, Date summerStart, Date summerEnd) {
        return !date.before(summerStart) && !date.after(summerEnd);
    }

    public double getWinterConsumption(int length) {
        return length * winterFuelConsumption + winterWarmingUp;
    }

    public double getSummerConsumption(int length) {
        return length * summerFuelConsumption;
    }

    public double getTripConsumption(Date date, int length, Date SummerStart, Date SummerEnd) {
        return isSummer(date, SummerStart, SummerEnd)
                ? getSummerConsumption(length)
                : getWinterConsumption(length);
    }

    public int getNumberOfPassengersCanBeTransferred() {
        if (canPassengersBeTransferred()) {
            return numberOfPassengers;
        }
        return 0;
    }

    private boolean canPassengersBeTransferred() {
        return isDriverAvailable() && fuel > 0;
    }

    public boolean isDriverAvailable() {
        return driverAvailable;
    }

    public void setDriverAvailable(boolean driverAvailable) {
        this.driverAvailable = driverAvailable;
    }

    public void startMoving() {
        fastenDriverBelt();
        if (numberOfPassengers > 0) {
            fastenPassengersBelts();
        }
    }

    public void fastenPassengersBelts() {
    }

    public void fastenDriverBelt() {
    }

    public abstract int getMaxSpeed();
}