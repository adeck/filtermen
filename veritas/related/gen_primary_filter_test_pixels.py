#!/usr/bin/env python2.7

f = open('primary_filter_test_pixels.txt', 'w')

def write(img):
  rowsize = 42
  numrows = 42
  for row in img:
    #print("numrows: " + str(numrows))
    numrows -= 1
    numcols = rowsize
    for elem in row:
      #print("numcols: " + str(numcols))
      numcols -= 1
      f.write(str(elem) + '\n')
    while (numcols > 0):
      numcols -= 1
      f.write('0\n')
  while (numrows > 0):
    numrows -= 1
    for i in range(rowsize):
      f.write('0\n')

img = [
        [200, 200, 60, 60],
        [60, 60, 60, 60],
        [0],
        [0],
        [0],
        [0, 70, 70, 70],
        [0, 0, 151],
        [0, 70, 70, 70],
        [0, 0, 150],
        [0, 70, 70, 70],
        [0],
        [0,300,300,300,50],
        [50,300,300,300,40],
        [200,300,300,300,10]
      ]

write(img)
    
expected = [
      [60, 60],
      [0],
      [0],
      [0],
      [0, 70, 70, 70],
      [0, 0, 151],
      [0, 70, 70, 70],
      [0],
      [0],
      [0],
      [300,300,300],
      [300,300,300],
      [300,300,300]
    ]
"""
# row 0
--numrows
f.write('200\n' * 2)
f.write('60\n' * 2)
f.write('0\n' * (rowsize - 4))
# row 1
f.write('60\n' * 4)
f.write('0\n' * (rowsize - 4))
# rows 2,3,4
f.write('0\n' * rowsize * 3)
# row 5
f.write('0\n')
f.write('70\n' * 3)
f.write('0\n' * (rowsize - 4))
# row 6
f.write('0\n' * 2)
f.write('151\n')
f.write('0\n' * (rowsize - 3))
"""
