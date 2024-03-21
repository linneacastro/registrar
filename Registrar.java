// NAME: Linnea P. Castro
// DATE: 24 April 2023
// COURSE: CSE 223
// ASSIGNMENT #: PA4

// PA4 A Registrar Class Written in Java 

// PROGRAM SYNOPSIS:
/*
The Registrar Class provides a way to read in and organize student and enrollment records.  This class highlights the use of built
in methods to store and extract data, and makes use of Java's Abstract Data Types, HashMap and LinkedList especially, relying on 
the methods these data types offer, rather than hard coding nodes, and constructing the inner workings of these structures.  

An outside program will use this Class to read in a specific file.  The file must contain very specific formatting, with an 
individual record occupying a single line:

S,SID,Name indicates a student record
E,Course,SID indicates an enrollment record

The presence of the "S" or "E" serves as a marker of which HashMap to store that record in: either the student HashMap, or the 
enrollment HashMap.  Entry into the Students HashMap is straightforward, with the SID being hashed as a Key and the student's name
as the associated value, and each SID entry being unique.  Data entry into the Enrollment hashmap is slightly more nuanced.  
With the very likely possibility that multiple students are enrolled in a single class, each course name's Key hashes to a linked list
which can contain several student SIDs. When a line of entry contains the "E" marker, we check to see if that course already exists
within the enrollment HashMap.  If it does, that line's SID is added to the LinkedList.  If that course is new, a new Key/Value pair
is created, with the course name as the Key, and a brand new LinkedList being it's Value.  

Methods can be used within a main program to obtain general information such as, how many students are stored in the students HashMap, or 
How many courses have any students enrolled in them? Methods can also be used to serve as a bridge to access information across HashMaps,
with SIDs in a given course's LinkedList then being used to search inside the students HashMap to match that SID with an actual name.  
Detailed information on each method can be found below.

Skills practiced:
- Fully using built in methods of the HashMap and LinkedIn classes to access information like, size, toArray, get, put, and add, depending
on use with HashMap or LinkedList.  Getting clear on what I'm searching for or trying to access, and then searching Java documentation to
see if there is a method for that, and then understanding how to use these methods/return types/etc. to get what I was looking for.

- Understanding scope and the importance of declaring variables outside specific methods if you want to be able to access them more 
flexibly by methods.  Also, understanding exactly what to put inside the constructor (not the LinkedList in this case), and realizing
why.  Even though the ADT nature of Java precludes me from having to know intricate details of structures, I still very much have to understand
what is going on and why.

- Practicing mapping out what I want to happen before coding to make sure that I understand the desired behavior of the program.  For me,
this meant drawing diagrams on paper and knowing what conditions I would need to have to make sure I could enter incoming data in as 
asked in the assignment instructions.  Most important for ingest method, but also in general.
*/

import java.util.*; // Import libraries to access Scanner, HashMap, and LinkedList
import java.io.*; 

// CLASS: Registrar - The Registrar class contains a constructor method and seven additional methods to access information within the two
// HashMaps the constructor creates.  The purpose of the class is to create a way to manage student records and enrollment by differentiating
// between the two record types, storing the given information, and then provide ways to not only access the information, but make connections
// from one HashMap to the another using the SID field as a link between which students are enrolled in a given class, and their actual name,
// which is not stored in the enrollment record itself. 

public class Registrar{ // Class is called Registrar, first letter is capitalized
  
  // VARIABLES DECLARED OUTSIDE REGISTRAR CONSTRUCTOR BUT INSIDE CLASS
  private HashMap<String,String> students; // students hashmap and parameters.  All variables inside class, but outside methods, declared private.
  private String SID; // Variable to store SID as students hashmap Key
                      // Also using same variable to store SID inside LinkedList for enrollment record
                      // Works for processing one line at a time, but if two lines were being hashed at once, I'd have to change this
  private String studentName; // Variable to store student name as Value associated with SID Key in students HashMap

  private HashMap<String,LinkedList<String>> enrollment; // enrollment hashmap and parameters
  private String courseName; // Variable to store courseName as Key inside enrollment HashMap

  private LinkedList<String> listOfSIDs; // listsOfSIDs LinkedList and parameters, will be associated as Value inside enrollment HashMap, one
                                         // LinkedList will be created for each courseName entered into the HashMap as Key

  private String studentRecordIdentifier="S"; // Declaring String literal variable to differentiate student record from enrollment record
  private String enrollmentRecordIdentifier="E"; // Enrollment record identifier
  
