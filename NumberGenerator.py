#!/usr/bin/python

import math
import random

outputFile1 = "RandomNumbersHistogram.txt"
outputFile2 = "RandomNumbers.txt"
fo = open(outputFile1, "w")
of = open(outputFile2, "w")
nums = [0]*256
noiseMean = 60
noiseStdDev = 30
dataMean = 180
dataStdDev = 10

for i in xrange(0,10000000):
	threshold = random.random()
	if threshold <= .05:
		randomnumber = int(random.gauss(dataMean,dataStdDev))
	else:
		randomnumber = int(random.gauss(noiseMean,noiseStdDev))
	if randomnumber <= 255 and randomnumber >= 0:
		nums[randomnumber] += 1
		of.write(str(randomnumber) + '\n')

for i in xrange(0,255):
	print str(i) + ':\t' + str(nums[i])
	fo.write(str(i) + '\t' + str(nums[i]) + '\n')
fo.close()
of.close()