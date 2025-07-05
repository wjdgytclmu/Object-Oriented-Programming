package com.vehicle.management;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

// 车辆管理系统GUI
public class VehicleManagementGUI extends JFrame {
    private final VehicleManager manager = new VehicleManager();
    private JTextArea resultArea;

    public VehicleManagementGUI(){
        setTitle("车辆管理系统");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setVisible(true);
    }

    private void initComponents(){
//        创建主面板
        JPanel mainPanel=new JPanel(new BorderLayout());

//        创建顶部菜单
        JMenuBar menuBar=createMenuBar();
        setJMenuBar(menuBar);

//        创建结果显示区域
        resultArea=new JTextArea(20,60);
        resultArea.setEditable(false);
        JScrollPane scrollPane=new JScrollPane(resultArea);

        mainPanel.add(scrollPane,BorderLayout.CENTER);
        add(mainPanel);
    }

    private JMenuBar createMenuBar(){
        JMenuBar menuBar=new JMenuBar();

//        文件菜单
        JMenu fileMenu=new JMenu("文件");
        JMenuItem exitItem=new JMenuItem("退出");
        exitItem.addActionListener(e ->System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

//        车辆管理菜单
        JMenu vehicleMenu=new JMenu("车辆管理");

        JMenuItem addItem=new JMenuItem("添加车辆");
        addItem.addActionListener(e ->showAddVehicleDialog());
        vehicleMenu.add(addItem);

        JMenuItem queryItem = new JMenuItem("查询车辆");
        queryItem.addActionListener(e -> showQueryDialog());
        vehicleMenu.add(queryItem);

        JMenuItem displayAllItem = new JMenuItem("显示所有车辆");
        displayAllItem.addActionListener(e -> displayAllVehicles());
        vehicleMenu.add(displayAllItem);

        JMenuItem editItem = new JMenuItem("编辑车辆");
        editItem.addActionListener(e -> showEditDialog());
        vehicleMenu.add(editItem);

        JMenuItem deleteItem = new JMenuItem("删除车辆");
        deleteItem.addActionListener(e -> showDeleteDialog());
        vehicleMenu.add(deleteItem);

        JMenuItem statsItem = new JMenuItem("统计信息");
        statsItem.addActionListener(e -> showStatistics());
        vehicleMenu.add(statsItem);

        menuBar.add(vehicleMenu);

        JMenu configMenu=new JMenu("系统配置");
        JMenuItem oilPriceItem = new JMenuItem("修改油价");
        oilPriceItem.addActionListener(e -> showOilPriceDialog());
        JMenuItem maxVehicleTItem=new JMenuItem("修改最大车辆限制");
        maxVehicleTItem.addActionListener(e ->showMaxVehicleDialog());
        configMenu.add(oilPriceItem);
        configMenu.add(maxVehicleTItem);
        menuBar.add(configMenu);

        return menuBar;
    }

    private void showAddVehicleDialog(){
        JDialog dialog = new JDialog(this, "添加车辆", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField idField = new JTextField();
        JTextField licenseField = new JTextField();
        JTextField manufacturerField = new JTextField();
        JTextField purchaseDateField = new JTextField("YYYY-MM-DD");
        JTextField kilometersField = new JTextField();
        JTextField fuelConsumptionField = new JTextField();
        JTextField roadMaintenanceField = new JTextField();

        JComboBox<String> typeComboBox=new JComboBox<>(new String[]{"大客车","小轿车","卡车"});
        JTextField additionalInfoField=new JTextField();

        panel.add(new JLabel("车辆编号:"));
        panel.add(idField);
        panel.add(new JLabel("车牌号:"));
        panel.add(licenseField);
        panel.add(new JLabel("制造商:"));
        panel.add(manufacturerField);
        panel.add(new JLabel("购买日期:"));
        panel.add(purchaseDateField);
        panel.add(new JLabel("总公里数:"));
        panel.add(kilometersField);
        panel.add(new JLabel("油耗/公里:"));
        panel.add(fuelConsumptionField);
        panel.add(new JLabel("养路费:"));
        panel.add(roadMaintenanceField);
        panel.add(new JLabel("车辆类型:"));
        panel.add(typeComboBox);

        typeComboBox.addActionListener(e -> {
            String type = (String) typeComboBox.getSelectedItem();
            if (type.equals("大客车")) {
                additionalInfoField.setToolTipText("请输入载客量（人）");
            } else if (type.equals("小轿车")) {
                additionalInfoField.setToolTipText("请输入箱数（2或3）");
            } else {
                additionalInfoField.setToolTipText("请输入载重量（吨）");
            }
        });

        panel.add(new JLabel("附加信息:"));
        panel.add(additionalInfoField);

        JButton addButton = new JButton("添加");
        addButton.addActionListener(e -> {
            try {
                String vehicleId = idField.getText();
                String licensePlate = licenseField.getText();
                String manufacturer = manufacturerField.getText();
                LocalDate purchaseDate = LocalDate.parse(purchaseDateField.getText());
                double totalKilometers = Double.parseDouble(kilometersField.getText());
                double fuelConsumption = Double.parseDouble(fuelConsumptionField.getText());
                double roadMaintenanceFee = Double.parseDouble(roadMaintenanceField.getText());
                String vehicleType = (String) typeComboBox.getSelectedItem();

                Vehicle vehicle;
                if (vehicleType.equals("大客车")) {
                    int passengerCapacity = Integer.parseInt(additionalInfoField.getText());
                    vehicle = new Bus(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, passengerCapacity);
                } else if (vehicleType.equals("小轿车")) {
                    int trunkNumber = Integer.parseInt(additionalInfoField.getText());
                    vehicle = new Car(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, trunkNumber);
                } else {
                    double loadCapacity = Double.parseDouble(additionalInfoField.getText());
                    vehicle = new Truck(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, loadCapacity);
                }

                // 设置养路费
                vehicle.setRoadMaintenanceFee(roadMaintenanceFee);

                if (manager.addVehicle(vehicle)) {
                    JOptionPane.showMessageDialog(dialog, "车辆添加成功！");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "车辆添加失败！");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "输入有误: " + ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showQueryDialog(){
        JDialog dialog = new JDialog(this, "查询车辆", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JComboBox<String> queryTypeComboBox = new JComboBox<>(
                new String[]{"按编号查询", "按制造商查询", "按类型查询"});
        JTextField queryValueField = new JTextField();

        panel.add(new JLabel("查询方式:"));
        panel.add(queryTypeComboBox);
        panel.add(new JLabel("查询值:"));
        panel.add(queryValueField);

        JButton queryButton = new JButton("查询");
        queryButton.addActionListener(e -> {
            try {
                String selectedType = (String) queryTypeComboBox.getSelectedItem();
                String queryValue = queryValueField.getText();

                StringBuilder result = new StringBuilder();

                if (selectedType.equals("按编号查询")) {
                    Vehicle vehicle = manager.queryById(queryValue);
                    if (vehicle != null) {
                        result.append(vehicle.toString());
                    } else {
                        result.append("该编号不存在！");
                    }
                } else if (selectedType.equals("按制造商查询")) {
                    List<Vehicle> vehicles = manager.queryByManufacturer(queryValue);
                    if (!vehicles.isEmpty()) {
                        for (Vehicle vehicle : vehicles) {
                            result.append(vehicle.toString()).append("\n\n");
                        }
                    } else {
                        result.append("该制造商不存在车辆！");
                    }
                } else {
                    List<Vehicle> vehicles = manager.queryByType(queryValue);
                    if (!vehicles.isEmpty()) {
                        for (Vehicle vehicle : vehicles) {
                            result.append(vehicle.toString()).append("\n\n");
                        }
                    } else {
                        result.append("该类型不存在车辆！");
                    }
                }

                resultArea.setText(result.toString());
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "查询失败: " + ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(queryButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void displayAllVehicles(){
        List<Vehicle> vehicles = manager.getAllVehicles();
        StringBuilder result = new StringBuilder();

        if (vehicles.isEmpty()) {
            result.append("车辆信息库为空！");
        } else {
            for (Vehicle vehicle : vehicles) {
                result.append(vehicle.toString()).append("\n\n");
            }
        }

        for (Vehicle vehicle : vehicles) {
            result.append(vehicle.toString()).append("\n\n"); // 调用最新的toString()
        }

        resultArea.setText(result.toString());
    }

    private void showEditDialog() {
        JDialog dialog = new JDialog(this, "编辑车辆", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField idField = new JTextField();
        panel.add(new JLabel("车辆编号:"));
        panel.add(idField);

        JButton searchButton = new JButton("查找");
        searchButton.addActionListener(e -> {
            String vehicleId = idField.getText();
            Vehicle vehicle = manager.queryById(vehicleId);

            if (vehicle == null) {
                JOptionPane.showMessageDialog(dialog, "该编号不存在！");
                return;
            }

            dialog.dispose();
            showEditVehicleDialog(vehicle);
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditVehicleDialog(Vehicle vehicle) {
        JDialog dialog = new JDialog(this, "编辑车辆信息", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField idField = new JTextField(vehicle.getVehicleId());
        JTextField licenseField = new JTextField(vehicle.getLicensePlate());
        JTextField manufacturerField = new JTextField(vehicle.getManufacturer());
        JTextField purchaseDateField = new JTextField(
                vehicle.getPurchaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        JTextField kilometersField = new JTextField(String.valueOf(vehicle.getTotalKilometers()));
        JTextField fuelConsumptionField = new JTextField(String.valueOf(vehicle.getFuelConsumption()));
        JTextField roadMaintenanceField = new JTextField(String.valueOf(vehicle.getRoadMaintenanceFee()));

        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"大客车", "小轿车", "卡车"});
        typeComboBox.setSelectedItem(vehicle.getVehicleType());

        JTextField additionalInfoField = new JTextField();

        // 设置附加信息
        if (vehicle instanceof Bus) {
            additionalInfoField.setText(String.valueOf(((Bus) vehicle).getPassengerCapacity()));
        } else if (vehicle instanceof Car) {
            additionalInfoField.setText(String.valueOf(((Car) vehicle).getTrunkNumber()));
        } else {
            additionalInfoField.setText(String.valueOf(((Truck) vehicle).getLoadCapacity()));
        }

        panel.add(new JLabel("车辆编号:"));
        panel.add(idField);
        panel.add(new JLabel("车牌号:"));
        panel.add(licenseField);
        panel.add(new JLabel("制造商:"));
        panel.add(manufacturerField);
        panel.add(new JLabel("购买日期:"));
        panel.add(purchaseDateField);
        panel.add(new JLabel("总公里数:"));
        panel.add(kilometersField);
        panel.add(new JLabel("油耗/公里:"));
        panel.add(fuelConsumptionField);
        panel.add(new JLabel("养路费:"));
        panel.add(roadMaintenanceField);
        panel.add(new JLabel("车辆类型:"));
        panel.add(typeComboBox);

        typeComboBox.addActionListener(e -> {
            String type = (String) typeComboBox.getSelectedItem();
            if (type.equals("大客车")) {
                additionalInfoField.setToolTipText("请输入载客量（人）");
            } else if (type.equals("小轿车")) {
                additionalInfoField.setToolTipText("请输入箱数（2或3）");
            } else {
                additionalInfoField.setToolTipText("请输入载重量（吨）");
            }
        });

        panel.add(new JLabel("附加信息:"));
        panel.add(additionalInfoField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                String originalId = vehicle.getVehicleId();
                String vehicleId = idField.getText();
                String licensePlate = licenseField.getText();
                String manufacturer = manufacturerField.getText();
                LocalDate purchaseDate = LocalDate.parse(purchaseDateField.getText());
                double totalKilometers = Double.parseDouble(kilometersField.getText());
                double fuelConsumption = Double.parseDouble(fuelConsumptionField.getText());
                double roadMaintenanceFee = Double.parseDouble(roadMaintenanceField.getText());
                String vehicleType = (String) typeComboBox.getSelectedItem();

                Vehicle updatedVehicle;
                if (vehicleType.equals("大客车")) {
                    int passengerCapacity = Integer.parseInt(additionalInfoField.getText());
                    updatedVehicle = new Bus(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, passengerCapacity);
                } else if (vehicleType.equals("小轿车")) {
                    int trunkNumber = Integer.parseInt(additionalInfoField.getText());
                    updatedVehicle = new Car(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, trunkNumber);
                } else {
                    double loadCapacity = Double.parseDouble(additionalInfoField.getText());
                    updatedVehicle = new Truck(vehicleId, licensePlate, manufacturer, purchaseDate,
                            totalKilometers, fuelConsumption, loadCapacity);
                }

                // 设置养路费
                updatedVehicle.setRoadMaintenanceFee(roadMaintenanceFee);

                if (manager.editVehicle(originalId, updatedVehicle)) {
                    JOptionPane.showMessageDialog(dialog, "车辆信息更新成功！");
                    dialog.dispose();
                    displayAllVehicles();
                } else {
                    JOptionPane.showMessageDialog(dialog, "车辆信息更新失败！");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "输入有误: " + ex.getMessage());
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showDeleteDialog(){
        JDialog dialog = new JDialog(this, "删除车辆", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField idField = new JTextField();
        panel.add(new JLabel("车辆编号:"));
        panel.add(idField);

        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            String vehicleId = idField.getText();
            if (manager.deleteVehicle(vehicleId)) {
                JOptionPane.showMessageDialog(dialog, "车辆删除成功！");
                dialog.dispose();
                displayAllVehicles();
            } else {
                JOptionPane.showMessageDialog(dialog, "车辆删除失败！");
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showStatistics(){
        StringBuilder result = new StringBuilder();
        List<Vehicle> vehicles = manager.getAllVehicles();

        result.append("车辆总数: ").append(vehicles.size()).append("\n");

        long busCount = vehicles.stream().filter(v -> v instanceof Bus).count();
        long carCount = vehicles.stream().filter(v -> v instanceof Car).count();
        long truckCount = vehicles.stream().filter(v -> v instanceof Truck).count();

        result.append("大客车数量: ").append(busCount).append("\n");
        result.append("小轿车数量: ").append(carCount).append("\n");
        result.append("卡车数量: ").append(truckCount).append("\n");

        // 计算平均油耗
        double avgFuelConsumption = vehicles.stream()
                .mapToDouble(Vehicle::getFuelConsumption)
                .average()
                .orElse(0);

        result.append("平均油耗: ").append(String.format("%.2f", avgFuelConsumption)).append(" 升/公里\n");

        // 显示当前油价
        result.append("当前油价: ").append(SystemConfig.getOilPrice()).append(" 元/升\n");

        // 显示最大车辆数量
        result.append("最大车辆数量限制: ").append(SystemConfig.MAX_VEHICLES);

        resultArea.setText(result.toString());
    }

    private void showOilPriceDialog(){
        JDialog dialog = new JDialog(this, "修改油价", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField priceField = new JTextField(String.valueOf(SystemConfig.getOilPrice()), 10);

        panel.add(new JLabel("当前油价 (元/升):"));
        panel.add(priceField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                double newPrice = Double.parseDouble(priceField.getText());
                SystemConfig.saveOilPrice(newPrice);

                // 关键：重新计算所有车辆的费用
                for (Vehicle vehicle : manager.getAllVehicles()) {
                    vehicle.calculateTotalCost(); // 触发费用重新计算
                }

                // 保存更新后的数据到文件
                manager.saveVehicles();

                JOptionPane.showMessageDialog(dialog, "油价修改成功！");
                dialog.dispose();
                displayAllVehicles();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的数字！");
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showMaxVehicleDialog(){
        JDialog dialog = new JDialog(this, "修改最大车辆限制", true);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField maxVehicleField = new JTextField(String.valueOf(SystemConfig.getMaxVehicles()), 10);

        panel.add(new JLabel("当前最大车辆 (辆):"));
        panel.add(maxVehicleField);

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            try {
                double maxVehicle = Double.parseDouble(maxVehicleField.getText());
                SystemConfig.saveMaxVehicles((int) maxVehicle);
                SystemConfig.getMaxVehicles();
                JOptionPane.showMessageDialog(dialog, "最大车辆限制修改成功！");
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的数字！");
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleManagementGUI::new);
    }
}