  // REGISTRAR CONSTRUCTOR METHOD - The purpose of this Method is to create two new HashMaps, one for holding student records and one for
  // holding enrollment records.  The students HashMap contains a String as its Key and another String as its Value.  No matter how many invididual
  // lines of student or enrollment records are processed in the data file ingested, these two HashMaps will only be created once.

  public Registrar(){ 
    students=new HashMap<String,String>(); // Create a HashMap called students w SID and studentName pairs, initialize variables OUTSIDE constructor 
    enrollment=new HashMap<String,LinkedList<String>>(); // Create a HashMap called enrollment w courseName and LinkedList
  }

  // INGEST METHOD - The ingest method is crucial to processing lines of incoming records, differentiating between student records and 
  // enrollment records and then ensuring proper storate of this information depending on record type.  Ingest relies on a Scanner 
  // positioned within a try/catch block to verify that the incoming File exists, if a FileNotFoundException is not thrown, the method 
  // enters a while loop whereby each line of the File is processed, and continues for as long as there is a next line.  Each line is
  // broken into comma separated fields, with an S or E in the first field indicating whethere the rest of the line contains a student or
  // enrollment record.  Student records are processed in a straightforward manner with the second field becoming the HashMap Key and the 
  // third field being stored in the Value location associated with preceding Key. 

  // An E indicates the presence of an enrollment record.  The processing of enrollment records is more nuanced than that of the student 
  // records, because of the creation and presence of the LinkedList.  The method must first check to see if the incoming courseName in 
  // the second field exists.  If the courseName doesn't already exist as a Key in the enrollment HashMap, that entry must be created, 
  // together with the listOfSIDs LinkedList, which must then be populated with the SID contained in the third field of the comma separated line.   
  // If the courseName already exists as a Key within the enrollment HashMap, then so does its LinkedList, listOfSIDs, so the only job then is 
  // go to the Value spot represented by that courseName/Key and add the SID into the LinkedList.  When all lines are exhausted, the file is
  // closed and the method returns true to the main program.

  public boolean ingest(String filename){
    Scanner sc;  // Create Scanner
    
    try{ // Beginning of try/catch block
      sc=new Scanner(new File(filename)); // Try to create a Scanner to read a file passed in by a main program
    }
    catch (FileNotFoundException e){ // If file is not found, the catch block will throw an error and return false to main program
      return(false);
    }   
 
    while (sc.hasNextLine()){ // See if the line in the file has a line after it
      String oneLineOfRecord=sc.nextLine(); // Each line is a String called oneLineOfRecord
      String[] arrayOfWordsOnLine=oneLineOfRecord.split(","); // Break that oneLineOfRecord into fields separated by the comma
                                                              // Store each field in an array called arrayOfWordsOnLine
        
      if (arrayOfWordsOnLine[0].equals(studentRecordIdentifier)){ // If 1st field matches "S", it is a student record, store it in students HashMap
        SID=arrayOfWordsOnLine[1]; // 2nd field after "S" is SID
        studentName=arrayOfWordsOnLine[2]; // 3rd field is studentName
        students.put(SID,studentName); // Populate students HashMap w SID as Key and studentName as Value, continue looping
      }

      if (arrayOfWordsOnLine[0].equals(enrollmentRecordIdentifier)){ // If 1st field matches "E", it is an enrollment record
        courseName=arrayOfWordsOnLine[1]; // 2nd field after "E" is courseName
        SID=arrayOfWordsOnLine[2]; // 3rd field is SID

        if (enrollment.containsKey(courseName)){ // Checking to see if courseName is already in enrollment HashMap, this returns boolean, if true...
          enrollment.get(courseName).add(SID); // Go to courseName key spot in HashMap, add SID to value, which is LinkedList called listOfSIDs
        }

        else{ // Above if conditional check for courseName returned FALSE, courseName is not in enrollment HashMap, we want to add it and must create LinkedList 
          listOfSIDs=new LinkedList<String>(); // Create the new LinkedList
          listOfSIDs.addFirst(SID); // Add SID as first item in LinkedList called listOfSIDs
          enrollment.put(courseName, listOfSIDs); // Add this key/value pair to HashMap and LinkedList
        }
      }
    } // end of while loop

    sc.close(); // Close file once all data has been read
    return(true); // File loaded in with no errors, return true to main program
  } // end ingest method   

  // NUMBER OF STUDENTS METHOD - This method returns the number of students in the students HashMap as an int.  It uses the size method to find the size 
  // of the students HashMap and stores that as a variable which is returned to the main program.  The size of the HashMap will correspond to the number of 
  // students, with one entry per student record. 
 
