package com.vehicle.management;//车辆编号、车牌号、车辆制造公司、车辆购买时间、
// 车辆型号（大客车、小轿车和卡车）、总公里数、
// 耗油量/公里、基本维护费用、养路费、累计总费用
import java.io.Serializable;
import java.time.LocalDate;
public abstract class Vehicle implements Serializable {
    private static final long serialVersionUID=1L;
//    车辆编号
    protected String vehicleId;
//    车牌号
    protected String licensePlate;
//    车辆制造公司、
    protected String manufacturer;
//    车辆购买时间、
    protected LocalDate purchaseDate;
// 车辆型号（大客车、小轿车和卡车）、
    protected String vehicleType;
// 总公里数
    protected double totalKilometers;
// 耗油量/公里、
    protected double fuelConsumption;
//    基本维护费用、
    protected double basicMaintenanceFee;
//    养路费、
    protected double roadMaintenanceFee;
//    累计总费
    protected double totalCost;

//    构造方法
    public Vehicle(String vehicleId, String licensePlate, String manufacturer, LocalDate purchaseDate,
                   String vehicleType, double totalKilometers, double fuelConsumption){
        this.vehicleId = vehicleId;
        this.licensePlate = licensePlate;
        this.manufacturer = manufacturer;
        this.purchaseDate = purchaseDate;
        this.vehicleType = vehicleType;
        this.totalKilometers = totalKilometers;
        this.fuelConsumption = fuelConsumption;
        setBasicMaintenanceFee();
        calculateTotalCost();
    }
//    设置基本维护费用
    protected abstract void setBasicMaintenanceFee();

//    计算当月总费用
    public void calculateTotalCost() {
        this.totalCost = SystemConfig.getOilPrice() * fuelConsumption + basicMaintenanceFee + roadMaintenanceFee;
    }

//    Getter和Setter方法
    public String getVehicleId(){return vehicleId;}
    public String getLicensePlate() { return licensePlate; }
    public String getManufacturer() { return manufacturer; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getVehicleType() { return vehicleType; }
    public double getTotalKilometers() { return totalKilometers; }
    public double getFuelConsumption() { return fuelConsumption; }
    public double getBasicMaintenanceFee() { return basicMaintenanceFee; }
    public double getRoadMaintenanceFee() { return roadMaintenanceFee; }
    public double getTotalCost() { return totalCost; }
    public void setRoadMaintenanceFee(double roadMaintenanceFee) {
        this.roadMaintenanceFee = roadMaintenanceFee;
        calculateTotalCost(); // 更新总费用
    }

    // 获取附加信息（由子类实现）
    public abstract String getAdditionalInfo();

    public double getCurrentTotalCost() {
        calculateTotalCost(); // 每次获取费用时重新计算
        return totalCost;
    }

    @Override
    public String toString() {
        return "车辆编号: " + vehicleId +
                "\n车牌号: " + licensePlate +
                "\n制造商: " + manufacturer +
                "\n购买日期: " + purchaseDate +
                "\n车辆类型: " + vehicleType +
                "\n总公里数: " + totalKilometers + " 公里" +
                "\n油耗: " + fuelConsumption + " 升/公里" +
                "\n基本维护费: " + basicMaintenanceFee + " 元" +
                "\n养路费: " + roadMaintenanceFee + " 元" +
                "\n总费用: " + getCurrentTotalCost() + " 元" +
                "\n附加信息: " + getAdditionalInfo();
    }
}
