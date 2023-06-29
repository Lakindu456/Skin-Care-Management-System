package Console;

import GUI.GUI_Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class WestminsterSkinConsultationManager implements SkinConsultationManager {

    //Method to display the console menu
    public String displayMenu(){
        return """
                \n----------Westminster Skin Consultation Manager----------
                Menu
                \t1. Add a new doctor
                \t2. Delete a doctor
                \t3. Display doctors
                \t4. Save the doctors in a file
                \t5. Load the doctors
                \t6. Open in GUI
                \t7. Exit
                """;
    }

    //Defining an arraylist to store all the doctor objects
    ArrayList<Doctor> doctorObjList = new ArrayList<>();

    //Method to add a new doctor
    public String addDoctor(Scanner input){
        //Checking if there are a maximum of 10 doctors
        if (doctorObjList.size() == 10){
            return "NOTE:\n\tThere are now a total of 10 doctors in the center, which is the maximum allowed.\n";
        }
        System.out.println("At any time, type \"X\" to return to the menu.\n");
        Doctor doctorObj = new Doctor();

        //Getting and validating the first name of the doctor
        while (true) {
            System.out.print("Enter the first name: ");
            String fName = input.nextLine();
            fName = nameValidate(fName);
            if (fName.equalsIgnoreCase("Redirecting back to the menu...")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (fName.equalsIgnoreCase("Invalid")){
                System.out.println("NOTE:\n\tThe First Name can only contain LETTERS!\n\tPlease Try Again!\n");
                continue;
            }
            doctorObj.setfName(fName);
            break;
        }

        //Getting and validating the last name of the doctor
        while (true) {
            System.out.print("Enter the last name: ");
            String lName = input.nextLine();
            lName = nameValidate(lName);
            if (lName.equalsIgnoreCase("Redirecting back to the menu...")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (lName.equalsIgnoreCase("Invalid")){
                System.out.println("NOTE:\n\tThe Last Name can only contain LETTERS!\n\tPlease Try Again!\n");
                continue;
            }
            doctorObj.setlName(lName);
            break;
        }

        //Getting and validating the DOB of the doctor
        while (true) {
            System.out.print("Enter the date of birth (yyyy-mm-dd): ");
            String dob = input.nextLine();
            dob = dobValidate(dob);
            if (dob.equalsIgnoreCase("Redirecting back to the menu...")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (dob.equalsIgnoreCase("Invalid Age")){
                System.out.println("NOTE\n\tThe age of the doctor must be in between 21 and 60\n\tPlease Try Again!\n");
                continue;
            }
            if (dob.equalsIgnoreCase("Invalid Format")){
                System.out.println("ERROR:\n\tInvalid Date!\n\tPlease Try Again!\n");
                continue;
            }
            LocalDate date = LocalDate.parse(dob);
            doctorObj.setDob(date);
            break;
        }

        //Getting and validating the mobile number of the doctor
        while (true) {
            System.out.print("Enter the mobile number: ");
            String mobileNo = input.nextLine();
            mobileNo = mobileNoValidate(mobileNo);
            if (mobileNo.equalsIgnoreCase("Redirecting back to the menu...")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (mobileNo.equalsIgnoreCase("Invalid Input")) {
                System.out.println("NOTE:\n\tThe mobile number can only contain NUMBERS!\n\tPlease Try Again!\n");
                continue;
            }
            if (mobileNo.equalsIgnoreCase("Invalid Length")){
                System.out.println("NOTE:\n\tThe length of the mobile number must be 10!\n\tPlease Try Again!\n");
                continue;
            }
            doctorObj.setMobileNo(mobileNo);
            break;
        }

        //Getting and validating the medical license number of the doctor
        main:
        while (true) {
            System.out.print("Enter the medical license number: ");
            String medLiceNo = input.nextLine();
            medLiceNo = medLiceNoValidate(medLiceNo);
            if (medLiceNo.equalsIgnoreCase("Redirecting back to the menu...")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (medLiceNo.equalsIgnoreCase("Invalid")) {
                System.out.println("NOTE:\n\tThe medical license number can only contain NUMBERS!\n\tPlease Try Again!\n");
                continue;
            }
            for (Doctor obj: doctorObjList){
                if (obj.getMedLiceNo().equalsIgnoreCase(medLiceNo)){
                    System.out.println("NOTE:\n\tThere cannot be doctors with the same medical licence number!\n\tPlease try Again!\n");
                    continue main;
                }
            }
            doctorObj.setMedLiceNo(medLiceNo);
            break;
        }

        //Getting and validating the specialised area of the doctor
        while (true) {
            System.out.print("Enter the specialised area: ");
            String specialised = input.nextLine();
            specialised = specializationValidate(specialised);
            if (specialised.equalsIgnoreCase("Redirecting back to the menu...")) {    //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            if (specialised.equalsIgnoreCase("Invalid")) {
                System.out.println("NOTE:\n\tThe specialization field cannot be EMPTY!\n\tPlease Try Again!\n");
                continue;
            }
            doctorObj.setSpecialization(specialised);
            break;
        }

        //Calling the method to check if there are doctors with the same name
        checkSameNameDoctor(doctorObj);
        doctorObjList.add(doctorObj);
        return "NOTE:\n\t" + doctorObj.getfName() + " " + doctorObj.getlName() + " has been added to the center as a doctor.\n\tThere are " + doctorObjList.size() + " doctors in the center now.";
    }

    //This method validate the input of the first and last name
    public String nameValidate(String name){
        if (name.equalsIgnoreCase("X")) {
            return "Redirecting back to the menu...";
        }
        if ((characterValidate(name) > 0) || (name.equals(""))){
            return "Invalid";
        }
        return name;
    }

    //This method validate the input of the DOB
    public String dobValidate(String dob){
        try {
            if (dob.equalsIgnoreCase("X")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            LocalDate date = LocalDate.parse(dob);
            boolean validate = dateValidate(date);
            if (validate){
                return "Invalid Age";
            }
        }catch (Exception e){
            return "Invalid Format";
        }
        return dob;
    }

    //This method validate the input of the mobile number
    public String mobileNoValidate(String mobileNo){
        if (mobileNo.equalsIgnoreCase("X")){   //Checking if the user wants to return to the menu
            return "Redirecting back to the menu...";
        }
        if (characterValidate(mobileNo) != mobileNo.length()) {
            return "Invalid Input";
        }
        if (mobileNo.length() != 10){
            return "Invalid Length";
        }
        return mobileNo;
    }

    //This method validate the input of the medical license number
    public String medLiceNoValidate(String medLiceNo){
        if (medLiceNo.equalsIgnoreCase("X")){   //Checking if the user wants to return to the menu
            return "Redirecting back to the menu...";
        }
        if ((characterValidate(medLiceNo) != medLiceNo.length()) || (medLiceNo.equals(""))) {
            return "Invalid";
        }
        return medLiceNo;
    }

    //This method validate the input of the specialization
    public String specializationValidate(String specialised){
        if (specialised.equalsIgnoreCase("X")) {    //Checking if the user wants to return to the menu
            return "Redirecting back to the menu...";
        }
        if (specialised.equals("")) {
            return "Invalid";
        }
        return specialised;
    }

    //This method checks how many doctors are there with the same name
    public void checkSameNameDoctor(Doctor doctorObj){
        int count = 0;
        for (Doctor obj: doctorObjList){
            String lName = obj.getlName().replaceAll("[^a-zA-Z]", "");
            if (obj.getfName().equalsIgnoreCase(doctorObj.getfName()) && lName.equalsIgnoreCase(doctorObj.getlName())){
                count++;
            }
        }
        if (count > 0){
            doctorObj.setlName(doctorObj.getlName() + " " + (count+1));
        }
    }

    //This method counts the number of non-alphabetical characters that the user enters
    public int characterValidate(String str){
        int nonAlphaCount = 0, numCount = 0;
        char[] charArray = str.toCharArray();
        for (char i : charArray){
            if (!Character.isAlphabetic(i)){
                nonAlphaCount++;
            }
            if (Character.isDigit(i)){
                numCount++;
            }
        }
        if (nonAlphaCount == numCount){
            return nonAlphaCount;
        }
        return (nonAlphaCount - numCount)+1;
    }

    //This method validates the date of birth
    public boolean dateValidate(LocalDate date){
        return !((date.isAfter(LocalDate.parse("1962-12-31"))) && (date.isBefore(LocalDate.parse("2002-01-01"))));
    }

    //Delete a doctor
    public String deleteDoctor(Scanner input){
        if (doctorObjList.size() == 0){
            return "NOTE:\n\tCurrently there are no doctors in the center.";
        }
        System.out.println("At any time, type \"X\" to return to the menu.\n");
        while (true) {
            //Displaying the medical licence numbers
            int count = 0;
            System.out.println("Medical Licence Numbers of the current doctors in the center");
            for (Doctor doctorObj: doctorObjList){
                count++;
                System.out.println("\tDoctor " + count + ": " + doctorObj.getMedLiceNo());
            }

            System.out.print("\nEnter the medical licence number to delete a doctor: ");
            String medLiceNo = input.nextLine();
            if (medLiceNo.equalsIgnoreCase("X")){   //Checking if the user wants to return to the menu
                return "Redirecting back to the menu...";
            }
            count = -1;
            for (Doctor doctorObj : doctorObjList) {
                count++;
                if (doctorObj.getMedLiceNo().equalsIgnoreCase(medLiceNo)) {
                    System.out.println("The doctor with the medical licence " + doctorObj.getMedLiceNo() + ", has been removed successfully!");
                    System.out.println("\nRemoved Doctor Information");
                    doctorInformation(doctorObj);
                    doctorObjList.remove(count);
                    return "\nThe remaining number of doctors in the center: " + doctorObjList.size();
                }
            }
            System.out.println("ERROR:\n\tThere's no matching!\n\tPlease Try Again!\n");
        }
    }

    //Display the list of the doctors
    public void displayDoctor(){
        if (doctorObjList.size() == 0){
            System.out.println("NOTE\n\tCurrently there are no doctors in the center.");
        }else {
            String[][] copy = new String[doctorObjList.size()][2];
            int count = -1;
            for (Doctor doctorObj : doctorObjList) {
                count++;
                copy[count] = new String[]{doctorObj.getlName().toLowerCase(), String.valueOf(count)};
            }
            //Using lambda expressions, sort by surname was done by referring https://www.folkstalk.com/tech/sort-2d-array-based-on-one-column-java-with-code-examples/.
            Arrays.sort(copy, (a, b) -> CharSequence.compare(a[0], b[0]));

            for (int i = 0; i < doctorObjList.size(); i++) {
                Doctor obj = doctorObjList.get(Integer.parseInt(copy[i][1]));
                System.out.println("\nDoctor " + (i + 1) + " Information");
                doctorInformation(obj);
            }
        }
    }

    //The format to display the information of the doctors
    public void doctorInformation(Doctor doctorObj){
        System.out.println("\tFirst name         : " + doctorObj.getfName());
        System.out.println("\tLast name          : " + doctorObj.getlName());
        System.out.println("\tDate of birth      : " + doctorObj.getDob());
        System.out.println("\tMobile no          : " + doctorObj.getMobileNo());
        System.out.println("\tMedical Licence No : " + doctorObj.getMedLiceNo());
        System.out.println("\tSpecialised area   : " + doctorObj.getSpecialization());
    }

    //Save in a file
    public void saveDoctor(String fileName){
        try(FileWriter file = new FileWriter(fileName)) {   //Creates a text file to store data.
            for (Doctor obj : doctorObjList) {
                file.write(obj.getfName() + "\n");
                file.write(obj.getlName() + "\n");
                file.write(obj.getDob().toString() + "\n");
                file.write(obj.getMobileNo() + "\n");
                file.write(obj.getMedLiceNo() + "\n");
                file.write(obj.getSpecialization() + "\n");
            }
        }catch (Exception e){
            System.out.println("ERROR:\n\t" + e);
        }
        System.out.println("NOTE:\n\tThe data has been saved successfully!");
    }

    //Load the file
    public void loadDoctor(){
        try {
            File file = new File("Doctor_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            doctorObjList.clear();
            while (read_File.hasNext()){
                Doctor doctorObj = new Doctor();
                doctorObj.setfName(read_File.nextLine());
                doctorObj.setlName(read_File.nextLine());
                doctorObj.setDob(LocalDate.parse(read_File.nextLine()));
                doctorObj.setMobileNo(read_File.nextLine());
                doctorObj.setMedLiceNo(read_File.nextLine());
                doctorObj.setSpecialization(read_File.nextLine());
                doctorObjList.add(doctorObj);
            }
            System.out.println("NOTE\n\tThe data has been loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR:\n\tThe text file cannot be found.");
        }
    }

    //Main method
    public static void main(String[] args){
        //Creating an object for the WestminsterSkinConsultationManager class
        WestminsterSkinConsultationManager managerObj = new WestminsterSkinConsultationManager();

        //Creating a scanner object to get inputs
        Scanner input = new Scanner(System.in);

        mainLoop:
        while (true){
            //Displaying the menu
            System.out.println(managerObj.displayMenu());

            //Getting user input to choose a menu option
            System.out.print("Select the menu number: ");
            String optionNum = input.nextLine();

            //Checking the user choice
            switch (optionNum){
                case "1":
                    //Add a new doctor
                    System.out.println("\n----------Add a new doctor----------");
                    System.out.println(managerObj.addDoctor(input));
                    break;
                case "2":
                    //Delete a doctor
                    System.out.println("\n----------Delete a doctor----------");
                    System.out.println(managerObj.deleteDoctor(input));
                    break;
                case "3":
                    //Display doctors
                    System.out.println("\n----------Display the doctors----------");
                    managerObj.displayDoctor();
                    break;
                case "4":
                    //Save the doctors in a file
                    System.out.println("\n----------Save the doctors----------");
                    managerObj.saveDoctor("Doctor_Data.txt");
                    break;
                case "5":
                    //Load the doctors
                    System.out.println("\n----------Load the doctors----------");
                    managerObj.loadDoctor();
                    break;
                case "6":
                    //Open the GUI
                    managerObj.saveDoctor("GUI_Doctor_Data.txt");
                    System.out.println("\tOpening the GUI...");
                    GUI_Menu gui = new GUI_Menu();
                    gui.guiMenu();
                    break;
                case "7":
                    //Exit the program
                    System.out.println("End of the program!");
                    break mainLoop;
                default:
                    System.out.println("ERROR:\n\tOops...There's no such an option number.\n\tPlease Try Again!");
            }
        }
    }
}
