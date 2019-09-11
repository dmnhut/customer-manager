package com;

import java.io.*;
import java.util.Scanner;

public class CustomerManager {
    private static final String FILE_NAME = "/home/data/customer.db";
    private static final String FILE_TEMP = "/home/data/customer_temp.db";

    public static void main(String[] __) {
        mainMenu();
    }

    public static void goTo(String key) {
        Scanner scan = new Scanner(System.in);
        switch (key.trim().toUpperCase()) {
            case "E":
                mainMenu();
                break;
            case "C":
                System.out.println("==================================================================================>");
                System.out.println("New customer");
                createView();
                break;
            case "L":
                System.out.println("==================================================================================>");
                System.out.println("List customer");
                listView();
                break;
            case "F":
                System.out.println("==================================================================================>");
                System.out.print("Find customer with id = ");
                findView(scan.nextLine().trim());
                break;
            case "U":
                System.out.println("==================================================================================>");
                System.out.print("Customer revenue plus = ");
                updateCustomer(Customer.id, scan.nextLong());
                break;
            case "D":
                System.out.println("==================================================================================>");
                deleteCustomer(Customer.id);
                break;
            case "Q":
                System.out.println("\nExit done.");
                System.exit(0);
                break;
            default:
                break;
        }
    }

    public static void mainMenu() {
        Scanner scan = new Scanner(System.in);
        System.out.println("<=================================================================================");
        System.out.println("Add new customer (C) | Shown list customer (L) | Find customer by id (F) | Exit (Q)");
        System.out.println("==================================================================================>");
        goTo(scan.nextLine());
    }

    public static void createView() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Customer id = ");
        Customer.id = scan.nextLine();
        System.out.print("Customer name = ");
        Customer.name = scan.nextLine();
        System.out.print("Customer address = ");
        Customer.address = scan.nextLine();
        System.out.print("Customer revenue = ");
        Customer.revenue = scan.nextLong();
        try {
            DataOutputStream data = new DataOutputStream(new FileOutputStream(FILE_NAME, true));
            data.writeUTF(Customer.id);
            data.writeUTF(Customer.name);
            data.writeUTF(Customer.address);
            data.writeLong(Customer.revenue);
            data.flush();
            data.close();
            System.out.println("Wrote customer");
            System.out.println(Customer.id + " -- " + Customer.name + " -- " + Customer.address + " -- " + Customer.revenue);
        } catch (IOException ie) {
            System.out.println(ie);
        }
        System.out.println("==================================================================================>");
        System.out.println("Add new customer (C) | Main menu (E)");
        scan = new Scanner(System.in);
        goTo(scan.nextLine());
    }

    public static void listView() {
        Scanner scan = new Scanner(System.in);
        int count = 0;
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(FILE_NAME));
            while (data.available() > 0) {
                Customer.id = data.readUTF();
                Customer.name = data.readUTF();
                Customer.address = data.readUTF();
                Customer.revenue = data.readLong();
                System.out.println(Customer.id + " -- " + Customer.name + " -- " + Customer.address + " -- " + Customer.revenue);
                count++;
                if (count == 5 && data.available() != 0) {
                    count = 0;
                    System.out.print("Enter to print next item ...");
                    if (scan.nextLine().trim().compareTo("") != 0) {
                        mainMenu();
                        break;
                    }
                }
            }
            data.close();
        } catch (IOException ie) {
            System.out.println(ie);
        }
        System.out.print("Enter to return main menu");
        if (scan.nextLine().trim().compareTo("") == 0) {
            mainMenu();
        }
    }

    public static void findView(String id) {
        Scanner scan = new Scanner(System.in);
        try {
            DataInputStream data = new DataInputStream(new FileInputStream(FILE_NAME));
            while (true) {
                try {
                    if (data.available() == 0) {
                        System.out.println("Not find customer with id = " + id);
                        goTo("E");
                        break;
                    }
                    Customer.id = data.readUTF();
                    Customer.name = data.readUTF();
                    Customer.address = data.readUTF();
                    Customer.revenue = data.readLong();
                    if (Customer.id.compareTo(id) == 0) {
                        System.out.println(Customer.id + " -- " + Customer.name + " -- " + Customer.address + " -- " + Customer.revenue);
                        System.out.println("=================================================================================>");
                        System.out.println("Update customer (U) | Delete customer (D) | Main menu (E)");
                        goTo(scan.nextLine().trim().toUpperCase());
                    }
                } catch (IOException ie) {
                    System.out.println(ie);
                }
            }
            data.close();
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public static void updateCustomer(String id, long revenue) {
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(FILE_NAME));
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(FILE_TEMP));
            while (true) {
                try {
                    if (dataInput.available() == 0) {
                        break;
                    }
                    Customer.id = dataInput.readUTF();
                    Customer.name = dataInput.readUTF();
                    Customer.address = dataInput.readUTF();
                    Customer.revenue = dataInput.readLong();
                    if (Customer.id.compareTo(id) == 0) {
                        Customer.revenue += revenue;
                    }
                    dataOutput.writeUTF(Customer.id);
                    dataOutput.writeUTF(Customer.name);
                    dataOutput.writeUTF(Customer.address);
                    dataOutput.writeLong(Customer.revenue);
                    dataOutput.flush();
                } catch (IOException ie) {
                    System.out.println(ie);
                }
            }
            dataInput.close();
            dataOutput.close();
            File oldData = new File(FILE_NAME);
            oldData.delete();
            File newData = new File(FILE_TEMP);
            newData.renameTo(oldData);
            findView(id);
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    public static void deleteCustomer(String id) {
        try {
            DataInputStream dataInput = new DataInputStream(new FileInputStream(FILE_NAME));
            DataOutputStream dataOutput = new DataOutputStream(new FileOutputStream(FILE_TEMP));
            while (true) {
                try {
                    if (dataInput.available() == 0) {
                        break;
                    }
                    Customer.id = dataInput.readUTF();
                    Customer.name = dataInput.readUTF();
                    Customer.address = dataInput.readUTF();
                    Customer.revenue = dataInput.readLong();
                    if (Customer.id.compareTo(id) == 0) {
                        continue;
                    }
                    dataOutput.writeUTF(Customer.id);
                    dataOutput.writeUTF(Customer.name);
                    dataOutput.writeUTF(Customer.address);
                    dataOutput.writeLong(Customer.revenue);
                    dataOutput.flush();
                } catch (IOException ie) {
                    System.out.println(ie);
                }
            }
            dataInput.close();
            dataOutput.close();
            File oldData = new File(FILE_NAME);
            oldData.delete();
            File newData = new File(FILE_TEMP);
            newData.renameTo(oldData);
            System.out.println("Deleted customer with id = " + Customer.id);
            goTo("F");
        } catch (IOException ie) {
            System.out.println(ie);
        }
    }

    static class Customer {
        static String id;
        static String name;
        static String address;
        static long revenue;
    }
}
