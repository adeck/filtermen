#!/usr/bin/python

import math
import random

outputFile1 = "RandomNumbersHistogram.txt"
outputFile2 = "RandomNumbers.txt"
fo = open(outputFile1, "w")
of = open(outputFile2, "w")
nums = [0]*256

for i in xrange(0,100000):
	threshold = random.random()
	if threshold <= .05:
		randomnumber = int(random.gauss(180,10))
	else:
		randomnumber = int(random.gauss(60,30))
	if randomnumber <= 255 and randomnumber >= 0:
		nums[randomnumber] += 1
		of.write(str(randomnumber) + '\n')

for i in xrange(0,255):
	print str(i) + ':\t' + str(nums[i])
	fo.write(str(i) + '\t' + str(nums[i]) + '\n')
fo.close()
of.close()