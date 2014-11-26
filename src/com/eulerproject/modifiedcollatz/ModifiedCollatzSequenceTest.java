package com.eulerproject.modifiedcollatz;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Defines various test cases for the ModifiedCollatzSequence Class
 * 
 * @author Suhas Shetty
 *
 */
public class ModifiedCollatzSequenceTest {

	@Test
	public void testModifiedCollatzSequenceSmall() {
		String testSequenceSmall = "DdDddUUdDD";
		Long lowerBound = 100000L;
		modifiedCollatzSequenceTestHelper(testSequenceSmall,lowerBound );
	}
	
	@Test
	public void testModifiedCollatzSequenceBig() {
        String testSequenceBig = "UDDDUdddDDUDDddDdDddDDUDDdUUDd";
		Long lowerBound = 1000000000000000L;
		modifiedCollatzSequenceTestHelper(testSequenceBig,lowerBound);
	}
	
	public void modifiedCollatzSequenceTestHelper(String testSequence, Long lowerBound){
		ModifiedCollatzSequence sequence = new ModifiedCollatzSequence(testSequence);
		Long result = 0L;;
		try {
			result = sequence.computeSmallestNumWithSequence(lowerBound);
		} catch (Exception e1) {
			fail();
		}
		try {
			assert(sequence.performCollatzCalculation(result) == 
					sequence.performCollatzCalculation((long) testSequence.length(), result)) ;
		} catch (Exception e) {
			fail();
		}
	}

	@Test(expected=Exception.class)
	public void testModifiedCollatzSequencesNullException() throws Exception {
        String testSequence = "UDDDUdddDDUDDddDdDddDDUDDdUUDd";
		ModifiedCollatzSequence sequence = new ModifiedCollatzSequence(testSequence);
		sequence.computeSmallestNumWithSequence(null);
    }
	
	@Test(expected=Exception.class)
	public void testModifiedCollatzSequenceIncorrectSequence() throws Exception {
        String testSequence = "rDDDUdddDDUDDddDdDddDDUDDdUUDd";
		ModifiedCollatzSequence sequence = new ModifiedCollatzSequence(testSequence);
		sequence.computeSmallestNumWithSequence(100L);
    }
	
	@Test(expected=Exception.class)
	public void testModifiedCollatzSequenceIncorrectBound() throws Exception {
        String testSequence = "UDDDUdddDDUDDddDdDddDDUDDdUUDd";
		ModifiedCollatzSequence sequence = new ModifiedCollatzSequence(testSequence);
		sequence.computeSmallestNumWithSequence(-100L);
    }
}
