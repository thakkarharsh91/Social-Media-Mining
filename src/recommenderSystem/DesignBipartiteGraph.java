package recommenderSystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import com.opencsv.CSVReader;

public class DesignBipartiteGraph {


	public static void processData(String networkOne, String networkTwo, String networkChoice , String problemChoice, String index, String topUsers, ServletContext context) throws Exception
	{
		String path = System.getProperty("user.home");
		File newFile = new File(path +"\\"+ "data.txt");
		FileWriter fw = new FileWriter(newFile, false);
		BufferedWriter writer = new BufferedWriter(fw);
		CSVReader readerForNetworkOne = null,readerForNetworkTwo = null;
		String[][] matrixNetworkOne,matrixNetworkTwo;
		double[][] finalMatrixNetworkOne,finalMatrixNetworkTwo,finalResultHits = null,topUserOne,topUserTwo,finalResultPageRank;
		double[] resultNetworkOne,resultNetworkTwo,xVectorForNetworkOne,xVectorForNetworkTwo,pageMatrixOne,pageMatrixTwo;

		if(networkOne.contains("gplus1"))
		{
			readerForNetworkOne = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/gplus1.csv")));
		}
		else if(networkOne.equalsIgnoreCase("gplus2"))
		{
			readerForNetworkOne = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/gplus2.csv")));
		}
		else if(networkOne.equalsIgnoreCase("gplus3"))
		{
			readerForNetworkOne = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/gplus3.csv")));
		}

		if(networkTwo.contains("twitter1"))
		{
			readerForNetworkTwo = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/twitter1.csv")));
		}
		else if(networkTwo.equalsIgnoreCase("twitter2"))
		{
			readerForNetworkTwo = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/twitter2.csv")));
		}
		else if(networkTwo.equalsIgnoreCase("twitter3"))
		{
			readerForNetworkTwo = new CSVReader(new FileReader(context.getRealPath("/WEB-INF/classes/twitter3.csv")));
		}

		matrixNetworkOne = ConstructorMatrixForNetworkOne(readerForNetworkOne);
		matrixNetworkTwo = ConstructorMatrixForNetworkTwo(readerForNetworkTwo);

		finalMatrixNetworkOne = covertMatrixForNetworkOne(matrixNetworkOne);
		finalMatrixNetworkTwo = covertMatrixForNetworkTwo(matrixNetworkTwo);

		if(Integer.parseInt(networkChoice)==1)
		{
			if(Integer.parseInt(problemChoice)==1)
			{
				resultNetworkOne = MatrixSample.calculateRightSingularVector(finalMatrixNetworkOne.length,finalMatrixNetworkOne[0].length,finalMatrixNetworkOne);
				resultNetworkTwo = MatrixSample.calculateRightSingularVector(finalMatrixNetworkTwo.length,finalMatrixNetworkTwo[0].length,finalMatrixNetworkTwo);
				finalResultHits = MatrixSample.constructBipartiteGraph(resultNetworkOne,resultNetworkTwo);

				if(Integer.parseInt(index)<= finalResultHits.length && Integer.parseInt(topUsers) <= finalResultHits[0].length)
				{
					topUserOne = MatrixSample.sortUsersForNetworkOne(Integer.parseInt(index),Integer.parseInt(topUsers),finalResultHits);
					MatrixSample.showTopUsers1(Integer.parseInt(index),Integer.parseInt(topUsers),topUserOne, writer, fw);
					xVectorForNetworkOne = MatrixSample.calculateXForNetworkOne(topUserOne);
					xVectorForNetworkOne[Integer.parseInt(index)] = 0;
					fw.write("\n");
					writer.write("\r\nCalculated X for sorted matrix: ");
					for(int a=0; a<xVectorForNetworkOne.length; a++)
					{
						fw.write("\n");
						writer.write("\r\n" + xVectorForNetworkOne[a]+" ");
					}
					fw.write("\n");
				}
				else
				{
					System.exit(0);
				}
			}
			else
			{
				pageMatrixOne = MatrixSample.pageRankNetwork(finalMatrixNetworkOne);
				pageMatrixTwo = MatrixSample.pageRankNetwork(finalMatrixNetworkTwo);
				finalResultPageRank = MatrixSample.constructBipartiteGraph(pageMatrixOne,pageMatrixTwo);

				if(Integer.parseInt(index)<=finalResultPageRank.length && Integer.parseInt(topUsers)<=finalResultPageRank[0].length)
				{
					topUserOne = MatrixSample.sortUsersForNetworkOne(Integer.parseInt(index),Integer.parseInt(topUsers),finalResultPageRank);
					MatrixSample.showTopUsers1(Integer.parseInt(index),Integer.parseInt(topUsers),topUserOne,writer,fw);
					xVectorForNetworkOne = MatrixSample.calculateXForNetworkOne(topUserOne);
					xVectorForNetworkOne[Integer.parseInt(index)] = 0;
					fw.write("\n");
					writer.write("\r\nCalculated X for sorted matrix: ");
					for(int a=0; a<xVectorForNetworkOne.length; a++)
					{
						fw.write("\n");
						writer.write("\r\n" + xVectorForNetworkOne[a] + " ");
					}
					fw.write("\n");
				}
				else
				{
					System.exit(0);
				}
			}
		}
		else
		{
			if(Integer.parseInt(problemChoice)==1)
			{
				resultNetworkOne = MatrixSample.calculateRightSingularVector(finalMatrixNetworkOne.length,finalMatrixNetworkOne[0].length,finalMatrixNetworkOne);
				resultNetworkTwo = MatrixSample.calculateRightSingularVector(finalMatrixNetworkTwo.length,finalMatrixNetworkTwo[0].length,finalMatrixNetworkTwo);
				finalResultHits = MatrixSample.constructBipartiteGraph(resultNetworkOne,resultNetworkTwo);

				if(Integer.parseInt(index) <= finalResultHits[0].length && Integer.parseInt(topUsers) <= finalResultHits.length)
				{
					topUserTwo = MatrixSample.sortUsersForNetworkTwo(Integer.parseInt(index),Integer.parseInt(topUsers),finalResultHits);
					MatrixSample.showTopUsers2(Integer.parseInt(index),Integer.parseInt(topUsers),topUserTwo, writer, fw);
					xVectorForNetworkTwo = MatrixSample.calculateXForNetworkTwo(topUserTwo);
					xVectorForNetworkTwo[Integer.parseInt(index)] = 0;
					fw.write("\n");
					writer.write("\nCalculated X for sorted matrix: ");
					for(int a=0; a<xVectorForNetworkTwo.length; a++)
					{
						fw.write("\n");
						writer.write("\r\n" + xVectorForNetworkTwo[a]+" ");
					}
					fw.write("\n");
				}
				else
				{
					System.exit(0);
				}
			}
			else
			{
				pageMatrixOne = MatrixSample.pageRankNetwork(finalMatrixNetworkOne);
				pageMatrixTwo = MatrixSample.pageRankNetwork(finalMatrixNetworkTwo);
				finalResultPageRank = MatrixSample.constructBipartiteGraph(pageMatrixOne,pageMatrixTwo);

				topUserTwo = MatrixSample.sortUsersForNetworkTwo(Integer.parseInt(index),Integer.parseInt(topUsers),finalResultPageRank);
				MatrixSample.showTopUsers2(Integer.parseInt(index),Integer.parseInt(topUsers),topUserTwo, writer, fw);
				xVectorForNetworkTwo = MatrixSample.calculateXForNetworkTwo(topUserTwo);
				xVectorForNetworkTwo[Integer.parseInt(index)] = 0;
				fw.write("\n");
				writer.write("\r\nCalculated X for sorted matrix: ");
				for(int a=0; a<xVectorForNetworkTwo.length; a++)
				{
					fw.write("\n");
					writer.write("\r\n" + xVectorForNetworkTwo[a]+" ");
				}
				fw.write("\n");
			}
		}

		fw.flush();
		writer.close();

		File data = new File(path+"\\"+"data.txt");
		BufferedReader newBufferedReader = new BufferedReader(new FileReader(data));

		File resultFile = new File(path + "\\"+"Result.txt");
		FileWriter newFileWriter = new FileWriter(resultFile); 
		String line;

		while((line = newBufferedReader.readLine()) != null)
		{ 
			line = line.trim(); 
			if (!line.equals(""))
			{
				newFileWriter.write("\n" + line);
			}
		} 
		newFileWriter.close();
		newBufferedReader.close();
		
		FileUtils.forceDelete(data);
	}

