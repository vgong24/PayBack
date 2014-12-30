package com.example.victor.payback;

/**
 * Created by Victor on 12/22/2014.
 */
public class DebtProfile {
    private static final String SPOT = "spotted";
    private static final String OWE = "owe";
    private String _name, _phone, _description, _debt;
    private int _id;
    private double _amount;

    public DebtProfile( int pid, String name, double amount, String debt, String phone, String description) {
        _id = pid;
        _name = name;
        _amount = amount;
        _debt = debt;
        _phone = phone;
        _description = description;
    }


    public int getID(){ return _id;}

    public String getName(){ return _name;}

    public String getPhone(){return _phone;}

    public double getAmount(){return _amount;}

    public String getDebtType(){return _debt;}

    public String getDescription(){return _description;}

    public boolean isSpot(){
        if(_debt.equalsIgnoreCase(SPOT)){
            return true;
        }
        return false;
    }
    public boolean isOwe(){ return !isSpot();}

}
