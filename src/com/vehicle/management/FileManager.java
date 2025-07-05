package com.vehicle.management;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static final String FILE_PATH = "vehicles.csv";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    // 保存车辆列表到CSV文件
    public void saveVehicle(List<Vehicle> vehicles){
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(FILE_PATH))){
            writer.write("车辆ID,车牌号,制造商,购买日期,车辆类型,总公里数,油耗/公里,基本维护费,养路费,总费用,附加信息\\n");

            // 写入每辆车的数据
            for(Vehicle vehicle:vehicles){
                writer.write(formatVehicleToCsv(vehicle)+"\n");
            }

            System.out.println("车辆数据已保存到文件");
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("保存文件失败："+e.getMessage());
        }
    }

    // 从CSV文件加载车辆列表
    public List<Vehicle> loadVehicles(){
        List<Vehicle> vehicles=new ArrayList<>();
        File file=new File(FILE_PATH);

        if(!file.exists()){
            return vehicles;
        }

        try(BufferedReader reader=new BufferedReader(new FileReader(FILE_PATH))){
            String line;
            boolean isHeader=true;

            while((line=reader.readLine()) !=null){
                if(isHeader){
                    isHeader=false;
                    continue;
                }

                Vehicle vehicle=parserVehicleFromCsv(line);
                if(vehicle !=null){
                    vehicles.add(vehicle);
                }
            }

            System.out.println("成功从文件加载 "+vehicles.size()+" 辆车辆数据");
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("加载文件失败: "+e.getMessage());
        }

        return vehicles;
    }

    // 将车辆对象格式化为CSV行
    private String formatVehicleToCsv(Vehicle vehicle){
        return String.format("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f,%.2f,%s",
                vehicle.getVehicleId(),
                vehicle.getLicensePlate(),
                vehicle.getManufacturer(),
                vehicle.getPurchaseDate().format(DATE_FORMAT),
                vehicle.getVehicleType(),
                vehicle.getTotalKilometers(),
                vehicle.getFuelConsumption(),
                vehicle.getBasicMaintenanceFee(),
                vehicle.getRoadMaintenanceFee(),
                vehicle.getTotalCost(),
                vehicle.getAdditionalInfo());
    }

    // 从CSV行解析车辆对象
    private Vehicle parserVehicleFromCsv(String line){
        String[] parts=line.split(",");
        if(parts.length<11) return null;

        try{
            String vehicleId=parts[0];
            String licensePlate = parts[1];
            String manufacturer = parts[2];
            LocalDate purchaseDate = LocalDate.parse(parts[3], DATE_FORMAT);
            String vehicleType = parts[4];
            double totalKilometers = Double.parseDouble(parts[5]);
            double fuelConsumption = Double.parseDouble(parts[6]);
            double basicMaintenanceFee = Double.parseDouble(parts[7]);
            double roadMaintenanceFee = Double.parseDouble(parts[8]);
            double totalCost = Double.parseDouble(parts[9]);
            String additionalInfo = parts[10];

            Vehicle vehicle;

            switch(vehicleType){
                case "大客车":
                    int passengerCapacity=Integer.parseInt(
                            additionalInfo.replace("载客量:","").replace("人",""));
                    vehicle=new Bus(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, passengerCapacity);
                    break;

                case "小轿车":
                    int trunkNumber = Integer.parseInt(
                            additionalInfo.replace("箱数:", "").replace("厢", ""));
                    vehicle = new Car(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, trunkNumber);
                    break;

                case "卡车":
                    double loadCapacity = Double.parseDouble(
                            additionalInfo.replace("载重量:", "").replace("吨", ""));
                    vehicle = new Truck(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, loadCapacity);
                    break;

                default:
                    return null;
            }

            // 设置从文件中读取的维护费和养路费
            vehicle.setRoadMaintenanceFee(roadMaintenanceFee);
            return vehicle;
        } catch (Exception e) {
            System.err.println("解析车辆数据失败: " + line);
            e.printStackTrace();
            return null;
        }
    }
}
