package test.snt.oclsolver;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


import org.junit.Test;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import junit.framework.Assert;
import snt.oclsolver.distance.ClassDiagramTestData;
import snt.oclsolver.distance.SimpleClass;
import snt.oclsolver.eocl.EclipseOCLWrapper;
import snt.oclsolver.search.SearchAlgorithmEnum;
import snt.oclsolver.tuples.ClassifierTuple;
import snt.oclsolver.util.TestUtil;

public class FormationCorrect extends AbstractTestCase{

	
	String path = "ObjectDiagramModel";
	String umlFile =   "examples/formation/BlankPackage.uml";
	String objectFilePath = "formationFlightInstances";
//	String umlFile = "D:/PhD IA/PhD/Experiment/New folder/formation/BlankPackage.uml";
//	String excelFilePath = "formationFlightInstances/Arducopter2/ReportArduCopter.xlsx";
	String excelFilePath = "formationFlightInstances/Arducopter2/report.xlsx";

	
	@Test
	public void testQuery01() throws EncryptedDocumentException, IOException  //safeDistanceviolation
	{
		
		ClassDiagramTestData.getInstance().reset(umlFile);
		//data.clear();
		String query = "context UAV inv :"
				+ "	(self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle)"
				+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter2/SafeDistanceViolation";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data01 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
		 
	    data01.put("0", new Object[]{"Constraint",query});
	    data01.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data01.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		//End
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data01.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data01.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
			
			//Assert.assertEquals("true", str);       
	
	}
	
	
	@Test
	public void testQuery02() throws EncryptedDocumentException, IOException  //missionComplete
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		
		
