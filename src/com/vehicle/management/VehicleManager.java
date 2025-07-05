package com.vehicle.management;

import java.util.List;

public class VehicleManager {
    private final FileManager fileManager=new FileManager();
    private List<Vehicle> vehicles;

    public VehicleManager(){
        loadVehicles();
    }

//    从文件加载车辆
    private void loadVehicles(){
        this.vehicles=fileManager.loadVehicles();
    }

//    保存车辆到文件
public void saveVehicles(){
        fileManager.saveVehicle(vehicles);
    }

//    添加车辆
    public boolean addVehicle(Vehicle vehicle){
        // 使用配置接口的MAX_VEHICLES常量
        if (vehicles.size() >= SystemConfig.MAX_VEHICLES) {
            System.out.println("车辆信息库已满，不能再添加新的数据！");
            return false;
        }

        if (isIdExists(vehicle.getVehicleId())) {
            System.out.println("数据添加重复，车辆编号已存在！");
            return false;
        }

        vehicles.add(vehicle);
        saveVehicles(); // 保存到文件
        return true;
    }
//    按编号查询车辆
    public Vehicle queryById(String vehicleTd){
        return vehicles.stream()
                .filter(v -> v.getVehicleId().equals(vehicleTd))
                .findFirst()
                .orElse(null);
    }

//    获取所有车辆
    public List<Vehicle> getAllVehicles(){
        return vehicles;
    }

//    按制造公司查询车辆
    public List<Vehicle> queryByManufacturer(String manufacturer){
        return vehicles.stream()
                .filter(v -> v.getManufacturer().equals(manufacturer))
                .toList();
    }

//    按类别查询车辆
    public List<Vehicle> queryByType(String vehicleType){
        return vehicles.stream()
                .filter(v ->v.getVehicleType().equals(vehicleType))
                .toList();
    }

//    编辑车辆信息
    public boolean editVehicle(String vehicleId,Vehicle updatedVehicle){
        Vehicle vehicle = queryById(vehicleId);
        if(vehicle==null){
            System.out.println("该编号不存在！");
            return false;
        }

//        检查新编号是否已存在
        if (!vehicleId.equals(updatedVehicle.vehicleId)&&isIdExists(updatedVehicle.getVehicleId())){
            System.out.println("新编号已存在，修改失败！");
            return false;
        }
//        替换车辆
        int index=vehicles.indexOf(vehicle);
        vehicles.set(index,updatedVehicle);
        saveVehicles();
        return true;
    }
    private boolean isIdExists(String vehicleId) {
        return vehicles.stream()
                .anyMatch(v -> v.getVehicleId().equals(vehicleId));
    }

//    删除车辆
    public boolean deleteVehicle(String vehicleId){
        boolean removed=vehicles.removeIf(v ->v.getVehicleId().equals(vehicleId));
        if(removed){
            saveVehicles();
        }
        return removed;
    }

//    统计信息Statistics
    public void statistics(){
        System.out.println("车辆总数: "+vehicles.size());

        long busCount = vehicles.stream().filter(v -> v instanceof Bus).count();
        long carCount = vehicles.stream().filter(v -> v instanceof Car).count();
        long truckCount = vehicles.stream().filter(v -> v instanceof Truck).count();

        System.out.println("大客车数量: " + busCount);
        System.out.println("小轿车数量: " + carCount);
        System.out.println("卡车数量: " + truckCount);
    }
}
