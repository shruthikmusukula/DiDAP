package DataStorage;

public class BlinkPoint extends Point {

	public boolean lum;
	public Centroid cen;
	
	public BlinkPoint(int r, int c, boolean lum) {
		
		super(r, c);
		
		this.lum = lum;
	}
}
