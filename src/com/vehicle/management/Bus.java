package com.vehicle.management;

import java.io.Serializable;
import java.time.LocalDate;

public class Bus extends Vehicle {
    private static final long serialVersionUID = 1L;

    private int passengerCapacity;

    public Bus(String vehicleId, String licensePlate, String manufacturer, LocalDate purchaseDate,
               double totalKilometers, double fuelConsumption, int passengerCapacity) {
        super(vehicleId, licensePlate, manufacturer, purchaseDate, "大客车", totalKilometers, fuelConsumption);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    protected void setBasicMaintenanceFee() {
        this.basicMaintenanceFee = 2000;
    }

    @Override
    public String getAdditionalInfo() {
        return "载客量: " + passengerCapacity + "人";
    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }
}