  public int numberOfStudents(){ // Takes no arguments, returns int
    int numberOfStudents; // Variable to hold size of students HashMap
    numberOfStudents=students.size(); // Using built in size method to assign number of students to the variable numberOfStudents
    return(numberOfStudents); // Return numberOfStudents int to main program
  }

  // NUMBER OF COURSES METHOD - This method is very similar to the number of students method above in that it uses the size method to find out how many
  // courses exist.  A new HashMap entry was created each time a newCourse was presented, so the number returned by this method will correspond to the
  // total classes that have students enrolled.

  public int numberOfCourses(){ // Takes no arguments, returns int
    int numberOfCourses; // Variable to hold size of enrollment HashMap
    numberOfCourses=enrollment.size(); // Using built in size method to assign number of HashMap keys to numberOfCourses int variable
    return(numberOfCourses); // Returns numberOfCourses int to main program
  }

  // GET ARRAY OF ALL STUDENT SIDs METHOD - This method delivers an array of all the students inside the students HashMap.  It differs in signature
  // from the getStudents method which takes in a course as an argument and returns only students in a given course.  This method uses the keySet 
  // method in the HashMap class to bundle up all Keys into a set.  toArray is then used to turn this set into an array of Strings, with each 
  // individual students being associated with one element of the array. 

  public String[] getStudents(){ // Returns array of String, takes in no argument
    String[] arrayOfAllSIDs; // Variable created for String array the method will return
    arrayOfAllSIDs=students.keySet().toArray(new String[0]); // Using keySet to make a set of all Keys in students HashMap.  toArray then turns all 
                                                          // elements of set into an Array, with first name becoming element [0] in array, etc.
    return(arrayOfAllSIDs); // Array created above is returned to the main program
  }

  // GET ARRAY OF ALL STUDENTS (SIDS) ENROLLED IN GIVEN COURSE METHOD - This method will be used by the main program with a specific course name
  // as its argument to return an array of all students enrolled in that course.  If no course is found as a Key in the enrollment HashMap, the 
  // method returns null.  This method differes from the above method not only in signature, but in content, as this method searches the enrollment
  // HashMap and returns all contents of the LinkedList associated with that Key/course.  

  public String[] getStudents(String course){ // Specific course entered as argument, differentiating this method from getStudents method above
    boolean doesCourseExist; // Will evaluate true or false when course is queried in enrollment HashMap  
    String[] arrayOfSIDsInCourse;  // Array to store all SIDs enrolled in course
    doesCourseExist=enrollment.containsKey(course); // Search enrollment Keys for given course, will evaluate true/false

    if(doesCourseExist=true){ // Course was found, above search evaluated true
      arrayOfSIDsInCourse=enrollment.get(course).toArray(new String[0]); // enrollment.get(course) is the LinkedList associated with this course.
                                                                         // Use toArray to bundle up all contents of the LinkedList into an array 
      return(arrayOfSIDsInCourse); // Return that array (everything stored in that LinkedList) to main program
    }

    else{ // Course wasn't found in enrollment HashMap
      return(null); // Return null
    }
  }

  // GET STUDENT NAME WITH THIS SID METHOD - This method takes in an SID as an argument, searches for the given SID within the students HashMap, 
  // and returns the name of the student (the Value that SID Key maps to) to the main program. In this, information from the enrollment HashMap
  // in the form of SIDs can be mapped over to a name in the students HashMap, an entirely separate structure.  

  public String getStudentName(String SID){
    String searchForThisSID=SID; // Creating new variable name for passed in SID... debugging
    boolean doesStudentExist; // Will evaluate true/false when an SID is queried in students HashMap
    String studentName; // Variable for student name, will become return value
    doesStudentExist=students.containsKey(searchForThisSID); // Did you find this SID inside the students HashMap, true/false?
  
    if(doesStudentExist=true){ // SID was found
      studentName=students.get(searchForThisSID); // Value associated with that SID becomes StudentName
      return(studentName); // Return that name to main program
    }

    else{ // SID not found
      studentName=""; // studentName becomes a zero length String 
      return(studentName); // Return that zero length String to main program
    } 
  }

  // GET ARRAY OF ALL COURSES W STUDENTS ENROLLED METHOD - This method uses keySet and toArray methods within the HashMap class to gather all
  // courses (Keys) within the enrollment HashMap, and returns the array to the main program.  If no courses exist, the array length will be 0.

  public String[] getCourses(){ // Returns array of String
    String[] arrayOfCourses; // Variable to hold array of courses
    arrayOfCourses=enrollment.keySet().toArray(new String[0]); // Use keySet and toArray to create array of all Keys in hashmap, as they represent courses
    return(arrayOfCourses); // Return that array to main program
  }


} // End Registrar class


