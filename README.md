ModifiedCollatzConjecture
=========================

An optimized solution for the modified collatz conjecture problem - https://projecteuler.net/problem=277

Approach
-------
The brute force solution would be checking if every number > N followed the sequence by performing the modified Collatz 
conjecture on it; which does not scale.

Instead, a generic expression is obtained for the modified Collatz conjecture of this form

* (Ax + B)/C

where 
* A – 4 ^ U + 2 ^d, where 4,2 is from the collatz expression 4x+1/3 and 2*x -1/3, and U and d are the total ‘U’ and ‘d’ steps in the sequence
* B - This is a complex expression, but essentially obtained by convolution of the constants of the conjecture operation(ax + b/c) with increasing powers of 3 
* C - 3 ^ k, where k is the sequence length 

If Ax + B/C = Y, Y is the number obtained after performing k steps in the sequence on the number x. 
The smallest integer solutions for x, Y such that x > N is the required answer. Substituting values for Y to get potential 
x works for smaller solutions, but does not scale for larger ones. 
 
This problem is solved by using the Extended Euclidean algorithm which computes the GCD (a , b) for an equation of the form 
* ax + by = c 
 
and gets generic expressions for x. From the Diophantine solution, equations of the form 
* x= p + q * T,  y = m + n * T, 
where p, q, m, n are constants that are calculated and T is any integer can be obtained

Finding the right T (such that x > N) involved substituting the lower bound N as x. 

The program uses BigIntegers to handle huge calculations
