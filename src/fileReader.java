import java.io.File;
import java.util.Scanner;

public class fileReader
{

	public static void main(String[] args)
	{
		String path = "C:/Users/rsasw/Desktop/Code/BaseballStandingProject/baseball_standings.txt";
		String line;
		String[] parts;
		try
		{
			Scanner file = new Scanner(new File(path));
			while(file.hasNextLine())
			{
				line = file.nextLine();// takes one line from file
				parts = line.split("\t");// splits line into parts
				if(parts[1].equalsIgnoreCase("AL East"))
				{
					System.out.println(parts[0] + " " + parts[1]);
				}
				else if(parts[1].equalsIgnoreCase("AL Central"))
				{
					return;
				}
				else
				{
					System.out.println(parts[0] + " " + parts[1] + " " + parts[2]);
				}
			}
			file.close();
		}catch(Exception ex) {
			System.out.println("Could not read file");
			ex.printStackTrace();
		}
	}

}