		String query = "context UAV inv :"
		  		+ "(self.numberOfWaypointsCovered=self.mission.totalNumberOfWaypoints-1 and self.type='ArduCopter') "
		  		+ "implies (self.targetState='RTL'and self.currentGoal='AvoidCollision')";
		
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
        String ObjectPath = "formationFlightInstances/Arducopter2/endMission";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data02 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data02.put("0", new Object[]{"Constraint",query});
	    data02.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data02.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data02.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data02.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	
	}
	@Test
	public void testQuery03() throws EncryptedDocumentException, IOException   //safeDistanceViolation Arducopter
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv :"
		  		+ "(self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter')"
		  		+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";
		
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter2/SafeDistanceViolation";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data03 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data03.put("0", new Object[]{"Constraint",query});
	    data03.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data03.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data03.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data03.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	}
	
	@Test
	public void testQuery04() throws EncryptedDocumentException, IOException   //SharpTurn-SafeDistanceViolation Arducopter
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		
		String query = "context UAV inv :"
		  		+ "(self.turnRate>20 and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter') "
		  		+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";
		
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/SharpTurn";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data04 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data04.put("0", new Object[]{"Constraint",query});
	    data04.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data04.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data04.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data04.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	
	}
	
	/*@Test
	public void testQuery05()    //Communication delay safeDistanceViolation Arducopter
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv :"
		  		+ "(self.communicationDelayed=true and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter') "
		  		+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";

		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		cdtd.initClassesAndEOCL();
	
		
		ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd.getClassifiers();
		this.printResultTuple(ObjectForConstraintEvaluation);		
		
		//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
		
		String str = this.verifyResult(query,ObjectForConstraintEvaluation);
		Assert.assertEquals("true", str);
		
	
	}*/
	
	@Test
	public void testQuery06() throws EncryptedDocumentException, IOException   //Wind-SafeDistanceViolation Arducopter wind wpeed >25 and direction same as UAV heading
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		/*String query = "context UAV inv :"
				+ "(self.windSpeed>self.flightSpecification.windResistance and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter') "
				+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";*/
		
		//Environmental factor is associated with the 
		/*String query = "context UAV inv :" 
				+ "(self.environmentalFactor->exists(e|e.value1>self.flightSpecification.windResistance and ((self.currentHeading>180 and (e.value2=360-self.currentHeading or e.value2=360-self.currentHeading+30 or e.value2=360-self.currentHeading-30)) or (self.currentHeading<=180 and (e.value2=self.currentHeading+180 or e.value2=self.currentHeading+180+30 or e.value2=self.currentHeading+180-30)))) and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter') "
				+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";*/
		
		//ocl solver doesnot support mod therefore we have used <> not equal sign. we needed value2=(currentHeading+180) % 360;
		String query = "context UAV inv :"    
				+ "(self.mission.environmentalFactor->forAll(e|e.value1>self.flightSpecification.windResistance and "
				+ "self.currentHeading<>e.value2 and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and "
				+ "self.type='ArduCopter')) "
				+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";
		/*String query = "context UAV inv :"
				+ "(self.mission.environmentalFactor->exists(e|e.value1>self.flightSpecification.windResistance and self.currentDistanceToObstacle<self.mission.safeDistanceFromObstacle and self.type='ArduCopter')) "
				+ "implies (self.targetState='Wait' and self.currentGoal='AvoidCollision')";*/
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/Wind";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data06 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data06.put("0", new Object[]{"Constraint",query});
	    data06.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
		//	ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     System.out.println("Result: "+str);
		     data06.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data06.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data06.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	
	}
	
	
	@Test
	public void testQuery08() throws EncryptedDocumentException, IOException //if goal in execute mission than targetState should be FlyToWaypoint
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv :"
				+ "(self.currentGoal='ExecuteMission' )"
				+ "implies (self.targetState='FlyToWaypoint')";
		
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/AtSafeDistance";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data08 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data08.put("0", new Object[]{"Constraint",query});
	    data08.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data08.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data08.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data08.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	
	}
	
	@Test
	public void testQuery09() throws EncryptedDocumentException, IOException //FlyingToError
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv: (self.mission.waypoint->exists(w|w.waypointID=self.currentWaypoint and "
				+ "w.distanceFromPreviousWaypoint<self.currentDistanceFromWaypoint) or "
				+ "(self.currentHeading<>self.expectedUAVHeading and self.currentHeading<>self.expectedUAVHeading-5 and "
				+ "self.currentHeading<>self.expectedUAVHeading+5))"
				+ "implies (self.targetState='ErrorState')";
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/FlyingToError";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data09 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data09.put("0", new Object[]{"Constraint",query});
	    data09.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data09.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data09.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data09.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	}
	
	@Test
	public void testQuery10() throws EncryptedDocumentException, IOException //ErrorToLand 
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv: (self.isWaypointLost=true)"
				+ "implies (self.targetState='Land')";
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/ErrorToLand";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	  TreeMap<String, Object[]> data10 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data10.put("0", new Object[]{"Constraint",query});
	    data10.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     System.out.println("Result: "+str);
		     data10.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data10.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data10.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
	}
	
	@Test
	public void testQuery11() throws EncryptedDocumentException, IOException //ErrorToRTL
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv: (self.isWaypointLost=true)"
				+ "implies (self.targetState='RTL')";
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter3/ErrorToRTL";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data11 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data11.put("0", new Object[]{"Constraint",query});
	    data11.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles();
		
		
		     
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data11.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			
 
            
			
			//End
			
			
		  }
		  }
		
		//Saving all results in excel file
		int rowCount = sheet.getLastRowNum();
	
	
	
	//Iterate over data and write to sheet
	      Set<String> keyid = data11.keySet();
	      
	      
	      for (String key : keyid) {
	         Row row = sheet.createRow(++rowCount);
	        Object [] objectArr = data11.get(key);
	         int cellid = 0;
	         
	         for (Object obj : objectArr){
	            Cell cell = row.createCell(cellid++);
	            cell.setCellValue((String)obj);
	           
	         }
	      }
	inputStream.close();
	FileOutputStream outputStream = new FileOutputStream(excelFilePath);
    workbook.write(outputStream);
    workbook.close();
    outputStream.close();
		
	
	}
	
	
	@Test
	public void testQuery12() throws EncryptedDocumentException, IOException //AtSafeDistance
	{
		ClassDiagramTestData.getInstance().reset(umlFile);
		
		String query = "context UAV inv: (self.currentDistanceToObstacle>self.mission.safeDistanceFromObstacle)"
				+ "implies (self.targetState='FlyToWaypoint')";
		/*String query = "UAV.allInstances()->forAll((self.currentDistanceToObstacle>=self.mission.safeDistanceFromObstacle) implies (self.targetState='FlyToWaypoint'))";*/
		
		if (ClassDiagramTestData.instanceExists()) {
			ClassDiagramTestData.getInstance().reset(umlFile);
		}
		ClassDiagramTestData cdtd = ClassDiagramTestData.getInstance();
		
		String ObjectPath = "formationFlightInstances/Arducopter2/AtSafeDistance";
		
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	    Workbook workbook = WorkbookFactory.create(inputStream);

	    Sheet sheet = workbook.getSheetAt(0);
	    TreeMap<String, Object[]> data12 = new TreeMap<String, Object[]>(); //MissionNumber //transition //constraint //Result
	    data12.put("0", new Object[]{"Constraint",query});
	    data12.put("1", new Object[]{ "FileName", "Result" });
		
		//cdtd.initClassesAndEOCL();
		//int instanceCount =  new File("/formationFlightInstances/Arducopter1/SafeDistanceViolation").listFiles().length;
		File folder = new File(ObjectPath);
		File[] listOfFiles = folder.listFiles(); 
		    
		  
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			 
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    ClassDiagramTestData.getInstance().reset(umlFile);
		    ClassDiagramTestData cdtd1 = ClassDiagramTestData.getInstance();
		    
		    cdtd1.initClassesAndEOCL(ObjectPath+"/"+listOfFiles[i].getName());
			
			
			ArrayList<ClassifierTuple> ObjectForConstraintEvaluation = cdtd1.getClassifiers();
			this.printResultTuple(ObjectForConstraintEvaluation);		
			
			//ArrayList<ClassifierTuple> result = test(1,1000, query, SearchAlgorithmEnum.AVM, umlFile,path);
			
			String str = this.verifyResult(query,ObjectForConstraintEvaluation);
			
			//Writing in excel file
			
		     
		     data12.put(Integer.toString(i+2), new Object[]{ listOfFiles[i].getName(), str });
				
			//bookData = new String[]{"FileName", "Constraint", "Result"};
			
			
			System.out.println("Result: " + str);
 
            
			
			//End
			
			
		  }
		  }
		
		//Saving all results in excel file
			int rowCount = sheet.getLastRowNum();
		
		
		
		//Iterate over data and write to sheet
		      Set<String> keyid = data12.keySet();
		      
		      
		      for (String key : keyid) {
		         Row row = sheet.createRow(++rowCount);
		        Object [] objectArr = data12.get(key);
		         int cellid = 0;
		         
		         for (Object obj : objectArr){
		            Cell cell = row.createCell(cellid++);
		            cell.setCellValue((String)obj);
		           
		         }
		      }
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(excelFilePath);
	    workbook.write(outputStream);
	    workbook.close();
	    outputStream.close();
	    //Done Saving
	
	}

}
