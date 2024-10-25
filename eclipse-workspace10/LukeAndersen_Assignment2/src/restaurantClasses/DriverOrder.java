package restaurantClasses;

import java.lang.Math;
import java.util.concurrent.Semaphore;

public class DriverOrder extends Thread {
	public static double UserLat;
	public static double UserLong;
	public static long StartTime;
	
	private Semaphore mySemaphore;
	private Order myOrder;
	private Restaurant myRestaurant;
	
	DriverOrder(Restaurant rest, Semaphore s, Order order)
	{
		mySemaphore = s;
		myOrder = order;
		myRestaurant = rest;
	}
	
	public static void setUserLoctation(double uLat, double uLong)
	{
		UserLat = uLat;
		UserLong = uLong;
	}
	
	public static void setStartTime(long sTime)
	{
		StartTime = sTime;
	}
	
	public void run()
	{
		
		try {
			mySemaphore.acquire();
		
			
			System.out.println(Util.getElapsedTime(StartTime) + " Starting delivery of " + myOrder.getMenuItem() + " from " + myOrder.getRestaurant() + "!");
			
			double dist = DistanceCalc(UserLat, UserLong, myRestaurant.getLatitude(), myRestaurant.getLongitude());
			
			Thread.sleep((long) (dist * 2000.0f));
			
			System.out.println(Util.getElapsedTime(StartTime) +  " Finished Delivery of " + myOrder.getMenuItem() + " from " + myOrder.getRestaurant() + "!");
		
		}
		catch(InterruptedException ie)
		{
			System.out.println("Order was interrupted:" + ie.getMessage());
		}
		finally
		{
			mySemaphore.release();
		}
	}
	
	private double DistanceCalc(double lat1, double long1, double lat2, double long2)
	{
		double dist = 3963.0f * Math.acos(Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + 
				Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(long2 - long1)) );
		
		return dist;
	}
	
	
}
