package com.eulerproject.modifiedcollatz;

/**
 * This Class is used to hold expressions of the form ax+b/c which are central to computations in the collatz 
 * conjecture
 * 
 * @author Suhas Shetty
 *
 */
public class CollatzExpression {
	private int numeratorCoefficient;
	private int constantCoefficient;
	private int denominatorCoefficient;
	
	public CollatzExpression(int numeratorCoefficient,int constantCoefficient,int denominatorCoefficient) {
		this.numeratorCoefficient = numeratorCoefficient;
		this.constantCoefficient = constantCoefficient;
		this.denominatorCoefficient = denominatorCoefficient;
	}
	
	public int getNumeratorCoefficient() {
		return numeratorCoefficient;
	}
	
	public void setNumeratorCoefficient(int numeratorCoefficient) {
		this.numeratorCoefficient = numeratorCoefficient;
	}
	
	public int getConstantCoefficient() {
		return constantCoefficient;
	}
	
	public void setConstantCoefficient(int constantCoefficient) {
		this.constantCoefficient = constantCoefficient;
	}
	
	public int getDenominatorConumeratorCoefficientefficient() {
		return denominatorCoefficient;
	}
	
	public void setDenominatorCoefficient(int denominatorCoefficient) {
		this.denominatorCoefficient = denominatorCoefficient;
	}
 
	/**
	 * Calculates the expression result (ax+b)/c based its coefficients
	 * 
	 * @param input
	 * @return
	 */
	public Long calculateExpressionValue(Long input) {
		return ((numeratorCoefficient * input + constantCoefficient)/denominatorCoefficient);
	}
}
