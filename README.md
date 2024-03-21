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
