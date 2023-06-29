package GUI;
import Console.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class GUI_Consult extends JFrame {
    //JComponents of Consultation Window
    JLabel noDataLabel, noDataImage, consultationBgImage1, consultationBgImage2, consultHeadingLabel, dropLabel, dateLabel, startTimeLabel, endTimeLabel, consultationLogo, randomLabel;
    JComboBox<String> dropList;
    JTextField date, startTime, endTime;
    JButton checkAvailabilityButton, consultGoBackButton;

    //JComponents of Patient Window
    JLabel patientBgImage1, patientBgImage2, patientHeadingLabel, fNameLabel, lNameLabel, dobLabel, mobileNoLabel, nicLabel, notesLabel, addImgLabel, tickLabel, patientLogo;
    JTextField fNameTextField, lNameTextField, dobTextField, mobileNoTextField, nicTextField;
    JButton addImgButton, patientGoBackButton, confirmButton;
    JTextArea notesTextArea;
    JScrollPane scrollPane;

    //Others
    double cost;
    int uniqueID = 0;
    String doctorFullName;
    String[] doctorNames = fileData();
    int index = 0;
    int addImgCount = 0;
    Border blackLine = BorderFactory.createLineBorder(Color.black);

    //Create Objects
    GUI_Menu menuObj = new GUI_Menu();
    Encrypt_Decrypt encryptObj = new Encrypt_Decrypt();

    public GUI_Consult() {
        createDoctorList();
        loadConsultationDetails();
        this.setLayout(null);
        showConsultElements();
    }

    //This method counts the number of lines in the doctor text file
    public int lineCount(){
        int count = 0;
        try {
            File file = new File("GUI_Doctor_Data.txt");   //Opening the text file.
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

    //This method loads the doctors from the doctor text file
    public String[] fileData(){
        int lineCount = lineCount();
        String[] data = new String[10];
        if (lineCount != 0){
            data = new String[lineCount/6];
        }
        try {
            File file = new File("GUI_Doctor_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            int array = 0;
            while (read_File.hasNext()){
                String fName = read_File.nextLine();
                String lName = read_File.nextLine();
                data[array] = fName + " " + lName;
                for (int i=0; i<4; i++){
                    read_File.nextLine();
                }
                array++;
            }
            return data;
        } catch (FileNotFoundException e) {
            return data;
        }
    }

    //This method stores the consultation details in a new text file
    public void storeConsultationDetails(){
        try(FileWriter file = new FileWriter("GUI_Consultation_Data.txt")) {   //Creates a text file to store data.
            for (Object[] obj : patientConsultationList) {
                String doctorFullName = (String) obj[0];
                Patient patientObj = (Patient) obj[1];
                Consultation consultationObj = (Consultation) obj[2];

                file.write(doctorFullName + "\n");
                file.write(consultationObj.getStartDateTime() + "\n");
                file.write(consultationObj.getEndDateTime() + "\n");
                file.write(consultationObj.getCost() + "\n");
                file.write(consultationObj.getNotes() + "\n");

                file.write(patientObj.getUniqueID() + "\n");
                file.write(patientObj.getfName() + "\n");
                file.write(patientObj.getlName() + "\n");
                file.write(patientObj.getDob() + "\n");
                file.write(patientObj.getMobileNo() + "\n");
                file.write(patientObj.getNIC() + "\n");
            }
        }catch (Exception ignored){}
    }

    //This method loads the consultation details
    public void loadConsultationDetails(){
        try {
            File file = new File("GUI_Consultation_Data.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            while (read_File.hasNext()){
                Patient patientObj = new Patient();
                Consultation consultationObj = new Consultation();
                String doctorFullName = read_File.nextLine();
                LocalDateTime startDateTime = LocalDateTime.parse(read_File.nextLine());
                LocalDateTime endDateTime = LocalDateTime.parse(read_File.nextLine());
                consultationObj.setStartDateTime(startDateTime);
                consultationObj.setEndDateTime(endDateTime);
                consultationObj.setCost(Double.parseDouble(read_File.nextLine()));
                consultationObj.setNotes(read_File.nextLine());

                patientObj.setUniqueID(read_File.nextLine());
                patientObj.setfName(read_File.nextLine());
                patientObj.setlName(read_File.nextLine());
                patientObj.setDob(LocalDate.parse(read_File.nextLine()));
                patientObj.setMobileNo(read_File.nextLine());
                patientObj.setNIC(read_File.nextLine());

                patientConsultationList.add(new Object[] {doctorFullName, patientObj, consultationObj});

                for (ArrayList<String[]> doctor : doctorTimeList){
                    if (doctorFullName.equalsIgnoreCase(doctor.get(0)[0])){
                        doctor.add(new String[] {String.valueOf(startDateTime), String.valueOf(endDateTime)});
                    }
                }
                uniqueIdList.add(new String[] {patientObj.getNIC(), patientObj.getUniqueID()});
            }
        } catch (FileNotFoundException ignored) {}
    }

    private class buttonHandler implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            JButton actionSource = (JButton) evt.getSource();
            GUI_Menu menu = new GUI_Menu();
            if (actionSource.equals(consultGoBackButton)) {
                menu.guiMenu();
                dispose();
            } else if (actionSource.equals(patientGoBackButton)) {
                removeConsultation();
                hidePatientElements();
                showConsultElements();
            } else if (actionSource.equals(checkAvailabilityButton)) {
                boolean dateStatus = false;
                boolean startTimeStatus = false;
                boolean endTimeStatus = false;
                boolean dateTimeStatus = true;

                //Date validation
                try {
                    LocalDate.parse(date.getText());
                    dateStatus = true;
                } catch (Exception e) {
                    showMessageDialog(null, "Invalid Date!", "Error", ERROR_MESSAGE);
                }

                //Start Time validation
                if (dateStatus) {
                    try {
                        LocalTime.parse(startTime.getText());
                        startTimeStatus = true;
                    } catch (Exception e) {
                        showMessageDialog(null, "Invalid Starting Time!", "Error", ERROR_MESSAGE);
                    }
                }

                //End Time validation
                if (startTimeStatus) {
                    try {
                        LocalTime.parse(endTime.getText());
                        endTimeStatus = true;
                    } catch (Exception e) {
                        showMessageDialog(null, "Invalid Ending Time!", "Error", ERROR_MESSAGE);
                    }
                }

                //Date and Time validation by comparing with the current time
                if (endTimeStatus) {
                    LocalDateTime userStartDateTime = LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(startTime.getText()));
                    LocalDateTime userEndDateTime = LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(endTime.getText()));
                    if (LocalDate.parse(date.getText()).isBefore(LocalDate.now())) {
                        showMessageDialog(null, "The date has already passed!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    } else if (userStartDateTime.isBefore(LocalDateTime.now())) {
                        showMessageDialog(null, "The starting time has already passed!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    } else if (LocalTime.parse(startTime.getText()).isBefore(LocalTime.parse("08:00")) || LocalTime.parse(startTime.getText()).isAfter(LocalTime.parse("17:00"))) {
                        showMessageDialog(null, "The starting time must be in the range between 08:00 - 17:00!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    } else if (userEndDateTime.isBefore(userStartDateTime) || userEndDateTime.isEqual(userStartDateTime)) {
                        showMessageDialog(null, "The ending time must be after the starting time!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    } else if (LocalTime.parse(endTime.getText()).isBefore(LocalTime.parse("08:00")) || LocalTime.parse(endTime.getText()).isAfter(LocalTime.parse("17:00"))) {
                        showMessageDialog(null, "The ending time must be in the range between 08:00 - 17:00!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    } else if (ChronoUnit.MINUTES.between(LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(startTime.getText())), LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(endTime.getText()))) < 5) {
                        showMessageDialog(null, "The minimum duration of a consultation must be at least 5 minutes!", "Error", ERROR_MESSAGE);
                        dateTimeStatus = false;
                    }
                    if (dateTimeStatus) {
                        boolean status = checkAvailability(userStartDateTime, userEndDateTime);
                        if (status) {
                            hideConsultElements();
                            patientWindow();
                        }
                    }
                }
            } else if (actionSource.equals(confirmButton)) {
                boolean isValidated = true;
                WestminsterSkinConsultationManager driverObj = new WestminsterSkinConsultationManager();

                //Validate the first name
                if ((driverObj.characterValidate(fNameTextField.getText()) > 0) || (fNameTextField.getText().equals(""))) {
                    showMessageDialog(null, "The First Name can only contain LETTERS!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                }

                //Validate the last name
                if (isValidated && ((driverObj.characterValidate(lNameTextField.getText()) > 0) || (lNameTextField.getText().equals("")))){
                    showMessageDialog(null, "The Last Name can only contain LETTERS!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                }

                //Validate the dob
                if (isValidated){
                    try {
                        LocalDate date =  LocalDate.parse(dobTextField.getText());
                        boolean validate = dateValidate(date);
                        if (validate){
                            showMessageDialog(null, "The age of the patient must be in between 3 and 100", "Error", ERROR_MESSAGE);
                            isValidated = false;
                        }
                    }catch (Exception e){
                        showMessageDialog(null, "The Date of Birth is invalid!", "Error", ERROR_MESSAGE);
                        isValidated = false;
                    }
                }

                //Validate the mobile number
                if (isValidated && (driverObj.characterValidate(mobileNoTextField.getText()) != mobileNoTextField.getText().length())) {
                    showMessageDialog(null, "The mobile number can only contain NUMBERS!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                } else if (isValidated && (mobileNoTextField.getText().length() != 10)) {
                    showMessageDialog(null, "The length of the mobile number must be 10!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                }

                //Validate the NIC
                if (isValidated && (driverObj.characterValidate(nicTextField.getText()) != nicTextField.getText().length())) {
                    showMessageDialog(null, "The NIC can only contain NUMBERS!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                } else if (isValidated && (nicTextField.getText().length() != 12)) {
                    showMessageDialog(null, "The length of the NIC must be 12!", "Error", ERROR_MESSAGE);
                    isValidated = false;
                }

                //Success message
                if (isValidated) {
                    if (addImgCount == 0){
                        addPatientNotes(new File("/Users/sarithwijesundera/Desktop/w1912785_OOP_CW/noImage.png"));
                    }

                    String message = checkNIC(nicTextField.getText());

                    Patient patientObj = new Patient();
                    Consultation consultationObj = new Consultation();
                    patientObj.setfName(fNameTextField.getText());
                    patientObj.setlName(lNameTextField.getText());
                    patientObj.setDob(LocalDate.parse(dobTextField.getText()));
                    patientObj.setMobileNo(mobileNoTextField.getText());
                    patientObj.setUniqueID(String.valueOf(uniqueID));
                    patientObj.setNIC(nicTextField.getText());
                    consultationObj.setStartDateTime(LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(startTime.getText())));
                    consultationObj.setEndDateTime(LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(endTime.getText())));
                    consultationObj.setCost(Math.round(cost * 100.0) / 100.0);
                    if (notesTextArea.getText().equals("")){
                        consultationObj.setNotes("Empty");
                    } else {
                        consultationObj.setNotes(notesTextArea.getText());
                    }
                    patientConsultationList.add(new Object[] {doctorFullName, patientObj, consultationObj});

                    storeConsultationDetails();
                    showMessageDialog(null, message, "Consultation is Success", JOptionPane.INFORMATION_MESSAGE);
                    menu.guiMenu();
                    dispose();
                }
            } else if (actionSource.equals(addImgButton)) {
                showMessageDialog(null, "NOTE:\nOnce you added an image, you cannot edit the text in the Notes\nand you cannot go back!", "For Your Information", JOptionPane.INFORMATION_MESSAGE);
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                int dialogBox = fileChooser.showOpenDialog(null);

                if (dialogBox == JFileChooser.APPROVE_OPTION) {
                    addImgCount++;
                    File file = fileChooser.getSelectedFile();
                    addPatientNotes(file);
                }
            }
        }
    }

    //Initializing the arraylists
    ArrayList<ArrayList<String[]>> doctorTimeList = new ArrayList<>(10);
    ArrayList<Object[]> patientConsultationList = new ArrayList<>();
    ArrayList<String[]> uniqueIdList = new ArrayList<>();
    public void createDoctorList(){
        for (String name: doctorNames){
            ArrayList<String[]> doctor = new ArrayList<>();
            doctor.add(new String[]{name});
            doctorTimeList.add(doctor);
        }
    }

    //Check the availability of a selected doctor
    public boolean checkAvailability(LocalDateTime userStartDateTime, LocalDateTime userEndDateTime){
        for (ArrayList<String[]> doctor : doctorTimeList) {
            if (Objects.requireNonNull(dropList.getSelectedItem()).toString().equalsIgnoreCase(doctor.get(0)[0])) {
                for (int i = 1; i < doctor.size(); i++) {
                    boolean condition1 = userStartDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[0])) >= 0;
                    boolean condition2 = userStartDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[1])) <= 0;
                    boolean condition3 = userEndDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[0])) >= 0;
                    boolean condition4 = userEndDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[1])) <= 0;
                    boolean condition5 = userStartDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[0])) <= 0;
                    boolean condition6 = userEndDateTime.compareTo(LocalDateTime.parse(doctor.get(i)[1])) >= 0;

                    //If the doctor is not available call the random doctor method
                    if ((condition1 && condition2) || (condition3 && condition4) || (condition5 && condition6)) {
                        return randomDoctor(userStartDateTime, userEndDateTime);
                    }
                }
                doctor.add(new String[]{String.valueOf(userStartDateTime), String.valueOf(userEndDateTime)});
                index = doctorTimeList.indexOf(doctor);
                doctorFullName = doctor.get(0)[0];
                showMessageDialog(null, "The doctor is available!\n\nDoctor Name: " + doctorFullName + "\nStarting Date: " + LocalDate.parse(date.getText()) + "\nStarting Time: " + LocalTime.parse(startTime.getText())+ "\nEnding Time: " + LocalTime.parse(endTime.getText()), "Consultation Details", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        }
        return false;
    }

    //This method assigns a random doctor, if the doctor is not available
    public boolean randomDoctor(LocalDateTime userStartDateTime, LocalDateTime userEndDateTime){
        int loopCount = 1;
        ArrayList<Integer> randomList = new ArrayList<>();
        while (true) {
            int randomIndex = (int) (Math.random() * doctorTimeList.size());
            randomList.add(randomIndex);
            if (Collections.frequency(randomList, randomIndex) > 1){
                continue;
            }
            int notValidCount = 0;
            int doctorIndex = 0;
            for (int i=1; i<doctorTimeList.get(randomIndex).size(); i++){
                boolean condition1 = userStartDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[0]))>=0;
                boolean condition2 = userStartDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[1]))<=0;
                boolean condition3 = userEndDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[0]))>=0;
                boolean condition4 = userEndDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[1]))<=0;
                boolean condition5 = userStartDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[0]))<=0;
                boolean condition6 = userEndDateTime.compareTo(LocalDateTime.parse(doctorTimeList.get(randomIndex).get(i)[0]))>=0;

                if ((condition1 && condition2) || (condition3 && condition4) || (condition5 && condition6)) {
                    notValidCount++;
                }
                doctorIndex = i;
            }
            if (notValidCount < 1) {
                doctorTimeList.get(randomIndex).add(new String[]{String.valueOf(userStartDateTime), String.valueOf(userEndDateTime)});
                index = doctorTimeList.indexOf(doctorTimeList.get(randomIndex));
                doctorFullName = doctorTimeList.get(randomIndex).get(doctorIndex)[0];
                showMessageDialog(null, "The doctor is not available!\nA new doctor will be allocated automatically!\n\nDoctor Name: " + doctorFullName + "\nStarting Date: " + LocalDate.parse(date.getText()) + "\nStarting Time: " + LocalTime.parse(startTime.getText())+ "\nEnding Time: " + LocalTime.parse(endTime.getText()), "Consultation Details", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
            loopCount++;
            if (loopCount > doctorTimeList.size()){
                showMessageDialog(null, "Sorry!\n\nAll the doctors in the center are not available at that time", "Information", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }
    }

    //This method removes the last added consultation, when the user presses Go Back button in the confirmation window
    public void removeConsultation(){
        doctorTimeList.get(index).remove(doctorTimeList.get(index).size()-1);
    }

    //This method validates the date of birth
    public boolean dateValidate(LocalDate date){
        return !((date.isAfter(LocalDate.parse("1902-12-31"))) && (date.isBefore(LocalDate.parse("2020-01-01"))));
    }

    //This method hides all the elements in the Consult a Doctor window
    public void hideConsultElements(){
        consultHeadingLabel.setVisible(false);
        dropLabel.setVisible(false);
        dropList.setVisible(false);
        dateLabel.setVisible(false);
        date.setVisible(false);
        startTimeLabel.setVisible(false);
        startTime.setVisible(false);
        endTimeLabel.setVisible(false);
        endTime.setVisible(false);
        checkAvailabilityButton.setVisible(false);
        consultGoBackButton.setVisible(false);
        consultationLogo.setVisible(false);
        consultationBgImage1.setVisible(false);
        consultationBgImage2.setVisible(false);
        scrollPane.setVisible(false);
        randomLabel.setVisible(false);
    }

    //This method displays all the elements in the Consult a Doctor window
    public void showConsultElements(){
        this.setSize(550,640);
        this.setTitle("Consult a Doctor");
        buttonHandler handler = new buttonHandler();

        //Adding the Back to Menu button and the logo
        consultGoBackButton = new JButton("Back to Menu");
        consultGoBackButton.addActionListener(handler);
        consultGoBackButton.setBounds(50,520,120,40);
        consultGoBackButton.setBorder(BorderFactory.createLineBorder(Color.red));
        consultGoBackButton.setBackground(Color.decode("#FFB7B7"));
        consultGoBackButton.setOpaque(true);
        this.add(consultGoBackButton);

        consultationLogo = new JLabel();
        consultationLogo .setIcon(new ImageIcon("logo.png"));
        consultationLogo .setBounds(20, 10, 200, 100);
        this.add(consultationLogo);

        //Checking whether there are doctors in the center or not
        if (doctorNames[0] == null){
            noDataLabel = new JLabel("Oops...Currently there are no doctors in the center!");
            noDataLabel.setBounds(90, 390, 500, 100);
            noDataLabel.setFont(new Font("Chalkboard", Font.PLAIN, 15));
            this.add(noDataLabel);

            noDataImage = new JLabel();
            noDataImage.setIcon(new ImageIcon("notFound.jpg"));
            noDataImage.setBounds(0, 80, 500, 400);
            this.add(noDataImage);

        }else {
            //Displaying the opening hours and the costs in a table
            String[] columnNames = {"", ""};
            Object[][] data =  {{"Open Hours", "08:00 - 17:00"},
                    {"First Consultation", "£15 per hour"},
                    {"Each Consultation", "£25 per hour"}};

            JTable table = new JTable();
            TableModel model = new DefaultTableModel(data, columnNames);
            table.setModel(model);
            scrollPane = new JScrollPane(table);
            table.setGridColor(Color.black);
            table.setRowHeight(30);
            scrollPane.setBounds(210, 20, 295, 96);
            ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setHorizontalAlignment( SwingConstants.CENTER );
            this.add(scrollPane);

            //Adding JFrame components
            randomLabel = new JLabel("If the doctor is not available, a random doctor will be assigned automatically");
            randomLabel.setBounds(60,205,600,20);
            randomLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            randomLabel.setForeground(Color.red);
            this.add(randomLabel);

            dropLabel = new JLabel("List of Doctors: ");
            dropLabel.setBounds(50, 207, 200, 100);
            this.add(dropLabel);

            dropList = new JComboBox<>(doctorNames);
            dropList.setBounds(285, 210, 212, 100);
            this.add(dropList);

            dateLabel = new JLabel("Date (yyyy-MM-dd):");
            dateLabel.setBounds(50,300,200,25);
            this.add(dateLabel);

            date = new JTextField(String.valueOf(LocalDate.now()));
            date.setBounds(290,300,200,25);
            date.setBorder(new LineBorder(Color.black,1));
            this.add(date);

            startTimeLabel = new JLabel("Starting Time (hh:mm in 24h clock):");
            startTimeLabel.setBounds(50,350,250,25);
            this.add(startTimeLabel);

            startTime = new JTextField(String.valueOf(LocalTime.of(LocalTime.parse("08:00").getHour(), LocalTime.parse("08:00").getMinute())));
            startTime.setBounds(290,350,200,25);
            startTime.setBorder(new LineBorder(Color.black,1));
            this.add(startTime);

            endTimeLabel = new JLabel("Ending Time (hh:mm in 24h clock):");
            endTimeLabel.setBounds(50,405,250,25);
            this.add(endTimeLabel);

            endTime = new JTextField(String.valueOf(LocalTime.of(LocalTime.parse("08:05").getHour(), LocalTime.parse("08:05").getMinute())));
            endTime.setBounds(290,405,200,25);
            endTime.setBorder(new LineBorder(Color.black,1));
            this.add(endTime);

            checkAvailabilityButton = new JButton("Check Availability");
            checkAvailabilityButton.addActionListener(handler);
            checkAvailabilityButton.setBounds(343,520,150,40);
            checkAvailabilityButton.setBorder(BorderFactory.createLineBorder(Color.blue));
            checkAvailabilityButton.setBackground(Color.decode("#E4EEFF"));
            checkAvailabilityButton.setOpaque(true);
            this.add(checkAvailabilityButton);

            consultHeadingLabel = new JLabel();
            consultHeadingLabel.setBounds(20, 170, 500, 300);
            consultHeadingLabel.setOpaque(true);
            TitledBorder title = BorderFactory.createTitledBorder(blackLine, "DOCTOR CONSULTATION");
            title.setTitleJustification(TitledBorder.CENTER);
            consultHeadingLabel.setBorder(title);
            consultHeadingLabel.setBackground(Color.decode("#E4EEFF"));
            this.add(consultHeadingLabel);

            menuObj.btnHoverEffect(checkAvailabilityButton, Color.blue, Color.decode("#CDE0FF"), Color.blue);
        }
        //Adding images
        consultationBgImage1 = new JLabel();
        consultationBgImage1 .setIcon(new ImageIcon("background.jpg"));
        consultationBgImage1 .setBounds(0, -20, 1440, 500);
        this.add(consultationBgImage1 );

        consultationBgImage2 = new JLabel();
        consultationBgImage2 .setIcon(new ImageIcon("background.jpg"));
        consultationBgImage2 .setBounds(0, 240, 1440, 500);
        this.add(consultationBgImage2);

        menuObj.btnHoverEffect(consultGoBackButton, Color.red, Color.decode("#FFA6A6"), Color.red);
    }

    //This method hides all the elements in the confirmation window
    public void hidePatientElements(){
        patientGoBackButton.setVisible(false);
        scrollPane.setVisible(false);
        patientHeadingLabel.setVisible(false);
        fNameLabel.setVisible(false);
        lNameLabel.setVisible(false);
        dobLabel.setVisible(false);
        mobileNoLabel.setVisible(false);
        nicLabel.setVisible(false);
        notesLabel.setVisible(false);
        notesTextArea.setVisible(false);
        addImgLabel.setVisible(false);
        addImgButton.setVisible(false);
        tickLabel.setVisible(false);
        fNameTextField.setVisible(false);
        lNameTextField.setVisible(false);
        dobTextField.setVisible(false);
        mobileNoTextField.setVisible(false);
        nicTextField.setVisible(false);
        confirmButton.setVisible(false);
        patientBgImage1.setVisible(false);
        patientBgImage2.setVisible(false);
        patientLogo.setVisible(false);
    }

    //This method displays all the elements in the confirmation window
    public void patientWindow(){
        this.setSize(520,700);
        this.setTitle("Confirmation Window");
        buttonHandler handler = new buttonHandler();

        patientGoBackButton = new JButton("Go Back");
        patientGoBackButton.addActionListener(handler);
        patientGoBackButton.setBounds(50,605,80,40);
        patientGoBackButton.setBorder(BorderFactory.createLineBorder(Color.RED));
        patientGoBackButton.setBackground(Color.decode("#FFB7B7"));
        patientGoBackButton.setOpaque(true);
        this.add(patientGoBackButton);

        //Displaying the consultation information in a table
        String[] columnNames = {"", ""};
        Object[][] data =  {{"Doctor Name", doctorFullName},
                            {"Date", LocalDate.parse(date.getText())},
                            {"Start Time",LocalTime.parse(startTime.getText())},
                            {"End Time", LocalTime.parse(endTime.getText())}};

        JTable table = new JTable();
        TableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
        scrollPane = new JScrollPane(table);
        table.setGridColor(Color.black);
        table.setRowHeight(30);
        scrollPane.setBounds(185, 20, 295, 126);
        ((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class)).setHorizontalAlignment( SwingConstants.CENTER );
        this.add(scrollPane);

        //Adding JFrame components
        patientLogo = new JLabel();
        patientLogo .setIcon(new ImageIcon("logo.png"));
        patientLogo .setBounds(20, 10, 200, 100);
        this.add(patientLogo);

        fNameLabel = new JLabel("First Name:");
        fNameLabel.setBounds(50, 177, 200, 100);
        this.add(fNameLabel);

        fNameTextField = new JTextField();
        fNameTextField.setBounds(250, 220,200,25);
        fNameTextField.setBorder(new LineBorder(Color.black,1));
        this.add(fNameTextField);

        lNameLabel = new JLabel("Last Name:");
        lNameLabel.setBounds(50,270,200,25);
        this.add(lNameLabel);

        lNameTextField = new JTextField();
        lNameTextField.setBounds(250, 270,200,25);
        lNameTextField.setBorder(new LineBorder(Color.black,1));
        this.add(lNameTextField);

        dobLabel = new JLabel("Date of Birth (yyyy-MM-dd):");
        dobLabel.setBounds(50,320,250,25);
        this.add(dobLabel);

        dobTextField = new JTextField();
        dobTextField.setBounds(250, 320,200,25);
        dobTextField.setBorder(new LineBorder(Color.black,1));
        this.add(dobTextField);

        mobileNoLabel = new JLabel("Mobile Number:");
        mobileNoLabel.setBounds(50,370,250,25);
        this.add(mobileNoLabel);

        mobileNoTextField = new JTextField();
        mobileNoTextField.setBounds(250, 370,200,25);
        mobileNoTextField.setBorder(new LineBorder(Color.black,1));
        this.add(mobileNoTextField);

        nicLabel = new JLabel("NIC:");
        nicLabel.setBounds(50,420,250,25);
        this.add(nicLabel);

        nicTextField = new JTextField();
        nicTextField.setBounds(250, 420,200,25);
        nicTextField.setBorder(new LineBorder(Color.black,1));
        this.add(nicTextField);

        notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(50,470,250,25);
        this.add(notesLabel);

        notesTextArea = new JTextArea();
        notesTextArea.setBounds(250,470,200,40);
        notesTextArea.setBorder(new LineBorder(Color.black,1));
        this.add(notesTextArea);

        addImgLabel = new JLabel("Add an Image (Max 01):");
        addImgLabel.setBounds(50, 530, 250, 25);
        this.add(addImgLabel);

        addImgButton = new JButton("Select a file");
        addImgButton.addActionListener(handler);
        addImgButton.setBounds(290, 530, 120, 25);
        this.add(addImgButton);

        tickLabel = new JLabel();
        tickLabel.setBounds(415, 529, 50,25);
        tickLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        this.add(tickLabel);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(handler);
        confirmButton.setBounds(353,605,100,40);
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.decode("#56D400")));
        confirmButton.setBackground(Color.decode("#D8FFBD"));
        confirmButton.setOpaque(true);
        this.add(confirmButton);

        patientHeadingLabel = new JLabel();
        patientHeadingLabel.setBounds(30, 180, 450, 400);
        patientHeadingLabel.setOpaque(true);
        TitledBorder title = BorderFactory.createTitledBorder(blackLine, "PATIENT INFORMATION");
        title.setTitleJustification(TitledBorder.CENTER);
        patientHeadingLabel.setBorder(title);
        patientHeadingLabel.setBackground(Color.decode("#E4EEFF"));
        this.add(patientHeadingLabel);


        patientBgImage1 = new JLabel();
        patientBgImage1 .setIcon(new ImageIcon("background.jpg"));
        patientBgImage1 .setBounds(0, -20, 1440, 500);
        this.add(patientBgImage1 );

        patientBgImage2 = new JLabel();
        patientBgImage2 .setIcon(new ImageIcon("background.jpg"));
        patientBgImage2 .setBounds(0, 450, 1440, 500);
        this.add(patientBgImage2);

        menuObj.btnHoverEffect(confirmButton, Color.decode("#4CBA00"), Color.decode("#C4FF9B"), Color.green);
        menuObj.btnHoverEffect(patientGoBackButton, Color.red, Color.decode("#FFA6A6"), Color.red);
    }

    //This method calculate the cost based on the NIC of the patient
    public String checkNIC(String userNIC){
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(startTime.getText())), LocalDateTime.of(LocalDate.parse(date.getText()), LocalTime.parse(endTime.getText())));
        int similarCount = 0;
        int indexOfSimilar = 0;
        for (String[] nic: uniqueIdList){
            if (nic[0].equals(userNIC)){
                similarCount++;
                indexOfSimilar = uniqueIdList.indexOf(nic);
            }
            if (Integer.parseInt(nic[1]) > uniqueID){
                uniqueID = Integer.parseInt(nic[1]);
            }
        }
        if (similarCount == 0){
            uniqueID++;
            cost = (double) 15 * minutes / 60;
            uniqueIdList.add(new String[] {userNIC, String.valueOf(uniqueID)});
            return "This is your first consultation\nIt is £15 per hour\n\nYour patient ID: " + uniqueID + "\nCost: £" + Math.round(cost * 100.0) / 100.0;
        }else {
            uniqueID = Integer.parseInt(uniqueIdList.get(indexOfSimilar)[1]);
            cost = (double) 25 * minutes / 60;
            uniqueIdList.add(new String[] {userNIC, String.valueOf(uniqueID)});
            return "This is your " + (similarCount+1) + " consultation\nIt is £25 per hour\n\nYour patient ID: " + uniqueID + "\nCost: £" + Math.round(cost * 100.0) / 100.0;
        }
    }

    //This method uniquely saves the file names of the patient notes (texts and images) in a text file
    public void savePatientNoteName(String name) throws IOException {
        try(FileWriter file = new FileWriter("Patient_Note_Names.txt")) {   //Creates a text file to store patient note names
            int num = Integer.parseInt(name);
            for (int i=1; i<=num; i++){
                file.write(i + "\n");
            }
        }catch (Exception ignored){}

        //Saves the encrypted textual notes of the patient in a text file
        try(FileWriter file = new FileWriter("/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Texts/" + name + ".txt")) {   //Creates a text file to store patient texts
            if (notesTextArea.getText().equals("")){
                file.write("Empty" + "\n");
            } else {
                file.write(notesTextArea.getText() + "\n");
            }
        }catch (Exception ignored){}
        //Encrypt the textual note
        encryptObj.main("/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Texts/" + name + ".txt");
    }

    //This method opens the patient note file names and get the next unique file name to save the notes
    public String getPatientNoteName(){
        String name;
        int count = 1;
        try {
            File file = new File("Patient_Note_Names.txt");   //Opening the text file.
            Scanner read_File = new Scanner(file);    //Reads the content in the text file.
            while (read_File.hasNext()){
                read_File.nextLine();
                count++;
            }
            name = String.valueOf(count);
        }catch (Exception e){
            name = "1";
        }
        return name;
    }

    //This method saves the encrypted images of the patient in a selected location
    public void addPatientNotes(File file){
        String noteName = getPatientNoteName();
        InputStream is;
        OutputStream os;
        try {
            is = new FileInputStream(file);
            String filePath = "/Users/lakinduindipa/Desktop/w1912890_OOP_CW/Patient_Images/" + noteName + ".png";
            os = new FileOutputStream(filePath);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
            is.close();
            os.close();
            savePatientNoteName(noteName);
            tickLabel.setText("✅");
            addImgButton.setEnabled(false);
            patientGoBackButton.setEnabled(false);
            notesTextArea.setEnabled(false);
            //Encrypt the image
            encryptObj.main(filePath);
        } catch (Exception ignored){}
    }

    public void guiConsult() {
        GUI_Consult consult = new GUI_Consult();
        consult.setVisible(true);
        consult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}