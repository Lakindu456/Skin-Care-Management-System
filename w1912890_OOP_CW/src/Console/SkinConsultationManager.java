package Console;

import java.util.Scanner;

interface SkinConsultationManager {
    String displayMenu();
    String addDoctor(Scanner input);
    String deleteDoctor(Scanner input);
    void displayDoctor();
    void saveDoctor(String fileName);
    void loadDoctor();
}
