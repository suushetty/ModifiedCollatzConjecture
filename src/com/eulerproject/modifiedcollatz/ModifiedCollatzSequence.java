package com.eulerproject.modifiedcollatz;

import java.math.BigInteger;

/**
 * Solution Project Euler Problem 277 Modified Collatz Conjecture
 * The problem is to calculate a number above a certain bound that follows a known sequence in the Modified
 * Collatz Conjecture
 * 
 * The solution involves creating a generic expression for k levels of the sequence and calculating the coefficients
 * based on this general formula to get a equation of the form aX + bY = C, which we solve using euclid's extended 
 * algorithm 
 * 
 * The generic expression looks like (Ax + B)/C , 
 * where A is the numerator sum, B is the constant sum and C is the denominator result
 * 
 * @author Suhas Shetty
 *
 */
public class ModifiedCollatzSequence {

	//Stores the coefficients for the constant term of the generic expression
	private Integer[] constantCoeffArray;

	//Stores the coefficients for the numerator multiplier term of the generic expression
	private Integer[] numeratorCoeffArray;

	private int index = 0;
	private int upwardStepCount = 0;
	private int downwardStepCount = 0;

	// These are the values that will be calculated from the generic expression
	private Double expressionNumeratorSum = 0d;
	private Double expressionConstantSum = 0d;
	private Double expressionDenominator = 0d;

	private CollatzExpression upwardExpression;
	private CollatzExpression downwardExpression;
	private CollatzExpression bigDownwardExpression;

	private String sequence = null;

	/**
	 * Constructor for the class
	 * 
	 * @param sequence
	 */
	public ModifiedCollatzSequence(String sequence) {
		this.sequence = sequence;
		constantCoeffArray = new Integer[sequence.length()];
		numeratorCoeffArray = new Integer[sequence.length()];

		//Initializing the collatz expression coefficients
		bigDownwardExpression = new CollatzExpression(1, 0, 3);
		upwardExpression = new CollatzExpression(4, 2, 3);
		downwardExpression = new CollatzExpression(2, -1, 3);
	}

	/**
	 * This method stores collatz expression coeffs into an array. These values come from the expression
	 * for the modified Collatz Conjecture. i.e
	 * We treat the Collatz operation to be performed as (ax + b)/c, where
	 * a - multiplier
	 * b - constant
	 * c - denominator - 3 (Since its constant across all the operations)
	 * 
	 * For each of the steps in the sequence consisting of 'U', 'D', 'd', we store the expression coeffs into 
	 * respective arrays
	 * 
	 * @throws Exception 
	 */
	private void storeSequenceConstants() throws Exception {
		for(char sequenceStep: sequence.toCharArray()){
			switch (sequenceStep){
			case 'U' : constantCoeffArray[index] = upwardExpression.getConstantCoefficient();
			numeratorCoeffArray[index++] = upwardExpression.getNumeratorCoefficient();
			upwardStepCount++; break;
			case 'd' : constantCoeffArray[index] = downwardExpression.getConstantCoefficient(); 
			numeratorCoeffArray[index++] = downwardExpression.getNumeratorCoefficient();
			downwardStepCount++;break;
			case 'D' : constantCoeffArray[index] = bigDownwardExpression.getConstantCoefficient();
			numeratorCoeffArray[index++] = bigDownwardExpression.getNumeratorCoefficient();
			break;
			default : 
				throw new Exception("Prefix Sequence incorrect ");
			}
		}
	}

	/**
	 * This method performs prefix calculation for modified collatz conjecture.
	 * Given a sequence like "DdDddUUdDD....", we first perform the modified Collatz conjecture assuming an initial x.
	 * Based on the expression, we can derive that
	 * After k steps, we will have
	 * Sum = (A (x) + B ) /C
	 *    where                          
	 * A = a1 ^ U * a2 ^ d  * a3 ^ D , where a1 = 4, a2 = 2, a3 = 1 and U,D,d represent the number of times they occur
	 *     in the sequence
	 * B = Sigma[ Pi[multiplierCoeffArray[j]] * constantCoeffArray[i]) * 3^i], i->k-1,0 ; j ->1, k-1-i 
	 * C = 3 ^ k, 
	 *
	 * @param boundRange
	 */
	private Long calcCollatzExpressionResult(Long boundRange) {
		
		int sequenceLength = sequence.length();
		expressionConstantSum = 0d;
		for(int outer_index=sequenceLength-1; outer_index >= 0; outer_index--){
			Long product = 1L;
			for(int inner_index=1; inner_index <= (sequenceLength-1-outer_index); inner_index++) {
				product *= numeratorCoeffArray[sequenceLength - inner_index];
			}
			expressionConstantSum += product * Math.pow(3, outer_index) * constantCoeffArray[outer_index];
		}
		expressionNumeratorSum = Math.pow(4,upwardStepCount) * Math.pow(2,downwardStepCount);
		expressionDenominator = Math.pow(3,sequenceLength);
		return calculateResultThroughGCDComputation(expressionNumeratorSum, -expressionDenominator,
				-expressionConstantSum, boundRange);
	}

