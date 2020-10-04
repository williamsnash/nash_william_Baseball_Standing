import java.io.*;
import java.util.ArrayList; // used everywhere
import java.util.Map; //used in overall
import java.util.Scanner; //used for input and file reading
import java.util.TreeMap; // used in overall
import java.lang.Math; //used for rounding
import java.util.Collections; //used for sorting
import java.util.InputMismatchException; //used in main

/**
 * This is a program to read a text file and allow to user to select from 7 standings and then the program will print them out
 * @author William Nash
 * C:/Users/rsasw/Desktop/Code/BaseballStandingProject/baseball_standings.txt
 */
public class baseballStanding
{
	/**
	 * This is a nice formated welcome line
	 */
	public static void welcome()
	{
		System.out.println("********************************");
		System.out.println("*  Baseball Standing Analzyer  *");
		System.out.println("********************************");
	}
	
	/**
	 * This is the main
	 * Contains the menu / selection system
	 * Makes sure the file exists if not ends program
	 * 
	 */
	public static void main(String[] args)
	{
		welcome();
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the path to the file: ");
		String path = sc.nextLine();
		int choice = 0;
		try
		{
			Scanner file = new Scanner(new File(path));
			file.close();
		}catch(Exception ex){
			System.out.println("Could not read file");
			sc.close();
			return;
		}
		
		do
		{
			try
			{
				System.out.println("\nWhich standing would you like to see");
				System.out.println("1");
				System.out.println("2");
				System.out.println("3");
				System.out.println("4");
				System.out.println("5");
				System.out.println("6");
				System.out.println("7");
				System.out.println("8: Exit");
				System.out.print("Enter the number of your choice: ");
				choice = sc.nextInt();
				if(choice == 1)
				{
					printHeader();
					lineFinder("AL East", path);
				}
				else if(choice == 2)
				{
					printHeader();
					lineFinder("AL Central", path);
				}
				else if(choice == 3)
				{
					printHeader();
					lineFinder("AL West", path);
				}
				else if(choice == 4)
				{
					printHeader();
					lineFinder("NL East", path);
				}
				else if(choice == 5)
				{
					printHeader();
					lineFinder("NL Central", path);
				}
				else if(choice == 6)
				{
					printHeader();
					lineFinder("NL West", path);
				}
				else if(choice == 7)
				{
					overallStandings(path);
				}
			}catch(InputMismatchException ex) {
				System.out.println("Please only enter numbers 1-8!");
				choice = 0;
				sc.next();//This cleared the error from the scanner, without this it will cause an infinite loop
			}
		}while(choice != 8);
		System.out.println("Goodbye, have a good day!");
		sc.close();
	}//main
	
	/**
	 * printHeader will print the header in a nice stylized way
	 */
	public static void printHeader()
	{
		System.out.printf("%-15s %4s %8s %7s %8s \n", "Team", "Wins", "Losses", "Pct.", "Behind");
		System.out.println("----------------------------------------------");
	}
	
	/**
	 * lineFinder uses the selected standings file and the location wanted to be seen and sends the line one at a time to printFormater
	 * @param location is the selected located proved by the user in main function
	 * @param path is the path proved by user in the main function
	 */
	public static void lineFinder(String location, String path)
	{
		String line;
		String[] parts;
		try
		{
			Scanner file = new Scanner(new File(path));
			while(file.hasNextLine())
			{
				line = file.nextLine();// takes one line from file
				parts = line.split("\t");// splits line into parts
				if(parts[1].equalsIgnoreCase(location))
				{
					ArrayList<String> lines = new ArrayList<String>();
					for(int i = 0; i < 5; i++)
					{
						lines.add(file.nextLine());
					}
					printFormater(lines);
				}
			}
			file.close();
		}catch(Exception ex){
			System.out.println("Could not read file");
			ex.printStackTrace();
		}
	}
	
	/**
	 * takes in a line sent by the lineFinder function and prints it in a stylized table
	 * Calls the percentCalc function / passes ArrayList games
	 * Calls the gamesBehindCalc / passes ArrayList games
	 * @param line the line containing team name, wins and losses
	 */
	public static void printFormater(ArrayList<String> lines)
	{
		ArrayList<Double> games = new ArrayList<Double>();
		for(int i = 0; i < lines.size(); i++)
		{
			String line = lines.get(i);
			String[] parts = line.split("\t");
			games.add(Double.parseDouble(parts[1]));
			games.add(Double.parseDouble(parts[2]));
			double pct = percentCalc(games);
			double gamesBehind = gamesBehindCalc(games);
			System.out.printf("%-15s %3s %7s %9.3f  %6.1f \n", parts[0], parts[1], parts[2], pct, gamesBehind);
		}
	}
	
	/**
	 * This will calculate the win percent of a team
	 * @param games ArrayList containing win and lose data
	 * @return Win Percent
	 */
	public static double percentCalc(ArrayList<Double> games)
	{
		double total = games.get(games.size()-2) + games.get(games.size()-1);
		double percent = games.get(games.size()-2)/total;
		double rounded = Math.round(percent * 1000.0) / 1000.0;
		return rounded;
	}
	
	/**
	 * gameBehindCalc takes in the ArrayList games, which contains the wins and loses for each team, is calculates the amount of games behind using the formal. 
	 * @param games ArrayList full of the wins and loses
	 * @return it return the amount of games behind the team is
	 */
	public static double gamesBehindCalc(ArrayList<Double> games)
	{
		int size = games.size();
		if(games.size() == 2)
		{
			return 0.0;
		}
		else
		{
			Double teamA = games.get(0)-games.get(1);
			Double teamB = games.get(size-2)-games.get(size-1);
			double behind = (teamA - teamB)/2;
			return behind;
		}
	}
	
	/**
	 * This is a function dedicated to doing the work for the overall option
	 * This will read the file, find the winning %, sort, and print
	 * @param path allows for this function to read the file
	 */
	public static void overallStandings(String path)
	{
		String line;
		String[] parts;
		try
		{

			final Map<Double, String> allInfo = new TreeMap<>(Collections.reverseOrder());
			Scanner file = new Scanner(new File(path));
			while(file.hasNextLine())
			{
				line = file.nextLine();// takes one line from file
				parts = line.split("\t");// splits line into parts
				if(!parts[0].equalsIgnoreCase("League"))
				{
					Double total = Double.parseDouble(parts[1]) + Double.parseDouble(parts[2]);//calculation for win percent
					Double percent = Double.parseDouble(parts[1]) / total;
					allInfo.put(percent, line);
				}
			}
			file.close();

			System.out.printf("%-15s %4s %8s \n", "Team", "Wins", "Losses");//custom header
			System.out.println("-----------------------------");
			
			for(Double d: allInfo.keySet())// sorting for the tree map
			{
				//System.out.println(allInfo.get(d));
				String[] pieces = allInfo.get(d).split("\t");
				System.out.printf("%-15s %3s %7s \n", pieces[0], pieces[1], pieces[2]);//custom formatting for overall sort
			}
		}catch(Exception ex){
			System.out.println("Could not read file");
			ex.printStackTrace();
		}
		
	}
}
