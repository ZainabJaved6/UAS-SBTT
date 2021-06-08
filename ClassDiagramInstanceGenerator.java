import java.util.HashMap;

import RunTimeMissionInfoSaving.MissionRepresentation;
import RunTimeMissionInfoSaving.Vehicle;
import RunTimeMissionInfoSaving.WriteToObjectFile;
import uavAdapter.UAV;

/**
 * 
 */

/**
 * @author Zainab
 *
 */
public class ClassDiagramInstanceGenerator {

	/**
	 * 
	 */
	public static MissionRepresentation formationFlight = new MissionRepresentation();
	public static Vehicle leaderDrone = new Vehicle();
	public static Vehicle follower1Drone = new Vehicle();
	public static Vehicle follower2Drone = new Vehicle();
	
	public ClassDiagramInstanceGenerator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 
	 * A method to save run time mission instance against a particular transition/scenario
	 * The info is saved in the form of a script that the OCLSolver is able to take as input for constraint evaluation
	 * @param Uav
	 * @param missionID: ID of the selected transition tree path, one transition tree path represents one mission/test case
	 * @param instanceSavedAgainstTransition: name of the transition/scenario, required for instance file name to be created
	 */
	public static void saveInstance(UAV Uav, int missionID, String instanceSavedAgainstTransition)
	{
	//Get set loc info in vehicle instances
		if(leaderDrone.currentHeading==0)
	leaderDrone.GetSetHeadingLatLongID(Uav, 1);
		if(follower1Drone.currentHeading==0)
	follower1Drone.GetSetHeadingLatLongID(Uav, 2);
		if(follower2Drone.currentHeading==0)
	follower2Drone.GetSetHeadingLatLongID(Uav, 3);
	
	
	String locStr = Uav.uav.getLocationAll();
	String locArr[] = locStr.split("@");
	HashMap <Integer,HashMap<String,Double>>  loc = new HashMap <Integer,HashMap<String,Double>>();
	int index = 1;
	for (String a : locArr) { 
		loc.put(index, Uav.LocationMapper(a));
		index++;
	}
	System.out.println(loc);
	
	
	//getting and setting current waypoint
	leaderDrone.GetSetCurrentWaypoint(Uav,1);
	follower1Drone.GetSetCurrentWaypoint(Uav,1); //current waypoint can be accessed only for the leader
	follower2Drone.GetSetCurrentWaypoint(Uav,1);							
	
	
	/*HashMap<String,Double> wp3 = Uav.LocationMapper(Uav.uav.get_waypoint(2, 1));
	System.out.println(wp3.toString());
	follower2.currentWaypoint = Integer.parseInt(wp3.toString());
	follower2.numberOfWaypointsCovered = Integer.parseInt(wp3.toString())-1;*/
	
	//calculating distance from obstacle and distance from waypoint
	
	//follower1.calculateDistanceFromObstacle(leader,follower1);
	//follower2.calculateDistanceFromObstacle(leader,follower2);
	
	
	leaderDrone.calCurrentDistFromWaypoint(formationFlight);
	follower1Drone.calCurrentDistFromWaypoint(formationFlight);
	follower2Drone.calCurrentDistFromWaypoint(formationFlight);
	
	//End calculating and setting distance from obstacle and distance from waypoint
	
	//Setting mode, current battery, gps loss
	//-----------------Mode
	leaderDrone.currentMode = (Uav.uav.get_mode(1));
	follower1Drone.currentMode = (Uav.uav.get_mode(2));
	follower2Drone.currentMode = (Uav.uav.get_mode(3));
	
	//-------------Battery
	HashMap<String,Double> bat = Uav.LocationMapper(Uav.uav.get_sys_status(1));
	String s = bat.toString();
	String[] commaSplitBat = s.split(",");
	String[] equalSplit = commaSplitBat[0].split("=");
	leaderDrone.currentBattery = Double.parseDouble(equalSplit[1]);
	
	HashMap<String,Double> bat1 = Uav.LocationMapper(Uav.uav.get_sys_status(2));
	String s1 = bat1.toString();
	String[] commaSplitBat1 = s1.split(",");
	String[] equalSplit1 = commaSplitBat1[0].split("=");
	follower1Drone.currentBattery = Double.parseDouble(equalSplit1[1]);
	
	HashMap<String,Double> bat2 = Uav.LocationMapper(Uav.uav.get_sys_status(3));
	String s2 = bat2.toString();
	String[] commaSplitBat2 = s2.split(",");
	String[] equalSplit2 = commaSplitBat2[0].split("=");
	follower2Drone.currentBattery = Double.parseDouble(equalSplit2[1]);
	
	//-------------GPS
	leaderDrone.setGPSStatus(leaderDrone, Uav, 1);
	follower1Drone.setGPSStatus(follower1Drone, Uav, 2);
	follower2Drone.setGPSStatus(follower2Drone, Uav, 3);
	
	
	//Set ground speed, airspeed, wind speed and direction
	leaderDrone.setGroundSpeedAirSpeedWindSpeedDirec(Uav, 1);
	follower1Drone.setGroundSpeedAirSpeedWindSpeedDirec(Uav, 2);
	follower2Drone.setGroundSpeedAirSpeedWindSpeedDirec(Uav, 3);
	
	//-------------Angle between waypoints, speed on waypoints, turn rate saved against each next / current waypoint
//	formationFlight.CalculateTurnRate(Uav);
	
	
	
	//End
	
	//Getting setting flight specifications - climb and descent rates
	//leader
	//PILOT_SPEED_UP_DN are not stable parameters - sometimes give value of FLTTIME and RUNTIME parameter values
	
	leaderDrone.GetSetClimbDescentRate(leaderDrone, Uav, 1);
	follower1Drone.GetSetClimbDescentRate(follower1Drone, Uav, 2);
	follower2Drone.GetSetClimbDescentRate(follower2Drone, Uav, 3);
	
	leaderDrone.saveInstanceName(1);
	follower1Drone.saveInstanceName(2);
	follower2Drone.saveInstanceName(3);
	
	//leaderDrone.targetState="FlyToWaypoint";
	//follower1Drone.targetState="FlyToWaypoint";
	//follower2Drone.targetState="FlyToWaypoint";
	
	formationFlight.drone.add(leaderDrone);
	formationFlight.drone.add(follower1Drone);
	formationFlight.drone.add(follower2Drone);
	
	
	
	//Write information in file
	WriteToObjectFile w = new WriteToObjectFile();
	//w.writeToFile(FormationFlight);
	//We use an exiting code for the modification of class diagram instance generation
	String ApplicationSpecificClassDiagramPath = "path";
	//formationflight contains runtime info
	//mission ID and instanceSavedAgainstTransition var are used in class diagram instances generated
	w.generateScript(ApplicationSpecificClassDiagramPath, formationFlight,missionID, instanceSavedAgainstTransition);
	
	}


}