	/**
	 * Once we compute Ax + B / C as described above, we have gone through the provided input sequence. We want the 
	 * smallest value for x greater than a starting Bound (Z) such that Ax + B / C results in an integer Y. This would 
	 * ensure that the integer sequence is valid. We find integral solutions for x and Y using the Extended Euclid
	 * algorithm for computing GCD. We obtain equations of the form x = p + q * T , Y = m + n* T, where p, q, m, n are
	 * constants obtained from the GCD and the original equation's coefficients. We can substitute any integer value
	 * for T in order to obtain a valid solution for x. We can choose a good starting point for T by using Z as x, 
	 * and then T is either (starting point - 1)  or (starting point + 1)
	 * 
	 * @param inputCoeffA
	 * @param inputCoeffB
	 * @param inputCoeffC
	 * @param inputBound
	 * @return
	 */
	private Long calculateResultThroughGCDComputation(Double inputCoeffA, Double inputCoeffB, Double inputCoeffC, Long inputBound){		
		//Check if equation coefficients are negative
		boolean coeffANegative = inputCoeffA < 0;
		boolean coeffBNegative = inputCoeffB < 0;

		// Storing the coefficients of the input equation as a BigInteger
		BigInteger coeffA = BigInteger.valueOf(Math.abs(inputCoeffA.longValue()));
		BigInteger coeffB = BigInteger.valueOf(Math.abs(inputCoeffB.longValue()));
		BigInteger constantC = BigInteger.valueOf(inputCoeffC.longValue());
		
		BigInteger coeffATemp = coeffA;
		BigInteger coeffBTemp = coeffB;
		BigInteger lowerBound = BigInteger.valueOf(inputBound);

		BigInteger currentX = new BigInteger("0");
		BigInteger currentY = new BigInteger("1");
		BigInteger previousX = new BigInteger("1");
		BigInteger previousY = new BigInteger("0");
		BigInteger temp;

		BigInteger remainder;
		BigInteger quotient;
		
		BigInteger solution;
		BigInteger startingValue;
		BigInteger xVar;
		
		// Extended Euclid Algorithm to calculate GCD as well as the expression of GCD in terms of input coeffs
		while (!coeffBTemp.equals(BigInteger.ZERO)) {
			quotient = coeffATemp.divide(coeffBTemp);
			remainder = coeffATemp.mod(coeffBTemp);
			coeffATemp = coeffBTemp;
			coeffBTemp = remainder;

			temp = currentX;
			currentX = previousX.subtract(quotient.multiply(currentX));
			previousX = temp;
			temp = currentY;
			currentY = previousY.subtract(quotient.multiply(currentY));
			previousY = temp;            
		}
		
		// If coefficients were negative then transferring the sign to the roots of the equation
		if(coeffANegative){
			previousX = BigInteger.ZERO.subtract(previousX);
		}
		if(coeffBNegative){
			previousY = BigInteger.ZERO.subtract(previousY);
		}

		//The coefficient of the variable part of the generic equation for all solutions of X
		//i.e if x = p + q * T, this represents q;
		xVar = coeffB.divide(coeffATemp);

		//Scale the X and Y values to account for the constant in the equation
		previousX = previousX.multiply(constantC);
		previousY = previousY.multiply(constantC);

		startingValue = (lowerBound.subtract(previousX)).divide(xVar);
		
		// Calculate the generic equation for all solutions of X using the initialValue and xVar(coeffB/GCD)
		solution = previousX.add(xVar.multiply(startingValue));
		
		startingValue = startingValue.add(BigInteger.ONE);
		
		while(solution.compareTo(lowerBound) == -1) {
			solution = previousX.add(xVar.multiply(startingValue));
			startingValue = startingValue.subtract(BigInteger.ONE);
		}
		return solution.longValue();
	}

	/**
	 * This method calculates the smallest number > than the specified bound that is prefixed with the given sequence 
	 *
	 * @throws Exception 
	 */
	public Long computeSmallestNumWithSequence(Long boundingInput) throws Exception{
		if(boundingInput != null && boundingInput >= 0 && sequence != null) {
			storeSequenceConstants();
			return calcCollatzExpressionResult(boundingInput);
		} else{
			throw new Exception("Computation Input is Invalid");
		}
	}

	/**
	 * Performs modified collatz computation for the given number and for the specified step Length
	 * @param stepLength
	 * @param number
	 * @return
	 * @throws Exception
	 */
	public Long performCollatzCalculation(Long stepLength, Long number) throws Exception{
		while(stepLength-- > 0) {
			if(number % 3 == 0) {
				number = bigDownwardExpression.calculateExpressionValue(number);
			} else if (number % 3 == 1) {
				number = upwardExpression.calculateExpressionValue(number);
			} else {
				number = downwardExpression.calculateExpressionValue(number); 
			}
		}
		return number;
	}

	/**
	 * Performs modified collatz computation for the given number and for the given sequence
	 * @param number
	 * @return
	 * @throws Exception
	 */   
	public Long performCollatzCalculation(Long number) throws Exception{
		for(char sequenceStep: sequence.toCharArray()){
			switch (sequenceStep){
				case 'U' : number = upwardExpression.calculateExpressionValue(number); 
						   break;
				case 'd' : number = downwardExpression.calculateExpressionValue(number);  
						   break;
				case 'D' : number = bigDownwardExpression.calculateExpressionValue(number); 
						   break;
				default : 
					throw new Exception("Prefix Sequence incorrect ");
			}
		}
		return number;
	}
}
