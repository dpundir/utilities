package com.dpundir.utilities;


public class ElecBillMain {

	private static String[] months = {"AUG", "SEP", "OCT"};
	private static int[] demandUnit = {931, 1793, 897};
	private static double[] billedDemand = {4.80, 4.80, 4.80};
	private static double correctionFactor = 0.45;
	
	private static int ELEC_UNIT_RANGE_FIRST_MAX = 150;
	private static int ELEC_UNIT_RANGE_SECOND_MAX = 300;
	private static int ELEC_UNIT_RANGE_THIRD_MAX = 500;
	
	private static double ELEC_UNIT_RANGE_FIRST_CHARGE = 4.40;
	private static double ELEC_UNIT_RANGE_SECOND_CHARGE = 4.95;
	private static double ELEC_UNIT_RANGE_THIRD_CHARGE = 5.60;
	private static double ELEC_UNIT_RANGE_FOURTH_CHARGE = 6.20;
	
	private static double ELEC_FIXED_CHARGE = 90.0;
	private static double ELEC_SURCHARGE_FACTOR = 0.032;
	private static double ELEC_KVAH_FACTOR = 0.9;
	private static double ELEC_DUTY_FACTOR = 0.06;

	public static void main(String[] args){
		System.out.println(String.format("Calculating electricity bill difference of: %s", "UPPCL"));
		double elecBill = generateElecBill(false);
		double correctedElecBill = generateElecBill(true);
		System.out.println(String.format("Difference in bill: %f with current: %f and corrected: %f", elecBill-correctedElecBill, elecBill, correctedElecBill));
	}
	
	protected static double generateElecBill(boolean isCorrected){
		double correctedBill = 0, monthlyBill = 0, monthlyEnergyCharge = 0, monthlyFixedCharge = 0, monthlyElecDuty = 0, monthlyRegSurCharge = 0, monthlyDemandUnit = 0;
		for (int index = 0 ; index < 3 ; index++) {
			monthlyDemandUnit = isCorrected ? demandUnit[index] * correctionFactor : demandUnit[index];
			monthlyEnergyCharge = getMonthlyCharge(monthlyDemandUnit);
			monthlyFixedCharge = getFixedCharge(billedDemand[index]);
			monthlyElecDuty = getElectricityDuty(monthlyEnergyCharge);
			monthlyRegSurCharge = getElecSurcharge(monthlyEnergyCharge);
			monthlyBill = monthlyEnergyCharge + monthlyFixedCharge + monthlyElecDuty + monthlyRegSurCharge;
			correctedBill+= monthlyBill;
			System.out.println(String.format("Monthly bill for month: %s is: %f with energy charge: %f, fixed charge: %f, elec duty: %f, elec surcharge: %f", 
					months[index], monthlyBill, monthlyEnergyCharge, monthlyFixedCharge, monthlyElecDuty, monthlyRegSurCharge));
		}
		System.out.println(String.format("Cumulative bill: %f for: %d month(s)", correctedBill, months.length));
		return correctedBill;
	}
	
	protected static double getMonthlyCharge(double demandUnit){
		double monthlyEnergyCharge = 0;
		if (demandUnit <= ELEC_UNIT_RANGE_FIRST_MAX) {
			monthlyEnergyCharge = demandUnit * ELEC_UNIT_RANGE_FIRST_CHARGE;
		} else if (demandUnit >= (ELEC_UNIT_RANGE_FIRST_MAX + 1) && demandUnit <= ELEC_UNIT_RANGE_SECOND_MAX) {
			monthlyEnergyCharge = ELEC_UNIT_RANGE_FIRST_MAX * ELEC_UNIT_RANGE_FIRST_CHARGE + 
					(demandUnit - ELEC_UNIT_RANGE_FIRST_MAX) * ELEC_UNIT_RANGE_SECOND_CHARGE;
		} else if (demandUnit >= (ELEC_UNIT_RANGE_SECOND_MAX + 1) && demandUnit <= ELEC_UNIT_RANGE_THIRD_MAX) {
			monthlyEnergyCharge = ELEC_UNIT_RANGE_FIRST_MAX * ELEC_UNIT_RANGE_FIRST_CHARGE + 
					(ELEC_UNIT_RANGE_SECOND_MAX - ELEC_UNIT_RANGE_FIRST_MAX) * ELEC_UNIT_RANGE_SECOND_CHARGE + 
					(demandUnit - ELEC_UNIT_RANGE_SECOND_MAX) * ELEC_UNIT_RANGE_THIRD_CHARGE;
		} else {
			monthlyEnergyCharge = ELEC_UNIT_RANGE_FIRST_MAX * ELEC_UNIT_RANGE_FIRST_CHARGE + 
					(ELEC_UNIT_RANGE_SECOND_MAX - ELEC_UNIT_RANGE_FIRST_MAX) * ELEC_UNIT_RANGE_SECOND_CHARGE + 
					(ELEC_UNIT_RANGE_THIRD_MAX - ELEC_UNIT_RANGE_SECOND_MAX) * ELEC_UNIT_RANGE_THIRD_CHARGE + 
					(demandUnit - ELEC_UNIT_RANGE_THIRD_MAX) * ELEC_UNIT_RANGE_FOURTH_CHARGE;
		}
		return monthlyEnergyCharge;
	}
	
	protected static double getFixedCharge(double billedDemand){
		return billedDemand * ELEC_FIXED_CHARGE;
	}
	
	protected static double getElectricityDuty(double elecCharge){
		return elecCharge * ELEC_KVAH_FACTOR * ELEC_DUTY_FACTOR;
	}
	
	protected static double getElecSurcharge(double elecCharge){
		return elecCharge * ELEC_SURCHARGE_FACTOR;
	}
}
