package Function;

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
	public static final String in_path =  "D:\\Mitali\\ML\\NPS20\\test\\mstiff";

	/**
	 * Save mode for mine outputs
	 */
	public enum Save { /** save all images */ SAVE_ALL, /** save final output */ SAVE_FINAL, 
		/** display all images */ DISPLAY_ALL, /** display final output */ DISPLAY_FINAL, 
		/** do not display output */ NO_DISPLAY };
		
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
