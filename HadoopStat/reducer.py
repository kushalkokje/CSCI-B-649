#!/usr/bin/env python3

#import statistics
import sys
import ast
import math

min_list = []
max_list = []
# avg_list = []
# stat_list = []
count_list = []
sum_list = []
sumsq_list = []
final_output = {}

for line in sys.stdin:
	#print (line)
	line = ast.literal_eval(line)
	min_list.append(line["min"])
	max_list.append(line["max"])
	# avg_list.append(float(line["avg"]))
	# stat_list.append(float(line["stat"]))
	sum_list.append(float(line["sum"]))
	count_list.append(int(line["count"]))
	sumsq_list.append(float(line["sumsq"]))
	#print("\nMin list is ", min_list,"\nMax list is ",max_list,"\nAverage list is ",avg_list,"\nStandard deviation is ",stat_list,"\nCount list is",count_list)

tot_count = sum(count_list)
tot_sum = sum(sum_list)
final_avg = tot_sum / tot_count

# for i in range(0,len(avg_list)):
	# #print (i)
	# final_avg += count_list[i] * avg_list[i] / tot_count
	
print (final_avg)

tot_sumsq = sum(sumsq_list)
first_term = tot_sumsq / float(tot_count)
sec_term = (tot_sum / float(tot_count)) ** 2

variance = first_term - sec_term
print (variance)
final_stat = math.sqrt(variance)

final_output["min"] = min(min_list)
final_output["max"] = max(max_list)
#final_avg = sum(avg_list) / float(len(avg_list))
final_avg = format(final_avg,".6f")
final_output["avg"] = final_avg
# final_stat = statistics.stdev(stat_list)
final_stat = format(final_stat,".4f")
final_output["stat"] = final_stat

#print (final_output)

for keys,values in final_output.items():
	print (keys,"\t",values)