import edu.princeton.cs.algs4.WeightedQuickUnionUF;

enum SiteState {BLOCKED, OPEN}

public class Percolation {
	
	private final Site[][] percolationGrid;
	private int numOpenSites;
	private final int gridSize;
	private final WeightedQuickUnionUF wqu;
	private final int VIRTUAL_TOP;
	private final int VIRTUAL_BOTTOM;
	
	public Percolation(int n) {
		
		if (n < 0) {
		  throw new IllegalArgumentException();
		}
		percolationGrid = new Site[n][n];
		int idCounter = 0;
		for (int i = 0; i < n; i++) {
		  for (int j = 0; j < n; j++) {
			Site initialSite = new Site();
			initialSite.setId(idCounter);
		    percolationGrid[i][j] = initialSite;
		    idCounter++;
		  }
		}
		gridSize = n;
		wqu = new WeightedQuickUnionUF(n * n + 3);
		VIRTUAL_TOP = (n * n) + 1;
		VIRTUAL_BOTTOM = (n * n) + 2;
	}
	
	public void open(int row, int col) {
		// Checked for indexes that might be out of bounds
		if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
			throw new IllegalArgumentException("Cannot open site.  Index out of bounds.");
		}
		
		int gridRow = row - 1;
		int gridCol = col - 1;
		
		// Open up site if it not currently blocked
		// Unblocking from the top row generates a full site - can exit at this point
		if (!isOpen(row, col)) {
		  percolationGrid[gridRow][gridCol].setState(SiteState.OPEN);
		  numOpenSites++;
		  if (row == 1) {
		    wqu.union(wqu.find(percolationGrid[gridRow][gridCol].getId()), VIRTUAL_TOP);
		  }
		  if (row == gridSize) {
			wqu.union(wqu.find(percolationGrid[gridRow][gridCol].getId()), VIRTUAL_BOTTOM);
		  }
		}
		
		/*********************************************************/
		/* Determine if we can add to other connected components */
		/*********************************************************/
		
		/* Check Up Site*/
		if (gridRow > 0) {
		  if (percolationGrid[gridRow-1][gridCol].getState() != SiteState.BLOCKED) {
		    int start = wqu.find(percolationGrid[gridRow][gridCol].getId());
			int neighbor = wqu.find(percolationGrid[gridRow-1][gridCol].getId());
			if (!wqu.connected(start, neighbor)) {
		      wqu.union(start, neighbor);
			}
		  }
		}
		
		/* Check Down Site*/
		if (gridRow < gridSize - 1) {
		  if (percolationGrid[gridRow+1][gridCol].getState() != SiteState.BLOCKED) {
		    int start = wqu.find(percolationGrid[gridRow][gridCol].getId());
			int neighbor = wqu.find(percolationGrid[gridRow+1][gridCol].getId());
			if (!wqu.connected(start, neighbor)) {
			  wqu.union(start, neighbor);
			}
		  }
		}
		
		/* Check Left Site */
		if (gridCol > 0) {
		  if (percolationGrid[gridRow][gridCol-1].getState() != SiteState.BLOCKED) {
		    int start = wqu.find(percolationGrid[gridRow][gridCol].getId());
		    int neighbor = wqu.find(percolationGrid[gridRow][gridCol-1].getId());
		    if (!wqu.connected(start, neighbor)) {
			  wqu.union(start, neighbor);
			}
	      }
		}
		
		/* Right */
		if (gridCol < gridSize - 1) {
		  if (percolationGrid[gridRow][gridCol+1].getState() != SiteState.BLOCKED) {
		  int start = wqu.find(percolationGrid[gridRow][gridCol].getId());
		  int neighbor = wqu.find(percolationGrid[gridRow][gridCol+1].getId());
		  if (!wqu.connected(start, neighbor)) {
		    wqu.union(start, neighbor);
		  }
		}
	  }
	}
	
	public boolean isOpen(int row, int col) {
		if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
			throw new IllegalArgumentException("Range out of grid bounds.");
		}
		if (percolationGrid[row-1][col-1].getState() != SiteState.BLOCKED) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFull(int row, int col) {
		if (row < 1 || col < 1 || row > gridSize || col > gridSize) {
			throw new IllegalArgumentException();
		}
		
		/* For connected sites in top row, see if ids are a match */
		if (wqu.connected(wqu.find(percolationGrid[row-1][col-1].getId()), VIRTUAL_TOP)) {
		  return true;	
		} else {
		  return false;
		}
	}
	
	public int numberOfOpenSites() {
	  return numOpenSites;
	}
	
	public boolean percolates() {		
	  if (wqu.connected(VIRTUAL_BOTTOM, VIRTUAL_TOP)) {
	    return true;	
	  } else {
	    return false;
      }		
	}
	
	public String toString() {
	  StringBuilder bldr = new StringBuilder();
	  for (int i = 0; i < gridSize; i++) {
	    for (int j = 0; j < gridSize; j++) {
	      bldr.append("ID: " + percolationGrid[i][j].getId() + "\n");
	      bldr.append("State: " + percolationGrid[i][j].getState().name() + "\n");
		}
	  }
	  return bldr.toString();
	}
	
	private class Site {
		
		private SiteState state;
		private int id;
		
		public Site() {
			state = SiteState.BLOCKED;
		}
		
		public SiteState getState() {
			return state;
		}
		
		public void setState(SiteState state) {
			this.state = state;
		}
		
		public Integer getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
	}	

	public static void main(String[] args) {
		Percolation perc = new Percolation(4);
		perc.open(1, 2);
		perc.open(2, 4);
		perc.open(3, 3);
		perc.open(1, 3);
		perc.open(1, 4);
		perc.open(4, 1);
		perc.open(4, 2);
		perc.open(2, 3);
		perc.open(2, 1);
		perc.open(3, 4);
		System.out.println(perc.toString());
		System.out.println("Percolates: " + perc.percolates());
	}
}
