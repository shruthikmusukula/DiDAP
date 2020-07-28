public class Constants {
	/**
	 * Min window dimension
	 */
	public static int dim = 700;
	
	/**
	 * Change this to match your file output directory
	 */
	public static final String out_path = "D:\\Mitali\\ML\\NPS20\\test-out";
	
	/**
	 * Change this to match your file input directory
	 */
	public static final String in_path =  "D:\\Mitali\\ML\\NPS20\\mstiff";
		
	/**
	 * Type of image processing stage
	 */
	public enum Stage { TOPOGRAPH("Topograph"), PLANE("Plane"), SCAN("Scan"), COLOR_REDUCE("Color Reduce"), 
		CUT("Cut"), BOX("Box"), SLIDE("Slide"); 
		
		private String name;
		
		private Stage(String s) {
			name = s;
		}
		
		public String toString() {
			return name;
		}
	};
}
