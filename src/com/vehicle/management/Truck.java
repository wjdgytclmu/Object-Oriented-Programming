package com.vehicle.management;

import java.io.Serializable;
import java.time.LocalDate;

public class Truck extends Vehicle {
    public static final long serialVersionUID=1L;
    private double loadCapacity;

    public Truck(String vehicleId, String licensePlate, String manufacturer, LocalDate purchaseDate,
                 double totalKilometers, double fuelConsumption, double loadCapacity) {
        super(vehicleId, licensePlate, manufacturer, purchaseDate, "卡车", totalKilometers, fuelConsumption);
        this.loadCapacity = loadCapacity;
    }

    @Override
    protected void setBasicMaintenanceFee() {
        this.basicMaintenanceFee = 1500;
    }

    @Override
    public String getAdditionalInfo() {
        return "载重量: " + loadCapacity + "吨";
    }

    public double getLoadCapacity() {
        return loadCapacity;
    }
}
