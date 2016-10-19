import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

public class HadoopStat
{
	public static class StatMap extends Mapper<LongWritable, Text, Text, Text>
 {
  @Override
  protected void map(final LongWritable key,  
                     final Text value,
                     final Context context
					 ) 
					 throws IOException, InterruptedException 
	{
	  context.write(new Text("MAPPER"),value); 
	  //Output.collect( new Text("MAPPER"),value);
	};
 }

 public static class StatCombiner extends Reducer<Text, Text, Text, Text> {
  @Override
  protected void reduce(final Text key,
                        final Iterable<Text> values,
                        final Context context
						) throws IOException, InterruptedException
{
   Integer count = 0;
   Double sum   = 0D;
   Double sumsqr = 0D;
   Double minval = Double.MAX_VALUE;
   Double maxval = Double.MIN_VALUE;
   final Iterator<Text> itr = values.iterator();
   while (itr.hasNext()) {
    final String text = itr.next().toString();
    final Double value = Double.parseDouble(text);
    count++;
    sum += value;
    sumsqr += Math.pow(value, 2); 
	
	if (value > maxval)
	{
		maxval=value;
	}
	
	if (value < minval)
	{
		minval=value;
	}
   }

   final Double average = sum / count;

   context.write(new Text("A_C_MAX_MIN_SUMSQR"), new Text(average + "_" + count + "_" + maxval + "_" + minval + "_" +sumsqr));
   
  // output.collect(new Text("A_C_MAX_MIN"), new Text(average + "_" + count + "_" + maxval + "_" + minval));
  };
 }

 public static class StatReduce extends Reducer<Text, Text, Text, Text> {
  @Override
  protected void reduce(final Text key,
                        final Iterable<Text> values,
                        final Context context
						)
						throws IOException, InterruptedException
  {
   Double sum = 0D;
   Double maxvalue = Double.MIN_VALUE;
   Double minvalue = Double.MAX_VALUE;
   Double losumsqr = 0D;
   Integer totalcount = 0;
   
   final Iterator<Text> itr = values.iterator();
   while (itr.hasNext()) {
    final String text = itr.next().toString();
    final String[] tokens = text.split("_");
    final Double average = Double.parseDouble(tokens[0]);
    final Integer count = Integer.parseInt(tokens[1]);
	final Double maxval = Double.parseDouble(tokens[2]);
	final Double minval = Double.parseDouble(tokens[3]);
	final Double sumsqr = Double.parseDouble(tokens[4]);
	
	sum += (average * count);
	losumsqr += sumsqr;
    totalcount += count;
    
	if ( maxval > maxvalue  )  
	{
		maxvalue = maxval;
	}
	
	if ( minval < minvalue)
	{
		minvalue = minval;
	}
	
   }
   
  final Double term = losumsqr/totalcount;   
  final Double average = sum / totalcount;
  final Double stdev = Math.sqrt(
		       term - (Math.pow(average,2))
		               );
  
  //output.collect(new Text ("AVERAGE_MAX_MIN"), new Text(average + "_" + maxvalue + "_" + minvalue));
  context.write(new Text ("AVERAGE_MAX_MIN_SD"), new Text(average + "_" + maxvalue + "_" + minvalue + "_" +stdev));
  };
 }
 
   public static void main(String[] args) throws Exception {
	   
   Configuration conf = new Configuration();	
   @SuppressWarnings("deprecation")
   Job job = new Job(conf,"HadoopStat");
   
 
   job.setJobName("hadoopstat");
   
   job.setOutputKeyClass(Text.class);
   job.setOutputValueClass(Text.class);

   job.setMapperClass(StatMap.class);
   job.setCombinerClass(StatCombiner.class);
   job.setReducerClass(StatReduce.class);

   job.setInputFormatClass(TextInputFormat.class);
   job.setOutputFormatClass(TextOutputFormat.class);

   FileInputFormat.addInputPath(job, new Path(args[0]));
   FileOutputFormat.setOutputPath(job, new Path(args[1]));
   
   job.waitForCompletion(true);

   }
 
 
}
