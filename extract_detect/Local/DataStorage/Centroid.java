package DataStorage;

public class Centroid extends Point {

	/** luminescence */ public double lum;
	
	public int tag;
	
	public Centroid(int r, int c, double lum, int tag) {
		
		
		super(r, c);
		
		this.lum = lum;
		
		this.tag = tag;
	}
}
