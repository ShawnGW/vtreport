package Tools;

public class MergeXNT {
	public static Integer merge(String[][] resultMapping,String[][] MapCell_R,String[][] finMapping,Integer row,Integer col)
	{
		Integer numberA=0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (MapCell_R[i][j]!=null) {
					if (MapCell_R[i][j].equals("1")&&resultMapping[i][j].equals("1")) {
						finMapping[i][j]="A";
						numberA++;
					}else if(MapCell_R[i][j].equals("1")&&resultMapping[i][j].equals("2")) {
						finMapping[i][j]="B";
					}else {
						finMapping[i][j]="X";
					}
				}
			}
		}
		return numberA;
	}
}
