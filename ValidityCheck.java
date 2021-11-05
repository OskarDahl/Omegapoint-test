import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidityCheck {


    //Input: A string of length 12 or 10
    //Output: Boolean if the number passes Luhns algorithm, also checks length of number
    private static boolean luhnsCheck(String pnr){
        if(pnr.length() == 12)
            pnr = pnr.substring(2);
        if(pnr.length()!=10){
            return false;
        }

        int total = 0;
        for (int i = 0; i < pnr.length()-1; i++) {
            int number = Character.getNumericValue(pnr.charAt(i)) * (2-(i%2));
            total += number/10 + number%10;
        }
        int checkValue = (10 - (total%10))%10;
        if (checkValue == Character.getNumericValue(pnr.charAt(pnr.length()-1)))
            return true;
        return false;
    }

    //Input: String with personal number
    //Output: Boolean, true if it is a valid date or valid coordination "date"
    private static boolean isValidDate(String pnr){
        if(pnr.length() == 12){
            int year = Integer.parseInt(pnr.substring(0,2));
            if (!(year >= 18 && year <= 20)){
                return false;
            }
            else
                pnr = pnr.substring(2);
        }
        int[] daysInAMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
        if (Integer.parseInt(pnr.substring(0,2)) % 4 == 0)
            daysInAMonth[1] =29;
        int month = Integer.parseInt(pnr.substring(2,4)), day = Integer.parseInt(pnr.substring(4,6));
        if(day <= daysInAMonth[month-1]){
            return true;
        }
        else if(day >= 60 && day-60 <= daysInAMonth[month]){
            return true;
        }
        return false;
    }

    //Input: String with potential organization number
    //Output: Returns true if it is a valid organization number, otherwise false
    private static boolean isOrgNr(String orgnr){
        if(orgnr.length() == 12){
            if(!orgnr.startsWith("16"))
                return false;
            orgnr = orgnr.substring(2);
        }
        if(Integer.parseInt(orgnr.substring(2,4)) >=20)
            return true;
        return false;
    }

    //Formats input to only contain numbers, also checks if there is any letters in the input
    private static String formatInput(String input){
        Pattern p = Pattern.compile("[a-zA-Z]");
        Matcher match = p.matcher(input);
        if (match.find()){
            return "";
        }

        return input.replaceAll("[^0-9]", "");
    }

    public static void main(String[] args) {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("Please enter your personal number, coordination number, or organization number\nTo exit the program enter 0");
            String input = sc.nextLine();

            if(input.equals("0"))
                System.exit(0);

            String formattedInput = formatInput(input);
            boolean check = luhnsCheck(formattedInput);
            if (check && (isOrgNr(formattedInput) || isValidDate(formattedInput))) {
                System.out.println("The given number is valid");
            }
            else {
                System.out.println("The given number is not valid");
                try {
                    FileWriter writer = new FileWriter("invalidNumbersLog.txt", true);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    writer.write(dtf.format(LocalDateTime.now()) + " " + input + " is not a valid number\n");
                    writer.close();
                }
                catch (IOException e) {
                    System.out.println("An error occurred.");
                }
            }
        }
    }
}
