import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import uavAdapter.UAV;
import RunTimeMissionInfoSaving.MissionRepresentation;
import RunTimeMissionInfoSaving.Vehicle;
import RunTimeMissionInfoSaving.WriteToObjectFile;


/**
 * 
 */

/**
 * @author Zainab
 *
 */
public class TestSequenceGenerator {

	/**
	 * 
	 */
	public TestSequenceGenerator() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	
	public static MissionRepresentation formationFlight = new MissionRepresentation();
	public static Vehicle leaderDrone = new Vehicle();
	public static Vehicle follower1Drone = new Vehicle();
	public static Vehicle follower2Drone = new Vehicle();	
	public static int globalFormationMissionCounter=33;
	public static int missionID=33; //used while saving instance, missionID represents a path of the transition tree

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		UAV Uav = new UAV(10003, 10004);
		int id = 1;
//		int id2 = 2;
		
		while(true) {
			System.out.print("Ënter:> ");
			String c = sc.next();
			
			if(c.equals("c")) {
//				Uav.uav.connect("COM19:57600",id2,0);
				Uav.uav.connect("udpin:127.0.0.1:14552",id,0);
				Uav.uav.connect("udpin:127.0.0.1:14562",2,0);
				Uav.uav.connect("udpin:127.0.0.1:14572",3,0);
			}
			else if (c.equals("ctp"))
			{
				if(formationFlight.waypoints.size()==0)
				{
				formationFlight.MissionName = "FormationFlight";
				
				formationFlight.saveMission(Uav,1);}
				
				//Get set loc info in vehicle instances
				leaderDrone.GetSetHeadingLatLongID(Uav, id);
				follower1Drone.GetSetHeadingLatLongID(Uav, 2);
				follower2Drone.GetSetHeadingLatLongID(Uav, 3);
				
				leaderDrone.GetSetCurrentWaypoint(Uav,id);
				follower1Drone.GetSetCurrentWaypoint(Uav,1);
				follower2Drone.GetSetCurrentWaypoint(Uav,1);
				
				//reading transition tree paths to get and write mission
				String PathToTransitionTreeTxtFile = "Path";					
				File myObj = new File(PathToTransitionTreeTxtFile);
			      Scanner myReader;
				try {
					myReader = new Scanner(myObj);
				
					int i =0,k=0;String targetState="";
					
			      //Each line is a new mission
					//code added by zainab
					double distToObsL1, distToObsL2, distToObsF1, distToObsF2;
					String instanceSavedAgainstTransition=""; //scenario name, transition name associating it with the instance to be generated
					Boolean check=false;
					while (myReader.hasNextLine()) 
					{
						String data = null;
						
						if(check) //resetting mission variable for next transition
						{
							if(leaderDrone.windSpeed>25)
								Uav.uav.write_parameter(1, "SIM_WIND_SPD", 0);
							if(follower1Drone.windSpeed>25)
								Uav.uav.write_parameter(2, "SIM_WIND_SPD", 0);
							if(follower2Drone.windSpeed>25)
								Uav.uav.write_parameter(3, "SIM_WIND_SPD", 0);
							if(Uav.uav.get_mode(1).equals("LAND") || Uav.uav.get_mode(1).equals("RTL") )
							{
							//	TimeUnit.SECONDS.sleep(15);
							//	Uav.uav.stabalize(id,10, 0.5);
							//	Uav.uav.arm(1, 10, 0.5);
							//	Uav.uav.takeoff(1,10, 0.5,10);								
							}
							break;
						}
						System.out.println(globalFormationMissionCounter);
						globalFormationMissionCounter++; //an int value to get one mission/ transition tree path at a time
															//to ensure execution of UavFormation mission again for each transition tree path
															//UAVformation mission is required to be executed manually each time
															//after execution of formation mission again, we can type ctp, to re-execute this code 
															//and val of string data to be set to respective transition tree path selected
						
						for(int itr = 0; itr<globalFormationMissionCounter;itr++)						
					    data = myReader.nextLine();	
						
						System.out.println("Data: " + data);
						
						String[] colonSepData = data.split(",");
					     missionID++;
					     String expectedState="";
					   //  int[] transitionID = new int[colonSepData.length];
						 //represents the ID of number of times a particular transition appears in one mission
					     //Clear values of Uav instances
					     
					    //Restart mission //check if mission restarts or not after reaching waypoint 0
					    //check if required or not, as each if executes untill the end of the mission or untill the required consition is met
					     /* Uav.uav.goto_no_wait(1, formationFlight.waypoints.get(0).Latitude, formationFlight.waypoints.get(0).Longitude, 10);
					     Uav.uav.goto_no_wait(2, formationFlight.waypoints.get(0).Latitude, formationFlight.waypoints.get(0).Longitude, 10);
					     Uav.uav.goto_no_wait(3, formationFlight.waypoints.get(0).Latitude, formationFlight.waypoints.get(0).Longitude, 10);
					     */
					//    MissionFileGeneration.RestartUavFormation();
					     leaderDrone.clear();
					     follower1Drone.clear();
					     follower2Drone.clear();
			      	for(int j=0;j<colonSepData.length;j++)
			      	{
			      		Boolean breakFromWhile = false;
			      		Uav.uav.write_parameter(1, "SIM_WIND_SPD", 0); //setting wind speed so the next transition/scenario is not affected by the previously set wind speed
			      		leaderDrone.GetSetCurrentWaypoint(Uav, 1);
			      		
			      		String scenario;
			      		if(colonSepData[j].contains("ModeChangeFlyingToLand")) //Land //no constraints related to this
			      		{
			      			scenario="MC";
			      			TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
			      			
			      		}
				        	else if (colonSepData[j].contains("SharpTurn-SafeDistanceViolation")) //Wait
			      		{
				        		scenario="ST";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        }
				        	else if (colonSepData[j].contains("CommunicationDelay-SafeDistanceViolation")) //Wait
				        		{
				        		instanceSavedAgainstTransition="CommunicationDelay";
				        		System.out.println("CommunicationDelay");

				        		
				        		}
				        					        	/*else if (colonSepData[j].contains("WindDirection-SafeDistanceViolation")) //Wait
				        		{
				        		//	instanceSavedAgainstTransition="WindDirection";
				        			//Uav.uav.write_parameter(1, "SIM_WIND_DIR", 30);
				        		}*/
				        	else if (colonSepData[j].contains("Wind-SafeDistanceViolation")) //Wait //Wind speed direction have combined effect therefore combining both
				        		{
				        		
				        		scenario="WV";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		}
				        	else if (colonSepData[j].contains("SafeDistanceViolation")) // Mode should be Waiting
			        		{
				        		
				        		scenario="SV";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		
			        		}

				        	else if (colonSepData[j].contains("FlyingToError")) //ErrorState
				        		{
				        		
				        		scenario="FE";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		}
			      	
				        	else if (colonSepData[j].contains("missionComplete") && (leaderDrone.numberOfWaypointsCovered==formationFlight.totalNumberOfWaypoints-1)) //RTL
				        		{ //RTL
		        		
					      			System.out.println("missionComplete");
					      			instanceSavedAgainstTransition="missionComplete";
					      			leaderDrone.targetState="RTL"; //hardcoded, the value is RTL but issue in getting the value
					      			leaderDrone.currentGoal = "FlyToWaypoint"; //should be "avoid collision"
					      			ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
		        					        		
				        		}
				        	else if (colonSepData[j].contains("landed")) //Armed
				        		{
				        		instanceSavedAgainstTransition="landed";
				        		}
				        	else if (colonSepData[j].contains("landing")) //Land
				        		{
				        		instanceSavedAgainstTransition="landing";
				        		}
				        	else if (colonSepData[j].contains("AtSafeDistance")) //FlyToWaypoint
				        		{
				        		
				        		scenario="SD";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		
				        		}
				        	else if (colonSepData[j].contains("ErrorToLand")) //Land
				        		{
				        		scenario="EL";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		
				        		
				        		}
				        	else if (colonSepData[j].contains("ErrorToRTL")) //RTL
				        		{
				        		scenario="ER";
				        		TestCaseExecutorAndRuntimeObs.executeScenario(scenario);
				        		
				        		
				        		}
				        	else if (colonSepData[j].contains("disarm")) //Disarmed
				        		k=1;
				        	else if (colonSepData[j].contains("endMission")) //RTL
				        		k=1;
		
					      }check=true; //ensure the while loop executes only once for each uavformation mission
						}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//End reading and writing mission from transition tree
			}
		}

	}

}
