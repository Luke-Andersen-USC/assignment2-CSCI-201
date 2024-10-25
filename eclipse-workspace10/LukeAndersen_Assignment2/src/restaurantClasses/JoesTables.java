package restaurantClasses;
import java.io.BufferedReader;
import java.io.File;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class JoesTables {

	private static Restaurants restaurants;
	
	public static void main(String []args) throws NumberFormatException, IOException
	{
		Vector<Order> orders = new Vector<Order>();
		
		//set-up
		Scanner s = new Scanner(System.in);
		
		//restaurants json file
		
		//Scanner sc;
		boolean goodFile = false;
		String temp = "";
		String restFileName = "";
		
		while(!goodFile)
		{
			System.out.println("What is the name of the file containing the restaurant information?");
			
			restFileName = s.nextLine();
			
			
			Scanner sc = null;
			
			try
			{
				File file = new File(restFileName);
				sc = new Scanner(file);
				goodFile = true;
				
				temp = "";
			
				while(sc.hasNext())
				{
					temp += sc.nextLine();
				}
				
				sc.close();
				
				Gson gson = new Gson();
				restaurants = gson.fromJson(temp, Restaurants.class);
			}
			catch(FileNotFoundException e)
			{
				System.out.println("The file " + restFileName + " could not be found");
			}
			catch (JsonIOException e)
			{
				System.out.println("The file " + restFileName + " could not be read");
			}
			catch(JsonSyntaxException e)
			{
				System.out.println("The file " + restFileName + " is not formatted properly");
			}
			finally
			{
				if(sc != null)
				{
					sc.close();	
				}
				
				
			}
			
		}
		
		System.out.println("The file " + restFileName + " had been properly read.");
						
		//Schedule file
		
		String schedFileName = "";
		goodFile = false;
		
		while(!goodFile)
		{
			System.out.println("What is the name of the file containing the schedule information?");
			
			schedFileName = s.nextLine();
			
			System.out.println(schedFileName);
			Scanner sc = null;
			
			try
			{
				BufferedReader br = new BufferedReader(new FileReader(new File(schedFileName)));
				goodFile = true;
				

				while((temp = br.readLine()) != null)
				{					
					int time = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
					String restaurant = (temp.substring(temp.indexOf(',') + 1,temp.lastIndexOf(',')));
					String menuItem = temp.substring(temp.lastIndexOf(',') + 1);
					Order order = new Order(time, restaurant.trim(), menuItem.trim());
					orders.add(order);
					
				}
				
				br.close();
			}
			catch(FileNotFoundException e)
			{
				System.out.println("The file " + schedFileName + " could not be found");
			}
			finally
			{
				if(sc != null)
				{
					sc.close();	
				}
				
			}
			
		}
		
		System.out.println("The file " + schedFileName + " had been properly read.");		
		
		//user latitude
		Double userLat = 0.0;
		boolean userLatIn = false;
		while(!userLatIn)
		{
			System.out.println("What is your latitude?");	
		
			try
			{
				userLat = Double.parseDouble(s.nextLine());
				userLatIn = true;
			}
			catch(NumberFormatException e)
			{
				System.out.println("That is not a valid option.");
				userLatIn = false;
			}
		}
		
		//user longitude
		Double userLong = 0.0;
		boolean userLongIn = false;
		while(!userLongIn)
		{
			System.out.println("What is your longitude?");	
		
			try
			{
				userLong = Double.parseDouble(s.nextLine());
				userLongIn = true;
			}
			catch(NumberFormatException e)
			{
				System.out.println("That is not a valid option.");
				userLongIn = false;
			}
		}
		
		s.close();		
		//END OF USER INPUT - START OF METHOD
		
		
		//Creating semaphores to lock num Drivers
		SortedMap<String, Semaphore> restSemaphores = new TreeMap<String, Semaphore>();
		for(int i = 0; i < restaurants.data.size(); i++)
		{
			restSemaphores.put(restaurants.data.get(i).getName(), new Semaphore(restaurants.data.get(i).getDrivers()));
		}
	
		System.out.println("Starting execution of program...");
		
		ExecutorService exec = Executors.newCachedThreadPool();
		
		long start = System.currentTimeMillis();
		DriverOrder.setUserLoctation(userLat, userLong);
		DriverOrder.setStartTime(start);
		
		int i = 0;
		long elapsedTime = System.currentTimeMillis() - start;
		while(i < orders.size())
		{
			while(elapsedTime / 1000 >= orders.get(i).getTime())
			{
				Restaurant rest = restaurants.fetchRestaurant(orders.get(i).getRestaurant());
				Semaphore semaphore = restSemaphores.get(orders.get(i).getRestaurant()); 
				exec.execute(new DriverOrder(rest, semaphore, orders.get(i)));
				
				i++;
				if(i >= orders.size())
				{
					break;
				}
			}
			
			elapsedTime = System.currentTimeMillis() - start;
			
		}
		
		exec.shutdown();
		while(!exec.isTerminated())
		{
			Thread.yield();
		}
		
		System.out.println("All Orders Complete");
	}
	
	//END OF MAIN
	
}


