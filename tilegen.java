import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

class tilegen
{
	static String[] tiles = new String[225];
	static int[] map = new int[225];
	static int col = 15;
	static int row = 15;
	static String wall = "\"#\", ";
	static String eend = "\"#\"";
	static String flor = "\" \", ";
	static String filename = "E:/Projects/CCO/NormalNormal_X_002.txt"; //Check output folder for latest and then increment "Normal_X_###" OR "Boss_X_###" appropriately
	static String toFile = "";
	
	
	/*
	TODO:
	DETECTING REGIONS:	https://www.youtube.com/watch?v=xYOG8kH2tF8
	CONNECTING ROOMS:	https://www.youtube.com/watch?v=eVb9kQXvEZM
	*/

	
	public static void main(String[] args)
	{
		int indx = 0;
		tilegen tg = new tilegen();
		map = tg.generateCave(225,45);
		
		try
		{
			FileWriter fw = new FileWriter(filename);
			toFile += "\t\t{\n\t\t\t\"tiles\": [\n\t\t\t\t";
			
			for(int i = 0;i < 225;i++)
			{
				if(map[i] == 0)
				{
					toFile += flor;
				}
				else if(map[i] == 1)
				{
					if(i == 224)
					{
						toFile += eend;
						break;
					}
					else
					{
						toFile += wall;
					}
				}
				indx++;
				if(indx == 15)
				{
					indx = 0;
					toFile += "\n\t\t\t\t";
				}
			}
			toFile += "\n\t\t\t]\n\t\t},";
			fw.write(toFile);
			fw.close();
			System.out.println("Complete!");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private int getNeighbourCount(int[] mapArray,int index)
    {
        int nCount = 0;

        if(index + 1 < col * row && mapArray[index + 1] == 1)
        {
            nCount++;
        }

        if(index - 1 >= 0 && mapArray[index - 1] == 1)
        {
            nCount++;
        }

        if(index + row < col * row && mapArray[index + row] == 1)
        {
            nCount++;
        }

        if(index - row >= 0 && mapArray[index - row] == 1)
        {
            nCount++;
        }

        if (index + row + 1 < col * row && mapArray[index + row + 1] == 1)
        {
            nCount++;
        }

        if (index + row - 1 < col * row && mapArray[index + row - 1] == 1)
        {
            nCount++;
        }

        if (index - row - 1 >= 0 && mapArray[index - row - 1] == 1)
        {
            nCount++;
        }

        if (index - row + 1 >= 0 && mapArray[index - row + 1] == 1)
        {
            nCount++;
        }

        return nCount;
    }
	
	public int[] evolveCaveFirstStage(int[] mapArray,int size)
    {
        int[] newMap = new int[size];
        for (int j = 0; j < size; j++)
        {
            if(mapArray[j] == 1)
            {
                if(getNeighbourCount(mapArray, j) >= 4)
                {
                    newMap[j] = 1;
                }
                else if(getNeighbourCount(mapArray, j) < 2)
                {
                    newMap[j] = 0;
                }
            }
            else
            {
                if(getNeighbourCount(mapArray, j) >= 5)
                {
                    newMap[j] = 1;
                }
            }
        }
        return newMap;
    }

    public int[] evolveCaveSecondStage(int[] mapArray, int size)
    {
        int[] newMap = new int[size];
        for (int j = 0; j < size; j++)
        {
            if (getNeighbourCount(mapArray, j) >= 4 && mapArray[j] == 1)
            {
                newMap[j] = 1;
            }
            else if (getNeighbourCount(mapArray, j) >= 4 && mapArray[j] == 0)
            {
                newMap[j] = 1;
            }
            else if(getNeighbourCount(mapArray, j) < 3 && mapArray[j] == 1)
            {
                newMap[j] = 0;
            }
            else
            {
                newMap[j] = 0;
            }
        }
        return newMap;
    }
	
	public int[] generateCave(int size,int terrainPercent)
    {
		//generate terrain
        int[] map = new int[size];
        for (int x = 0; x < size; x++)
        {
            if(Math.random() * 100 < terrainPercent)
            {
                map[x] = 1;
            }
            else
            {
                map[x] = 0;
            }
        }

        //terrain generation first pass
        for (int i = 0;i < 3; i++)
        {
            map = evolveCaveFirstStage(map, size);
        }
        //terrain generation second pass
        //for(int j = 0;j < 3;j++)
        //{
        //    map = evolveCaveSecondStage(map, size);
        //}
        
		//add border walls
        int counter = 0;
        for(int f = 0;f < col;f++)
        {
            for (int g = 0; g < row; g++)
            {
                if (f == 0 || g == 0 || f == col - 1 || g == row - 1 || f == col || g == row)
                {
                    map[counter] = 1;
					if(counter == 7)
					{
						map[counter] = 0;
					}
					if(counter == 105)
					{
						map[counter] = 0;
					}
					if(counter == 119)
					{
						map[counter] = 0;
					}
					if(counter == 217)
					{
						map[counter] = 0;
					}
                }
                counter++;
            }
        }

        return map;
    }
}