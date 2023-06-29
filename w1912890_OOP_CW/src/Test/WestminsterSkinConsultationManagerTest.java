package Test;

import Console.WestminsterSkinConsultationManager;
import org.junit.jupiter.api.Assertions;

class WestminsterSkinConsultationManagerTest {
    WestminsterSkinConsultationManager obj = new WestminsterSkinConsultationManager();

    //--------------------------------Testing the validation of the first and last name of the doctor-------------------
    @org.junit.jupiter.api.Test
    void correctNameValidate() {
        Assertions.assertEquals("lakindu", obj.nameValidate("lakindu"));
    }

    @org.junit.jupiter.api.Test
    void emptyNameValidate() {
        Assertions.assertEquals("Invalid", obj.nameValidate(""));
    }

    @org.junit.jupiter.api.Test
    void nameWithNumbersValidate() {
        Assertions.assertEquals("Invalid", obj.nameValidate("lakindu123"));
    }

    @org.junit.jupiter.api.Test
    void nameWithSpecialCharacterValidate() {
        Assertions.assertEquals("Invalid", obj.nameValidate("lakindu@#$"));
    }

    @org.junit.jupiter.api.Test
    void backToMenuFromNameValidate() {
        Assertions.assertEquals("Redirecting back to the menu...", obj.nameValidate("X"));
    }



    //--------------------------------Testing the validation of the DOB of the doctor-----------------------------------
    @org.junit.jupiter.api.Test
    void correctDateValidate() {
        Assertions.assertEquals("2000-01-01", obj.dobValidate("2000-01-01"));
    }

    @org.junit.jupiter.api.Test
    void emptyDateValidate() {
        Assertions.assertEquals("Invalid Format", obj.dobValidate(""));
    }

    @org.junit.jupiter.api.Test
    void dateWithNumbersAndSpecialCharactersValidate() {
        Assertions.assertEquals("Invalid Format", obj.dobValidate("2000-:@#"));
    }

    @org.junit.jupiter.api.Test
    void dateWithLettersValidate() {
        Assertions.assertEquals("Invalid Format", obj.dobValidate("letters"));
    }

    @org.junit.jupiter.api.Test
    void dateWithImproperFormatValidate() {
        Assertions.assertEquals("Invalid Format", obj.dobValidate("01-01-2000"));
    }

    @org.junit.jupiter.api.Test
    void dateWithNonExistingDateValidate() {
        Assertions.assertEquals("Invalid Format", obj.dobValidate("2000-02-30"));
    }

    @org.junit.jupiter.api.Test
    void ageIsLessThan21Validate() {
        Assertions.assertEquals("Invalid Age", obj.dobValidate("2004-08-18"));
    }

    @org.junit.jupiter.api.Test
    void ageIsGreaterThan60Validate() {
        Assertions.assertEquals("Invalid Age", obj.dobValidate("1950-08-18"));
    }

    @org.junit.jupiter.api.Test
    void backToMenuFromDobValidate() {
        Assertions.assertEquals("Redirecting back to the menu...", obj.dobValidate("X"));
    }



    //--------------------------------Testing the validation of the mobile number of the doctor-------------------------
    @org.junit.jupiter.api.Test
    void correctMobileNoValidate() {
        Assertions.assertEquals("0713848627", obj.mobileNoValidate("0713848627"));
    }

    @org.junit.jupiter.api.Test
    void emptyMobileNoValidate() {
        Assertions.assertEquals("Invalid Length", obj.mobileNoValidate(""));
    }

    @org.junit.jupiter.api.Test
    void lengthIsLessThan10Validate() {
        Assertions.assertEquals("Invalid Length", obj.mobileNoValidate("12345"));
    }

    @org.junit.jupiter.api.Test
    void lengthIsGreaterThan10Validate() {
        Assertions.assertEquals("Invalid Length", obj.mobileNoValidate("12345678910"));
    }

    @org.junit.jupiter.api.Test
    void mobileNoWithLettersValidate() {
        Assertions.assertEquals("Invalid Input", obj.mobileNoValidate("12345hja"));
    }

    @org.junit.jupiter.api.Test
    void mobileNoWithSpecialCharactersValidate() {
        Assertions.assertEquals("Invalid Input", obj.mobileNoValidate("12345@#$"));
    }

    @org.junit.jupiter.api.Test
    void backToMenuFromMobileNoValidate() {
        Assertions.assertEquals("Redirecting back to the menu...", obj.mobileNoValidate("X"));
    }



    //----------------------------Testing the validation of the medical license number of the doctor--------------------
    @org.junit.jupiter.api.Test
    void correctMedLiceNoValidate() {
        Assertions.assertEquals("1234", obj.medLiceNoValidate("1234"));
    }

    @org.junit.jupiter.api.Test
    void emptyMedLiceNoValidate() {
        Assertions.assertEquals("Invalid", obj.medLiceNoValidate(""));
    }

    @org.junit.jupiter.api.Test
    void medLiceNoWithLettersValidate() {
        Assertions.assertEquals("Invalid", obj.medLiceNoValidate("123hjasd"));
    }

    @org.junit.jupiter.api.Test
    void medLiceNoWithSpecialCharactersValidate() {
        Assertions.assertEquals("Invalid", obj.medLiceNoValidate("123@#$"));
    }

    @org.junit.jupiter.api.Test
    void backToMenuFromMedLiceNoValidate() {
        Assertions.assertEquals("Redirecting back to the menu...", obj.medLiceNoValidate("X"));
    }



    //----------------------------Testing the validation of the specialization of the doctor----------------------------
    @org.junit.jupiter.api.Test
    void correctSpecializationValidate() {
        Assertions.assertEquals("Skin-1", obj.specializationValidate("Skin-1"));
    }

    @org.junit.jupiter.api.Test
    void emptySpecializationValidate() {
        Assertions.assertEquals("Invalid", obj.specializationValidate(""));
    }

    @org.junit.jupiter.api.Test
    void backToMenuFromSpecializationValidate() {
        Assertions.assertEquals("Redirecting back to the menu...", obj.specializationValidate("X"));
    }
}