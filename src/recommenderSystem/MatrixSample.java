package recommenderSystem;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.io.*;

public class MatrixSample{

	private final int rows;            
	private final int columns;           
	private final double[][] data;
	static DecimalFormat df = new DecimalFormat("#.####");
	static Queue<Integer> queueIndexOne = new LinkedList<Integer>();
	static Queue<Integer> queueIndexTwo = new LinkedList<Integer>();

	public MatrixSample(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		data = new double[rows][columns];
	}

	public MatrixSample(double[][] data) {
		rows = data.length;
		columns = data[0].length;
		this.data = new double[rows][columns];
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				this.data[i][j] = data[i][j];
	}

	public void show(double[][] printMatrix){
		for(int i = 0;i < rows; i++)
		{
			for(int j = 0;j < columns; j++)
			{
				System.out.print((double)printMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	static double[] calculateRightSingularVector(int rows,int columns,double[][] matrix) {
		Jama.Matrix matrixData;
		Matrix V;
		double[] network;
		SingularValueDecomposition svd;
		matrixData = new Jama.Matrix(matrix);
		svd = matrixData.svd();
		V = svd.getV();
		network = new double[V.getRowDimension()];
		for(int i=0;i<V.getRowDimension();i++)
		{
			network[i]=Double.valueOf(df.format((V.get(i, 0))));
		}
		return network;
	}

	static double[][] constructBipartiteGraph(double[] resultNetwork1, double[] resultNetwork2) {
		double[][] result = new double[resultNetwork1.length][resultNetwork2.length];
		for(int i = 0; i < resultNetwork1.length; i++)
		{
			for(int j = 0;j< resultNetwork2.length;j++)
			{
				result[i][j] = Double.valueOf(df.format(1 - Math.abs(resultNetwork1[i]-resultNetwork2[j])));
			}
		}
		return result;
	}

	static double[][] sortUsersForNetworkOne(int index, int topUsers,double[][] finalResult) {
		double[][] tempUserMatrix = new double[finalResult.length][finalResult[0].length];
		double highestValue = -100000000000000000000e99;
		int highestIndex = 0;
		ArrayList<Integer> sortedColumns = new ArrayList<Integer>();

		for(int a=0;a<finalResult.length;a++)
		{
			if(a==index)
			{
				for(int c=0; c<tempUserMatrix[0].length; c++)
				{
					for(int b=0; b<finalResult[a].length; b++)
					{
						if((!sortedColumns.contains(b)) && (finalResult[a][b] > highestValue))
						{
							highestValue = finalResult[a][b];
							highestIndex = b;
						}
					}
					sortedColumns.add(highestIndex);
					queueIndexOne.add(highestIndex);
					highestValue = -100000000000000000000e99;
					for(int d=0;d<finalResult.length;d++)
					{
						tempUserMatrix[d][c] =  finalResult[d][highestIndex];
					}
				}
				break;
			}
		}
		return tempUserMatrix;
	}

	static double[][] sortUsersForNetworkTwo(int index, int topUsers, double[][] finalResult) {
		double[][] tempUserMatrix = new double[finalResult.length][finalResult[0].length];
		double highestValue = -100000000000000000000e99;
		int highestIndex = 0;
		ArrayList<Integer> sortedRows = new ArrayList<Integer>();
		for(int a=0; a<finalResult.length ;a++)
		{
			for(int b=0; b<finalResult.length; b++)
			{
				if( (!sortedRows.contains(b)) && (finalResult[b][index]>=highestValue))
				{
					highestValue = finalResult[b][index];
					highestIndex = b;
				}
			}
			sortedRows.add(highestIndex);
			queueIndexTwo.add(highestIndex);
			tempUserMatrix[a] = finalResult[highestIndex];
			highestValue = -100000000000000000000e99;
		}
		return tempUserMatrix;
	}

	static void showTopUsers1(int index,int topUsers,double[][] topUser, BufferedWriter writer, FileWriter fw) throws Exception
	{
		if(index <= topUser.length)
		{
			for(int a=0; a<topUser.length;a++)
			{
				if(a == index)
				{
					writer.write("Top users are as follows: ");
					fw.write("\n");
					for(int b=0; b<topUsers; b++)
					{
						fw.write("\n");
						writer.write("\r\n" + topUser[a][b] + " " +  queueIndexOne.poll());
					}
				}
			}
		}
		else
		{
			System.out.println("Please run the program again with appropriate number of users");
			System.exit(0);
		}
	}

	static void showTopUsers2(int index, int topUsers,double[][] topUser, BufferedWriter writer, FileWriter fw) throws Exception {
		if(index <= topUser[0].length)
		{
			for(int a=0; a<topUser[0].length;a++)
			{
				if(a == index)
				{
					writer.write("Top users are as follows:");
					fw.write("\n");
					for(int b=0; b<topUsers; b++)
					{
						fw.write("\n");
						writer.write("\r\n" + topUser[b][a] + " " + queueIndexTwo.poll());
					}
				}
			}
		}
		else
		{
			System.out.println("Please run the program again with appropriate number of users");
			System.exit(0);
		}
	}

	static double[] calculateXForNetworkOne(double[][] tempUserMatrix)
	{
		for(int a=0; a<tempUserMatrix.length; a++)
		{
			for(int b=0; b<tempUserMatrix[a].length;b++)
			{
				if(b==0)
					tempUserMatrix[a][b] = tempUserMatrix[a][b]/(b+1);
				else
					tempUserMatrix[a][b] = tempUserMatrix[a][b-1] + tempUserMatrix[a][b]/(b+1);
			}
		}

		int columns = tempUserMatrix[0].length;
		double[] topUser = new double[tempUserMatrix.length];
		for(int a=0; a<topUser.length;a++)
		{
			topUser[a] =   new BigDecimal(tempUserMatrix[a][columns-1]).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return topUser;
	}

	static double[] calculateXForNetworkTwo(double[][] tempUserMatrix)
	{
		for(int a=0; a<tempUserMatrix.length; a++)
		{
			for(int b=0; b<tempUserMatrix[a].length; b++)
			{
				if(a==0)
					tempUserMatrix[a][b] = tempUserMatrix[a][b]/(a+1);
				else
					tempUserMatrix[a][b] = tempUserMatrix[a-1][b] + tempUserMatrix[a][b]/(a+1); 
			}
		}

		int rows = tempUserMatrix.length;
		int columns = tempUserMatrix[0].length;
		double[] topUser = new double[columns];
		for(int a=0; a<topUser.length;a++)
		{
			topUser[a] = new BigDecimal(tempUserMatrix[rows-1][a]).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return topUser;
	}

	static double[] pageRankNetwork(double[][] matrix)
	{
		double[] pageMatrixOne = new double[matrix.length]; 
		double rows = matrix.length;
		for(int x = 0;x<matrix.length;x++)
		{
			pageMatrixOne[x] = Double.valueOf(df.format(1/rows));
		}
		int oneDLength = pageMatrixOne.length;
		int twoDLength = matrix.length;
		int count = 0;
		double[] resultMatrix = new double[twoDLength];
		while(count <= 3)
		{
			for(int i = 0;i<twoDLength;i++)
			{
				double c = 0;
				for(int j=0;j<oneDLength;j++)
				{
					double l = pageMatrixOne[j];
					double m = matrix[i][j];
					c += l * m; 
				}
				resultMatrix[i] = c;
			}
	
			if(!Arrays.equals(pageMatrixOne, resultMatrix))
			{
				pageMatrixOne = resultMatrix;
			}
			count++;
		}
		double normalizedVal=0;
		for(int i=0;i<matrix.length;i++){
			normalizedVal=normalizedVal+(pageMatrixOne[i]*pageMatrixOne[i]);
		}
		normalizedVal=Math.sqrt(normalizedVal);
		for(int i=0;i<matrix.length;i++){
			pageMatrixOne[i]=pageMatrixOne[i]/normalizedVal;
		}
		return pageMatrixOne;
	}
}