	private static String[][] ConstructorMatrixForNetworkOne(CSVReader readerNetworkOne) {

		List<String[]> lines = null;
		try {
			lines = readerNetworkOne.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines.toArray(new String[lines.size()][]);
	}

	private static String[][] ConstructorMatrixForNetworkTwo(CSVReader readerNetworkTwo) {

		List<String[]> lines = null;
		try {
			lines = readerNetworkTwo.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lines.toArray(new String[lines.size()][]);		
	}

	private static double[][] covertMatrixForNetworkOne(
			String[][] matrixNetworkOne) {

		double[][] doubleMatrixNetworkOne = new double[matrixNetworkOne.length][matrixNetworkOne[0].length];

		for(int i = 0;i<matrixNetworkOne.length;i++)
		{
			for(int j = 0;j<matrixNetworkOne[0].length;j++)
			{
				doubleMatrixNetworkOne[i][j] = Double.parseDouble(matrixNetworkOne[i][j]);
			}
		}
		return doubleMatrixNetworkOne;
	}

	private static double[][] covertMatrixForNetworkTwo(
			String[][] matrixNetworkTwo) {

		double[][] doubleMatrixNetworkTwo = new double[matrixNetworkTwo.length][matrixNetworkTwo[0].length];

		for(int i = 0;i<matrixNetworkTwo.length;i++)
		{
			for(int j = 0;j<matrixNetworkTwo[0].length;j++)
			{
				doubleMatrixNetworkTwo[i][j] = Double.parseDouble(matrixNetworkTwo[i][j]);
			}
		}
		return doubleMatrixNetworkTwo;
	}

}
