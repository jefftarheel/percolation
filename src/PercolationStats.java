import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private int gridSize;
	private double[] thresholds;
	private int numTrials;
	private double mean;
	private double stddev;
	
	public PercolationStats(int n, int trials) {
		
	  if (n < 0 || trials < 0) {
		  throw new IllegalArgumentException();
	  }
	  this.gridSize = n;	
	  this.thresholds = new double[trials];
	  this.numTrials = trials;
	  
	  int counter = 0;
	  while (counter < trials) {
	    Percolation perc = new Percolation(gridSize);
		
	    while (true) {
	      int randomRow = StdRandom.uniform(gridSize) + 1;
	      int randomCol = StdRandom.uniform(gridSize) + 1;
	      if (!perc.isOpen(randomRow, randomCol)) {
	    	    perc.open(randomRow, randomCol);
	    	    if (perc.percolates()) {
	    	    	  int numOpenSites = perc.numberOfOpenSites();
	    	    	  thresholds[counter] = (double)numOpenSites / (gridSize * gridSize);
	    	    	  break;
	    	    }
	      }
	    }
	    counter++;		  
	  }
	  
	  mean = StdStats.mean(thresholds);
	  stddev = StdStats.stddev(thresholds);
	  
	}
	
	public double mean() {		
	  return mean;		
	}
	
	public double stddev() {		
	  return stddev;
	}
	
	public double confidenceLo() {		
	  return mean - ((1.96 * stddev) / Math.sqrt(numTrials));
	}
	
	public double confidenceHi() {
      return mean + ((1.96 * stddev) / Math.sqrt(numTrials));
	}
	
	public static void main(String[] args) {
		
		PercolationStats percStats = new PercolationStats(10, 50);
		StdOut.println("mean = " + percStats.mean());
		StdOut.println("stddev = " + percStats.stddev());
		StdOut.println("95% confidence interval = [" + percStats.confidenceLo() +
		", " + percStats.confidenceHi() + "]");
	}
}
