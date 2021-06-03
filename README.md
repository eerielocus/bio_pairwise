# bio_pairwise

Needleman-Wunsch Algorithm: Dynamic Programming for Sequence Alignment

The program is an object-oriented dynamic programming approach to sequence alignment and data parsing. It will utilize the Needleman-Wunsch algorithm, which is a global alignment-based algorithm designed to divide problems into a series of smaller problems, then traceback until an optimal solution is reached. The way it will do this is by constructing a matrix where it will place the results of comparison score between each nucleotide from each sequence for each possible situation. The score is predetermined by the program to be that matches are 1 point, mismatches (or substitutions) are -1 point, and gaps (insert/delete) are -2 points. Once these results are calculated, the program will then start from the bottom most right positions and traceback towards the topmost left position, using the produced scores to determine the most optimal sequence. A more detailed explanation of this process will be covered during the sequence alignment module description. 
