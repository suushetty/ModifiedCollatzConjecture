package com.eulerproject.modifiedcollatz;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This is the application class for ModifiedCollatzSequence that handles user input and validation
 *
 * @author Suhas Shetty
 *
 */
public class ModifiedCollatzSequenceApplication {

	public static void main(String[] args) throws Exception {
		
		Long startTime = null;
		Long endTime = null;
		Long input = null;
		String seqInput = null; 
		
		Long result = 0L;
		Scanner userInput = new Scanner(System.in);
		
		// Input the bound and the prefix sequence from the user
		System.out.println("=============================================");
		System.out.println("ModifiedCollatzSequenceApplication");
		System.out.println("This application calculates the smallest number greater than a number N that has the modified collatz sequence");
        System.out.println("Enter the lower bound (+ve) for Sequence: ");
        
        // Check for valid inputs and then calculate the number for the given sequence
        // Check if bound range is valid
		if(userInput.hasNextLong()) {
			input = userInput.nextLong();
			if(input != null && input >= 0) {
				System.out.println("Enter the Prefix Sequence (U,D,d): ");
				// Check if sequence has been input
				if(userInput.hasNext()){
					seqInput = userInput.next();
					startTime = System.currentTimeMillis();
					result = calculateCollatzSeqNumber(seqInput, input);
					if(result >= 0) {
						endTime = System.currentTimeMillis();
						System.out.println("Number that starts with the given sequence: " + result);
						System.out.println("Execution Time: " + (endTime - startTime) + " ms");
					}	
				} else{
					System.out.println("Input Sequence Incorrect . Exiting");
				}	
			} else{
				System.out.println("Input Bound Incorrect . Exiting");
			}
		} else{
			System.out.println("Input Bound Incorrect . Exiting");
		}
		userInput.close();
	 }
     
	 /**
	  * Calculates the smallest number with the sequence prefix
	  * @param seqInput
	  * @param input
	  * @return
	  * @throws Exception
	  */
	 public static Long calculateCollatzSeqNumber(String seqInput,Long input) throws Exception {
		 String regex = "[UdD]*";
		 if(validateInputSequence(regex, seqInput)) {
			 //Instantiate ModifiedCollatzSequenceApplication class
			 ModifiedCollatzSequence sequence = new ModifiedCollatzSequence(seqInput);
			 return sequence.computeSmallestNumWithSequence(input);
		 } else{
			 System.out.println("Input Sequence Incorrect . Exiting");
			 return -1L;
		 }	 
	 }
	  
	 /**
	  * Validates whether the input string sequence is valid
	  * @param regex
	  * @param seqInput
	  * @return
	  */
	 public static boolean validateInputSequence(String regex, String seqInput){
		 if((seqInput != null)&& !seqInput.isEmpty()) {
			 return Pattern.matches(regex, seqInput);
		 } 
		 return false;
	 }
}
