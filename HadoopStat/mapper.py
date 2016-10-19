#!/usr/bin/env python3

import math
import sys
#import statistics
import decimal

line_chunk = []
square_list = []
map_output = {}

for line in sys.stdin:
	line = line.strip()
	line = float(line)
	line_chunk.append(line)
	square_list.append(line**2)

#avg = sum(line_chunk) / float(len(line_chunk))
#avg = format(avg, '.2f')
#stat = statistics.stdev(line_chunk)
#stat = format(stat,".4f")
count = len(line_chunk)
#print (count)
#print (square_list)

map_output["min"] = min(line_chunk)
map_output["max"] = max(line_chunk)
# map_output["avg"] = avg
# map_output["stat"] = stat
map_output["count"] = count
map_output["sum"] = sum(line_chunk)
map_output["sumsq"] = sum(square_list)

print (map_output)
# print ("min\t",min(line_chunk))
# print ("max\t",max(line_chunk))
# print ("avg\t",avg)
# print ("stat\t",stat)