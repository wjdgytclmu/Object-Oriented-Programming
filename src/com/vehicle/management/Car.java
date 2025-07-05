package com.vehicle.management;

import java.io.Serializable;
import java.time.LocalDate;

public class Car extends Vehicle {
    private static final long serialVersionUID=1L;
    private int trunkNumber;

    public Car(String vehicleId, String licensePlate, String manufacturer, LocalDate purchaseDate,
               double totalKilometers, double fuelConsumption, int trunkNumber) {
        super(vehicleId, licensePlate, manufacturer, purchaseDate, "小轿车", totalKilometers, fuelConsumption);
        this.trunkNumber = trunkNumber;
    }

    @Override
    protected void setBasicMaintenanceFee() {
        this.basicMaintenanceFee = 1000;
    }

    @Override
    public String getAdditionalInfo() {
        return "箱数: " + trunkNumber + "厢";
    }

    public int getTrunkNumber() {
        return trunkNumber;
    }
}
