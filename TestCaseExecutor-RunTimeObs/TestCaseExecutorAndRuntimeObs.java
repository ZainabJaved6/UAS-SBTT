import java.util.concurrent.TimeUnit;

import RunTimeMissionInfoSaving.MissionRepresentation;
import RunTimeMissionInfoSaving.Vehicle;
import main.Main;
import uavAdapter.UAV;

/**
 * 
 */

/**
 * @author Zainab
 *
 */
public class TestCaseExecutorAndRuntimeObs {
	public static MissionRepresentation formationFlight = new MissionRepresentation();
	public static Vehicle leaderDrone = new Vehicle();
	public static Vehicle follower1Drone = new Vehicle();
	public static Vehicle follower2Drone = new Vehicle();	
	public static int missionID=33; //used while saving instance, missionID represents a path of the transition tree

	/**
	 * 
	 */
	public TestCaseExecutorAndRuntimeObs() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		

	}
	
	public static void executeScenario(String s) throws InterruptedException
	{
		Main.runMultiCopter();
		UAV Uav = new UAV(10003, 10004);
		int id = 1;
		double distToObsL1, distToObsL2, distToObsF1, distToObsF2;
		String instanceSavedAgainstTransition=""; //scenario name, transition name associating it with the instance to be generated
		Boolean check=false;
		Boolean breakFromWhile = false;
		String expectedState="";
		if (s.equals("MC"))
		{
			//instance for this is saved therefore commenting it out
  			instanceSavedAgainstTransition="ModeChangeFlyingToLand";
  			Uav.uav.land(id,10, 0.5);
  			leaderDrone.targetState="Land"; //should get mode using method
  			//leaderDrone.currentMode=
  			System.out.println("ModeChangeFlyingToLand: " + Uav.uav.get_mode(1));
  			ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
  		//	transitionID[j]++;
		}
		else if (s.equals("ST"))
		{
			instanceSavedAgainstTransition="SharpTurn-SafeDistanceViolation";
    		System.out.println("SharpTurn-SafeDistanceViolation");
    	//	transitionID++;
    		int turnRateL, turnRateF1, turnRateF2;
    		
    	//	while(!(distToWp<5))
    		//	distToWp = leader.calCurrentDistFromWaypoint(leader, FormationFlight);
    		//if(turnRate>30 && distToObs<6)
    		
    		//while(leaderDrone.numberOfWaypointsCovered!=formationFlight.totalNumberOfWaypoints) //check if they are ever equal 
    	//	while(Uav.uav.arm)
    	//	leaderDrone.GetSetCurrentWaypoint(Uav, 1);
    		leaderDrone.GetSetCurrentWaypoint(Uav, 1);
    		double dist = (int)leaderDrone.calCurrentDistFromWaypoint(formationFlight, 0);
    		while(leaderDrone.currentWaypoint!=formationFlight.waypoints.get(0).waypointID && dist>1) //loops un till the dist between leader and home is less than 3
    		{	//Continuously updating current lat long waypoint
    			//System.out.println("leade current wp: "+Uav.uav.get_current_waypoint(1));
    			leaderDrone.GetSetHeadingLatLongID(Uav, 1);
    			follower1Drone.GetSetHeadingLatLongID(Uav, 2);
    			follower2Drone.GetSetHeadingLatLongID(Uav, 3);
    			
        		leaderDrone.GetSetCurrentWaypoint(Uav,1);
		//		follower1Drone.GetSetCurrentWaypoint(Uav,2); We can only get current waypoint against the leader
		//		follower2Drone.GetSetCurrentWaypoint(Uav,3);
				leaderDrone.GetSetHeadingLatLongID(Uav, 1);
				follower1Drone.GetSetHeadingLatLongID(Uav, 2);
				follower2Drone.GetSetHeadingLatLongID(Uav, 3);
    			
			//	 turnRateL = (int) formationFlight.CalculateTurnRateUsingRollPitch(Uav, 1);
			//	turnRateL =  formationFlight.CalculateTurnRateUsingMissionPlannerFormula(Uav, 1);
        		 turnRateF1 = (int) formationFlight.CalculateTurnRateUsingRollPitch(Uav, 2);
        		 turnRateF2 = (int) formationFlight.CalculateTurnRateUsingRollPitch(Uav, 3);
				//turnRateF1 = formationFlight.CalculateTurnRateUsingMissionPlannerFormula(Uav, 2);
        		//turnRateF2 = formationFlight.CalculateTurnRateUsingMissionPlannerFormula(Uav, 3);
        		// distToObsL1 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
        		// distToObsL2 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
        		 distToObsF1 = follower1Drone.getDistanceFromObstacle(follower1Drone.currentLatitude, follower1Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
        		 distToObsF2 = follower2Drone.getDistanceFromObstacle(follower2Drone.currentLatitude, follower2Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
        		
    			
    			if(turnRateF1>20 && turnRateF1<200 )
    				{
    				if(distToObsF1>=0 && distToObsF1<6)
        			{
    					follower1Drone.turnRate=turnRateF1;
    				 TimeUnit.SECONDS.sleep(1); //wait for 1 second and check again if the distance has increased or not
    				 if(distToObsF1>=0 && distToObsF1<6) 
	        			{follower1Drone.currentDistanceToObstacle=distToObsF1;
    					 if(Uav.uav.get_mode(2)=="GUIDED" || Uav.uav.get_mode(2)=="AUTO" || Uav.uav.get_mode(2)=="FOLLOW")
	        				{
    						 	follower1Drone.targetState = "FlyToWaypoint";
	        					follower1Drone.currentGoal = "ExecuteMission";
	        					ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
	        					break;
	        				}
	        			}
	        			//else if (follower1.timetoWait!=0)
	        				//targetState= "Wait";
        			}
    		}
        		
        		if(turnRateF2>20 && turnRateF2<200 ) //can be > than 200 as well, not modifiying now, as other instances have been gene using 200 value
        		{
    				if(distToObsF2>=0 && distToObsF2<6)
        			{
    					follower2Drone.turnRate=turnRateF2;
        			TimeUnit.SECONDS.sleep(1); //wait for 1 second and check again if the distance has increased or not
        			 if(distToObsF2>=0 && distToObsF2<6) 
	        			{follower2Drone.currentDistanceToObstacle=distToObsF1;
	        			 if(Uav.uav.get_mode(3).equals("GUIDED") || Uav.uav.get_mode(3).equals("EQUALS") || Uav.uav.get_mode(3).equals("FOLLOW"))
	        				{
	        				 	follower2Drone.targetState = "FlyToWaypoint";
	        					follower2Drone.currentGoal = "ExecuteMission";
	        					ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);

	        					break;						        				
	        				}
	        			}
	        			//else if (follower2.timetoWait!=0)
	        				//targetState= "Wait";
    				}
    				
        		}
        	/*	if((turnRateL>30 && turnRateL<200) && ((distToObsL1>=0 && distToObsL1<6) || (distToObsL2>=0 && distToObsL2<6)))
        			{
	        			if(Uav.uav.get_mode(1)=="GUIDED" || Uav.uav.get_mode(1)=="AUTO" || Uav.uav.get_mode(1)=="FOLLOW")
	        				{
	        					targetState = "FlyToWaypoint"; 
	        					break;
	        				}
	        			//else if (leader.timetoWait!=0)
	        				//targetState= "Wait";
        			}   */		
        		
    			
    		}

		}
		else if (s.equals("WV"))
		{
		
		instanceSavedAgainstTransition="Wind"; //the direction of wind is same as the direction of UAV
		System.out.println("Wind-SafeDistanceViolation");
		
		leaderDrone.GetSetHeadingLatLongID(Uav, 1);
		follower1Drone.GetSetHeadingLatLongID(Uav, 2);
		follower2Drone.GetSetHeadingLatLongID(Uav, 3);
		int oppo = (int) (follower1Drone.currentHeading+180) % 360;
		Uav.uav.write_parameter(2, "SIM_WIND_DIR", oppo);
		
		follower1Drone.windSpeed = 50;
		follower1Drone.windDirection=oppo;
		
		follower2Drone.GetSetHeadingLatLongID(Uav, 2);
		oppo = (int) (follower2Drone.currentHeading+180) % 360;
		Uav.uav.write_parameter(3, "SIM_WIND_DIR", oppo);
		
		follower2Drone.windSpeed = 50;
		follower2Drone.windDirection=oppo;
		
		leaderDrone.GetSetHeadingLatLongID(Uav, 2);
		oppo = (int) (leaderDrone.currentHeading+180) % 360;
		Uav.uav.write_parameter(1, "SIM_WIND_DIR", oppo);
		
		leaderDrone.windSpeed = 50;
		leaderDrone.windDirection=oppo;
		
		
		
		//oppo = (int) (leaderDrone.currentHeading+180) % 360;
	//	Uav.uav.write_parameter(1, "SIM_WIND_DIR", oppo);
		Uav.uav.write_parameter(1, "SIM_WIND_SPD", 50);
		
		
		Uav.uav.write_parameter(2, "SIM_WIND_SPD", 50);
		Uav.uav.write_parameter(3, "SIM_WIND_SPD", 50);
		
		leaderDrone.GetSetCurrentWaypoint(Uav, 1);
		
		double dist = (int)leaderDrone.calCurrentDistFromWaypoint(formationFlight, 0);
		while(leaderDrone.currentWaypoint!=0 && dist>0) //loops un till the dist between leader and home is less than 1
		{		        			
			
			leaderDrone.GetSetCurrentWaypoint(Uav,1);
			//	follower1Drone.GetSetCurrentWaypoint(Uav,2);
			//	follower2Drone.GetSetCurrentWaypoint(Uav,3);
				leaderDrone.GetSetHeadingLatLongID(Uav, 1);
				follower1Drone.GetSetHeadingLatLongID(Uav, 2);
				follower2Drone.GetSetHeadingLatLongID(Uav, 3);
			 distToObsL1 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
    		 distToObsL2 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
    		 distToObsF1 = follower1Drone.getDistanceFromObstacle(follower1Drone.currentLatitude, follower1Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
    		 distToObsF2 = follower2Drone.getDistanceFromObstacle(follower2Drone.currentLatitude, follower2Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
    		
    		 if(distToObsF1>0 && distToObsF1<6)
    		 {
    			 TimeUnit.SECONDS.sleep(2); //wait for 2 second and check again if the distance has increased or not
    			 if(distToObsF1>0 && distToObsF1<6)
        		 {follower1Drone.currentDistanceToObstacle=distToObsF1;
    			 if(Uav.uav.get_mode(2).equals("GUIDED") || Uav.uav.get_mode(2).equals("EQUALS") || Uav.uav.get_mode(2).equals("FOLLOW"))
    				{
    				 follower1Drone.targetState = "FlyToWaypoint";
    				 follower1Drone.currentGoal = "ExecuteMission"; //should be avoid collision

    				 ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
    				 	Uav.uav.write_parameter(1, "SIM_WIND_SPD", 0);
	    				Uav.uav.write_parameter(2, "SIM_WIND_SPD", 0);
	        			Uav.uav.write_parameter(3, "SIM_WIND_SPD", 0);
    					break;
    				}
        		 }
    		 }
    		 else if (distToObsF2>0 && distToObsF2<6)
    		 {
    			 TimeUnit.SECONDS.sleep(2); //wait for 1 second and check again if the distance has increased or not
    			 if(distToObsF2>0 && distToObsF2<6)
    			 {follower2Drone.currentDistanceToObstacle=distToObsF2;
    			 if(Uav.uav.get_mode(3).equals("GUIDED") || Uav.uav.get_mode(3).equals("EQUALS") || Uav.uav.get_mode(3).equals("FOLLOW"))
    				{
    					follower2Drone.targetState = "FlyToWaypoint"; 
    					follower2Drone.currentGoal = "ExecuteMission"; //should be avoid collision

    					ClassDiagramInstanceGenerator.saveInstance(Uav,missionID,instanceSavedAgainstTransition);
    					Uav.uav.write_parameter(1, "SIM_WIND_SPD", 0);
	    				Uav.uav.write_parameter(2, "SIM_WIND_SPD", 0);
	        			Uav.uav.write_parameter(3, "SIM_WIND_SPD", 0);
    					break;
    				}
    			 }
    		 }
		}

		}
		
		else if (s.equals("SV"))
		{
			instanceSavedAgainstTransition="SafeDistanceViolation";
    		System.out.println("SafeDistanceViolation");
    		double dist = (int)leaderDrone.calCurrentDistFromWaypoint(formationFlight, 0);
    		while(leaderDrone.currentWaypoint!=0 && dist>1) //loops un till the dist between leader and home is less than 1
    		{
    			if(breakFromWhile==true)
    			{break;}
    			leaderDrone.GetSetCurrentWaypoint(Uav,1);
			//	follower1Drone.GetSetCurrentWaypoint(Uav,2);
			//	follower2Drone.GetSetCurrentWaypoint(Uav,3);
				leaderDrone.GetSetHeadingLatLongID(Uav, 1);
				follower1Drone.GetSetHeadingLatLongID(Uav, 2);
				follower2Drone.GetSetHeadingLatLongID(Uav, 3);
    			
				 distToObsL1 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
        		 distToObsL2 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
        		 distToObsF1 = follower1Drone.getDistanceFromObstacle(follower1Drone.currentLatitude, follower1Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
        		 distToObsF2 = follower2Drone.getDistanceFromObstacle(follower2Drone.currentLatitude, follower2Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
        		
        		 if(distToObsF1>0 && distToObsF1<6)
        		 {
        			 TimeUnit.SECONDS.sleep(2); //wait for 1 second and check again if the distance has increased or not
        			 if(distToObsF1>0 && distToObsF1<6)
	        		 {follower1Drone.currentDistanceToObstacle=distToObsF1;
        			 if(Uav.uav.get_mode(2).equals("GUIDED") || Uav.uav.get_mode(2).equals("AUTO") || Uav.uav.get_mode(2).equals("FOLLOW"))
        				{
        					follower1Drone.targetState = "FlyToWaypoint";
        					follower1Drone.currentGoal = "ExecuteMission"; //should be avoid collision

        					ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);

        					breakFromWhile=true;
        					break;
        				}
	        		 }
        		 }
        		 else if (distToObsF2>0 && distToObsF2<6)
        		 {
        			 TimeUnit.SECONDS.sleep(2); //wait for 1 second and check again if the distance has increased or not
        			 if(distToObsF2>0 && distToObsF2<6)
        			 {follower2Drone.currentDistanceToObstacle=distToObsF2;
        			 if(Uav.uav.get_mode(3).equals("GUIDED") || Uav.uav.get_mode(3).equals("AUTO") || Uav.uav.get_mode(3).equals("FOLLOW"))
        				{
        					follower2Drone.targetState = "FlyToWaypoint"; 
        					follower2Drone.currentGoal = "ExecuteMission"; //should be avoid collision

        					ClassDiagramInstanceGenerator.saveInstance(Uav,missionID,instanceSavedAgainstTransition);
        					breakFromWhile=true;
        					break;
        				}
        			 }
        		 }
        		/* else if (distToObsL2>0 && distToObsL2<6 || distToObsL1>0 && distToObsL1<6)
        		 {
        			 if(Uav.uav.get_mode(1)=="GUIDED" || Uav.uav.get_mode(1)=="AUTO" || Uav.uav.get_mode(1)=="FOLLOW")
        				{
        					targetState = "FlyToWaypoint"; 
        					break;
        				}
        		 }*/
    			
    		}

		
	}
	else if(s.equals("FE"))
	{
		instanceSavedAgainstTransition="FlyingToError";
		System.out.println("FlyingToError");
		
		follower1Drone.GetSetHeadingLatLongID(Uav, 2);
		int oppo = (int) (follower1Drone.currentHeading+100) % 360; //180 gives complete opposite direction of wind
		Uav.uav.write_parameter(2, "SIM_WIND_DIR", oppo);			//we need a crosswind to take the drone away from its waypoint
		
		//follower1Drone.windSpeed = 30;
		follower1Drone.windDirection=oppo;
		
		follower2Drone.GetSetHeadingLatLongID(Uav, 2);
		oppo = (int) (follower2Drone.currentHeading+100) % 360;
		Uav.uav.write_parameter(3, "SIM_WIND_DIR", oppo);
		
		//follower2Drone.windSpeed = 30;
		follower2Drone.windDirection=oppo;
		
		leaderDrone.GetSetHeadingLatLongID(Uav, 2);
		oppo = (int) (leaderDrone.currentHeading+100) % 360; 
		Uav.uav.write_parameter(1, "SIM_WIND_DIR", oppo);
		
		leaderDrone.windSpeed = 30;
		leaderDrone.windDirection=oppo;
		
		Uav.uav.write_parameter(1, "SIM_WIND_SPD", 30);
	//	
		
		double dist = (int)leaderDrone.calCurrentDistFromWaypoint(formationFlight, 0);
	//	while(leaderDrone.currentWaypoint!=0 && dist>1) //loops un till the dist between leader and home is less than 1
		while(dist>1)
		{
			
			//Uav.uav.write_parameter(2, "SIM_WIND_SPD", 30);
			//Uav.uav.write_parameter(1, "SIM_WIND_SPD", 30);
			
			leaderDrone.GetSetCurrentWaypoint(Uav, 1);
			leaderDrone.GetSetHeadingLatLongID(Uav, 1);
			follower1Drone.GetSetHeadingLatLongID(Uav, 2);
			formationFlight.expectedUAVHeading=follower1Drone.currentHeading;
			double distToWp = leaderDrone.calCurrentDistFromWaypoint(formationFlight, leaderDrone.currentWaypoint);
			//int ind = formationFlight.waypoints..indexOf(leaderDrone.currentWaypoint);
    		 formationFlight.expectedUAVHeading=follower1Drone.currentHeading;
    		 if ((distToWp>formationFlight.waypoints.get(leaderDrone.currentWaypoint).distanceFromPreviousWaypoint) || (leaderDrone.currentHeading!=follower1Drone.currentHeading && leaderDrone.currentHeading!=follower1Drone.currentHeading+5 && leaderDrone.currentHeading!=follower1Drone.currentHeading-5)) 
    		 {
    			 TimeUnit.SECONDS.sleep(6); //wait for 1 second and check again if the distance has increased or not
        		 if ((distToWp>formationFlight.waypoints.get(leaderDrone.currentWaypoint).distanceFromPreviousWaypoint) || (leaderDrone.currentHeading!=follower1Drone.currentHeading && leaderDrone.currentHeading!=follower1Drone.currentHeading+5 && leaderDrone.currentHeading!=follower1Drone.currentHeading-5)) 
        		 {
        			 System.out.println("Mode: "+Uav.uav.get_mode(1)); //not returning correct mode, shows RTL, but is GUIDED, so commenting this for now
    			// if(Uav.uav.get_mode(1).equals("GUIDED") || Uav.uav.get_mode(1).equals("AUTO") || Uav.uav.get_mode(1).equals("FOLLOW"))
    			//	{
    				 	leaderDrone.targetState = "FlyToWaypoint";
    					//targetState = "FlyToWaypoint"; 
    					expectedState = "ErrorState";
    					leaderDrone.isWaypointLost=true;
    					ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
	    				Uav.uav.write_parameter(1, "SIM_WIND_SPD", 0);

    					break;
    			//	}
        		 }
    		 }
    		 
		}

	}
	else if(s.equals("SD"))
	{
		instanceSavedAgainstTransition="AtSafeDistance";
		System.out.println("AtSafeDistance");
		double dist = (int)leaderDrone.calCurrentDistFromWaypoint(formationFlight, 0);
		while(leaderDrone.currentWaypoint!=0 && dist>1) //loops un till the dist between leader and home is less than 1
		{
			leaderDrone.GetSetCurrentWaypoint(Uav,1);
		//	follower1Drone.GetSetCurrentWaypoint(Uav,2);
		//	follower2Drone.GetSetCurrentWaypoint(Uav,3);
			leaderDrone.GetSetHeadingLatLongID(Uav, 1);
			follower1Drone.GetSetHeadingLatLongID(Uav, 2);
			follower2Drone.GetSetHeadingLatLongID(Uav, 3);
			
			 distToObsL1 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
    		 distToObsL2 = leaderDrone.getDistanceFromObstacle(leaderDrone.currentLatitude, leaderDrone.currentLongitude, follower1Drone.currentLatitude, follower1Drone.currentLongitude);
    		 distToObsF1 = follower1Drone.getDistanceFromObstacle(follower1Drone.currentLatitude, follower1Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
    		 distToObsF2 = follower2Drone.getDistanceFromObstacle(follower2Drone.currentLatitude, follower2Drone.currentLongitude, leaderDrone.currentLatitude, leaderDrone.currentLongitude);
    		
    		// if(distToObsF1>0 && distToObsF1<6)
    		// {
    		//	 TimeUnit.SECONDS.sleep(2); //wait for 1 second and check again if the distance has increased or not
    			 if(distToObsF1>6)
        		 {
    				 	
    				 	follower1Drone.targetState="FlyToWaypoint";
    				 	follower1Drone.currentGoal="ExecuteMission";
    					//targetState = "FlyToWaypoint"; 
    				 	ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);

    					break;
    				
        		 }
    		// }
    		// else if (distToObsF2>0 && distToObsF2<6)
    		// {
    			// TimeUnit.SECONDS.sleep(2); //wait for 1 second and check again if the distance has increased or not
    			 if(distToObsF2>6)
    			 {
    				 follower2Drone.targetState="FlyToWaypoint";
    				 follower2Drone.currentGoal="ExecuteMission";

    				 ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
    			 	break;
    			 }
    
    		 //}
    		
			
		}


	}
	else if (s.equals("EL"))
	{
		instanceSavedAgainstTransition="ErrorToLand";
		System.out.println("ErrorToLand");
			if(expectedState.equals("ErrorState"))
			{
				Uav.uav.land(id,10, 0.5);
				System.out.println("Mode should be Land: "+Uav.uav.get_mode(1));
    			leaderDrone.targetState=Uav.uav.get_mode(1); //should be land
    			ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
			}
	}
		
	else if (s.equals("ER"))
	{
		instanceSavedAgainstTransition="ErrorToRTL";
		System.out.println("ErrorToRTL");
    		if(expectedState.equals("ErrorState"))
			{
    			Uav.uav.rtl(id,10, 0.5);
    			System.out.println("Mode should be RTL: "+Uav.uav.get_mode(1));
    			leaderDrone.targetState=Uav.uav.get_mode(1); //should be land
    			ClassDiagramInstanceGenerator.saveInstance(Uav, missionID, instanceSavedAgainstTransition);
			}
	}
	
}
	

}
