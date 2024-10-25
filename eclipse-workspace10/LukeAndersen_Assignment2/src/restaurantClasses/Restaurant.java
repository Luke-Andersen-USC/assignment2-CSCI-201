package restaurantClasses;
import java.util.Vector;

public final class Restaurant {

	String name;
	String address;
	Double latitude;
	Double longitude;
	int drivers;
	Vector<String> menu;
	
	Restaurant(String name, String address, Double latitude, Double longitude, int drivers, Vector<String> menu)
	{
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.drivers = drivers;
		this.menu = menu;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newName)
	{
		name = newName;
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String newAddress)
	{
		address = newAddress;
	}
	
	public Double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(Double newLatitude)
	{
		latitude = newLatitude;
	}
	
	public Double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(Double newLongitude)
	{
		longitude = newLongitude;
	}
	
	public Vector<String> getMenu()
	{
		return menu;
	}
	
	public void setMenu(Vector<String> newMenu)
	{
		menu = newMenu;
	}
	
	public int getDrivers()
	{
		return drivers;
	}
	
	public void setDrivers(int newDrivers)
	{
		drivers = newDrivers;
	}
	

}
