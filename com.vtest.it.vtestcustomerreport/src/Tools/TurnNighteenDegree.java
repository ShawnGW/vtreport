package Tools;

public class TurnNighteenDegree {
	public static String[][] Turn(String[][] Mapcell,Integer row,Integer col)
	{		
		String[][] Result=new String[col][row];
		for (int j = 0; j < col; j++) {
		for (int i = 0; i < row; i++) {
			Result[j][i]=Mapcell[row-1-i][j];
		}
		}
		return Result;
	}
	public static String[][] turnNegativeNighteen(String[][] Mapcell,Integer row,Integer col)
	{		
		String[][] Result=new String[col][row];
		for (int j = 0; j < col; j++) {
			for (int i = 0; i < row; i++) {
				Result[col-1-j][i]=Mapcell[i][j];				
			}
		}
		return Result;
	}
}
