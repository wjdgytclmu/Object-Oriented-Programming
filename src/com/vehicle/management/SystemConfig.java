package com.vehicle.management;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public interface SystemConfig {
//    默认油价（元/升）
    double DEFAULT_OIL_PRICE =8.0;

//    最大车辆数量限制
    int MAX_VEHICLES =100;

//    配置文件路径
    String CONFIG_FILE="config.properties";

//    从配置文件中加载油价
    static double getOilPrice() {
        Properties properties = new Properties();
        try (InputStream is = SystemConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
                return Double.parseDouble(properties.getProperty("oil.price", String.valueOf(DEFAULT_OIL_PRICE)));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("加载油价配置失败，使用默认值");
    }
    return DEFAULT_OIL_PRICE;
    }

    // 保存油价到配置文件
    static void saveOilPrice(double oilPrice) {
        Properties properties = new Properties();
        try (InputStream is = SystemConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            System.err.println("读取配置文件失败，创建新配置");
        }

        properties.setProperty("oil.price", String.valueOf(oilPrice));

        try (java.io.OutputStream os = new java.io.FileOutputStream(CONFIG_FILE)) {
            properties.store(os, "车辆管理系统配置");
        } catch (IOException e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
        }
    }

    // 从配置文件加载最大车辆数量
    static int getMaxVehicles() {
        Properties properties = new Properties();
        try (InputStream is = SystemConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
                return Integer.parseInt(properties.getProperty("max.vehicles", String.valueOf(MAX_VEHICLES)));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("加载最大车辆数量配置失败，使用默认值");
        }
        return MAX_VEHICLES;
    }

    // 保存最大车辆数量到配置文件
    static void saveMaxVehicles(int maxVehicles) {
        Properties properties = new Properties();
        try (InputStream is = SystemConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            System.err.println("读取配置文件失败，创建新配置");
        }

        properties.setProperty("max.vehicles", String.valueOf(maxVehicles));

        try (java.io.OutputStream os = new java.io.FileOutputStream(CONFIG_FILE)) {
            properties.store(os, "车辆管理系统配置");
        } catch (IOException e) {
            System.err.println("保存配置文件失败: " + e.getMessage());
        }
    }
}
