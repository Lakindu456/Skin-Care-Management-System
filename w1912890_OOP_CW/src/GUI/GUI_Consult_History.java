package GUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class GUI_Consult_History extends JFrame {
    //Initializing the JFrame components
    JLabel doctorDisplay, dateDisplay, startTimeDisplay, endTimeDisplay, costDisplay, patientIdDisplay, firstNameDisplay, lastNameDisplay, dobDisplay, mobNoDisplay, nicDisplay, notesDisplay, imageDisplay;
    JButton goBackButton, consultButton, searchButton;
    JComboBox<String> consultationList;
    Border blackLine = BorderFactory.createLineBorder(Color.black);

    //Initializing arrays
    String[] consultations;
    String[][] data = tableData();

    //Creating objects
    GUI_Menu menuObj = new GUI_Menu();
    Encrypt_Decrypt decryptObj = new Encrypt_Decrypt();

    public GUI_Consult_History(){
        //Initialize an event handler
        buttonHandler handler = new buttonHandler();
        this.setLayout(null);
        this.setTitle("Consultation History");

        //Adding the Back to Menu button
        goBackButton = new JButton("Back to Menu");
        addButton(goBackButton,handler,100,600,120,40,Color.red,Color.decode("#FFB7B7"));
        menuObj.btnHoverEffect(goBackButton, Color.red, Color.decode("#FFA6A6"), Color.red);

        //Adding the Consult a Doctor button
        consultButton = new JButton("Consult a Doctor");
        addButton(consultButton,handler,1175, 600, 150, 40,Color.blue,Color.decode("#E4EEFF"));
        menuObj.btnHoverEffect(consultButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);

        //Checking whether there are any consultation history in the center or not
        if (data[0][0] == null){
            addLabel("Oops...Currently there is no consultation history in the center!",510,430,500,50,"Chalkboard", 0,16);
            addImage("notFound.jpg",470, 30, 600, 500);
        }else {
            addLabel("Consultation History",610, 30, 400,50,"SansSerif",1,22);

            int noOfConsultations = data.length;
            consultations = populateComboBox(noOfConsultations);

            addLabel("Select a Consultation:",450,122,200,50,"SansSerif",1,15);

            consultationList = new JComboBox<>(consultations);
            consultationList.setBounds(650, 100, 200, 100);
            this.add(consultationList);

            searchButton = new JButton("Search");
            addButton(searchButton,handler,870, 135, 75, 25,Color.decode("#56D400"),Color.decode("#D8FFBD"));
            menuObj.btnHoverEffect(searchButton, Color.decode("#4CBA00"), Color.decode("#C4FF9B"), Color.green);

            //Adding JLabels
            addLabel("Doctor Name:",100,220,200,100,"SansSerif", 1,14);
            doctorDisplay = new JLabel("");
            addDisplayLabel(doctorDisplay,250,220);

            addLabel("Date:",100,270,200,100,"SansSerif",1,14);
            dateDisplay = new JLabel();
            addDisplayLabel(dateDisplay,250,270);

            addLabel("Start Time:",100,320,200,100,"SansSerif",1,14);
            startTimeDisplay = new JLabel();
            addDisplayLabel(startTimeDisplay,250,320);

            addLabel("End Time:",100,370,200,100,"SansSerif",1,14);
            endTimeDisplay = new JLabel();
            addDisplayLabel(endTimeDisplay,250,370);

            addLabel("Cost:",100,420,200,100,"SansSerif",1,14);
            costDisplay = new JLabel();
            addDisplayLabel(costDisplay,250,420);

            addLabel("Patient ID:",700,200,200,100,"SansSerif",1,14);
            patientIdDisplay = new JLabel();
            addDisplayLabel(patientIdDisplay,840,200);

            addLabel("First Name:",700,250,200,100,"SansSerif",1,14);
            firstNameDisplay = new JLabel();
            addDisplayLabel(firstNameDisplay,840,250);

            addLabel("Last Name:",700,300,200,100,"SansSerif",1,14);
            lastNameDisplay = new JLabel();
            addDisplayLabel(lastNameDisplay,840,300);

            addLabel("Date of Birth:",700,350,200,100,"SansSerif",1,14);
            dobDisplay = new JLabel();
            addDisplayLabel(dobDisplay,840,350);

            addLabel("Mobile Number:",700,400,200,100,"SansSerif",1,14);
            mobNoDisplay = new JLabel();
            addDisplayLabel(mobNoDisplay,840,400);

            addLabel("NIC:",700,450,200,100,"SansSerif",1,14);
            nicDisplay = new JLabel();
            addDisplayLabel(nicDisplay,840,450);

            addLabel("Notes:",1050,200,200,100,"SansSerif",1,14);
            notesDisplay = new JLabel();
            addDisplayLabel(notesDisplay,1130,200);

            addLabel("Images:",1050,250, 200,100,"SansSerif",1,14);
            imageDisplay = new JLabel();
            imageDisplay.setBounds(1130,325,200,200);
            this.add(imageDisplay);

            addHeading(40, 200, 500, 350,"CONSULTATION INFORMATION");
            addHeading(650, 200, 750, 350,"PATIENT INFORMATION");
        }

        //Adding images
        addImage("logo.png",50, 0, 200, 100);
        addImage("background.jpg",0, -20, 1440, 500);
        addImage("background.jpg",440, -20, 1440, 500);
        addImage("background.jpg",840, -20, 1440, 500);
        addImage("background.jpg",0,200,1440,500);
        addImage("background.jpg",440,200,1440,500);
        addImage("background.jpg",840,200,1440,500);
    }

    private class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton actionSource = (JButton) evt.getSource();

            if (actionSource.equals(goBackButton)) {
                GUI_Menu menu = new GUI_Menu();
                menu.guiMenu();
                dispose();
            } else if (actionSource.equals(consultButton)) {
                GUI_Consult consult = new GUI_Consult();
                consult.guiConsult();
                dispose();
            } else if (actionSource.equals(searchButton)) {
                populateLabels();
            }
        }
    }

    //These methods are to add and place the JLabels in the JFrame
    public void addLabel(String text, int x, int y, int width, int height, String fontName, int font, int fontSize){
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(new Font(fontName, font, fontSize));
        this.add(label);
    }

    public void addDisplayLabel(JLabel label, int x, int y){
        label.setBounds(x, y, 200, 100);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(Color.blue);
        this.add(label);
    }

    public void addHeading(int x, int y, int width, int height, String text){
        JLabel label = new JLabel();
        label.setBounds(x,y,width,height);
        label.setOpaque(true);
        TitledBorder title = BorderFactory.createTitledBorder(blackLine, text);
        title.setTitleJustification(TitledBorder.CENTER);
        label.setBorder(title);
        label.setBackground(Color.decode("#E4EEFF"));
        this.add(label);
    }

    //This method is to add and place the images in the JFrame
    public void addImage(String fileName, int x, int y, int width, int height){
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(fileName));
        label.setBounds(x, y, width, height);
        this.add(label);
    }

    //This method is to add and place the JButtons in the JFrame
    public void addButton(JButton btn, buttonHandler handler, int x, int y, int width, int height, Color borderClr, Color bgClr){
        btn.addActionListener(handler);
        btn.setBounds(x, y, width, height);
        btn.setBorder(BorderFactory.createLineBorder(borderClr));
        btn.setBackground(bgClr);
        btn.setOpaque(true);
        this.add(btn);
    }

    //This method counts the number of lines in the consultation text file
    public int lineCount(){
        int count = 0;
        try {
            File file = new File("GUI_Consultation_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            while (read_File.hasNext()){
                read_File.nextLine();
                count++;
            }
            return count;
        } catch (FileNotFoundException e) {
            return count;
        }
    }

    //This method loads the consultations from the consultation text file
    public String[][] tableData(){
        int lineCount = lineCount();
        String[][] data = new String[10][11];
        if (lineCount != 0) {
            data = new String[lineCount/11][11];
        }
        try {
            File file = new File("GUI_Consultation_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.

            int array = 0;
            while (read_File.hasNext()){
                int subArray = 0;
                for (int i=0; i<11; i++){
                    data[array][subArray + i] = read_File.nextLine();
                }
                array++;
            }
            return data;
        } catch (FileNotFoundException e) {
            return data;
        }
    }

    //This method populates the combo-box with the consultation ID
    public String[] populateComboBox(int noOfConsultations){
        String[] arr = new String[noOfConsultations];
        for (int i=0; i < noOfConsultations; i++){
            arr[i] = "Consultation " + (i+1);
        }
        return arr;
    }

    //This method sets the consultation information into the specific labels according to the consultation ID
    public void populateLabels(){
        String selectedItem = (String) consultationList.getSelectedItem();
        assert selectedItem != null;
        int index = Integer.parseInt(selectedItem.replaceAll("\\D", ""))-1;

        doctorDisplay.setText(data[index][0]);
        String[] dateTime = data[index][1].split("T");
        dateDisplay.setText(dateTime[0]);
        startTimeDisplay.setText(dateTime[1]);
        dateTime = data[index][2].split("T");
        endTimeDisplay.setText(dateTime[1]);
        costDisplay.setText("£" + data[index][3]);
        patientIdDisplay.setText(data[index][5]);
        firstNameDisplay.setText(data[index][6]);
        lastNameDisplay.setText(data[index][7]);
        dobDisplay.setText(data[index][8]);
        mobNoDisplay.setText(data[index][9]);
        nicDisplay.setText(data[index][10]);
        String imgPath = "/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Images/" + (index+1) + ".png";
        String txtFilePath = "/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Texts/" + (index+1) + ".txt";
        try {
            //Decrypt the image and the textual note
            decryptObj.main(imgPath);
            decryptObj.main(txtFilePath);
        } catch (IOException ignored) {}

        try {
            String text = Files.readString(Paths.get(txtFilePath));
            notesDisplay.setText(text);
        } catch (IOException ignored) {}

        imageDisplay.setIcon(new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT)));

        try {
            //Again encrypt the image and the textual note
            decryptObj.main(imgPath);
            decryptObj.main(txtFilePath);
        } catch (IOException ignored) {}
    }

    public void consultHistory(){
        GUI_Consult_History history = new GUI_Consult_History();
        history.setVisible(true);
        history.setSize(1440,710);
        history.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
