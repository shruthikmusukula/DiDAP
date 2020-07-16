package DataStorage;

public class LightPoint extends Point {

	/** luminescence*/ public double lum;
	/** whether lum == 1 */ public boolean blink;
	public int tag;
	public Centroid cen;
	
	public LightPoint(int r, int c, double lum) {
		
		super(r, c);
		
		this.lum = lum;
		
		blink = lum == 1;
	}
}
