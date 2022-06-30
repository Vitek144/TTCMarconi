package cz.cvut.homolka;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class MainClass {

    private static final String HELP_NOTIFICATION = """
            TYPE "end" TO TERMINATE THE PROGRAM\s
            PROGRAM IS PROCESSING NUMBERS WHEN COUNT OF NUMBERS IS ODD THE PROGRAM WILL WRITE ONLY ODD NUMBERS SAME FUNCTION IS FOR EVEN COUNT AND EVEN NUMBERS\s
            THERE ARE 4 FUNCTIONS:\s
            1. WRITE COUNT OF NUMBERS YOU WANT TO TEST | EXAMPLE --> 5\s
            2. WRITE COUNT OF NUMBERS YOU WANT TO TEST AND FILE NAME WERE YOU WANT TO STORE THE NUMBERS | EXAMPLE --> 5 "numbers.txt"\s
            3. WRITE SOURCE FILE FROM WHERE WILL PROGRAM TAKE THE NUMBERS | EXAMPLE --> "C:\\Documents"\s
            4. WRITE SOURCE FILE FROM WHERE WILL PROGRAM TAKE THE NUMBERS AND FILE NAME WERE YOU WANT TO STORE THE NUMBERS | EXAMPLE --> "C:\\Documents" "numbers.txt\"""";



    public static void main(String[] args) {
        rightInputAndBuilder();
    }

    //               "C:\Games\numbers.txt"

    /**
     * Function checking rightness of parameters and completing needed task
     */
    private static void rightInputAndBuilder(){

        boolean rightFirstParam = false;

        Scanner sc = new Scanner(System.in);
        String command;
        String[] splitCommand;
        String param;
        String param2;
        System.out.println("FOR HELP TYPE \"help\" OR \"?\" ");

        while(!rightFirstParam) {

            command = sc.nextLine();
            splitCommand = command.split(" ",2);
            param = splitCommand[0];
            param = param.trim();
            try{
                param2 = splitCommand[1];
                param2 = param2.trim();

            }catch(IndexOutOfBoundsException e){
                param2 = null;
            }

            if (param.equals("help") || param.equals("?")) {
                System.out.println(HELP_NOTIFICATION);

            } else if (param.equals("end")) {
                rightFirstParam = true;

            } else{
                List<Integer> list = new ArrayList<>(firstParamValidation(param));
                rightFirstParam = secondParamValidation(list, param2);
            }

        }
        System.out.println("----END----");
    }

    /**
     * Checking if link is wrapped in quotation marks and deletes them
     * Example: "C:\Documents" ---> C:\Documents
     * @param param wanted parameter from console
     * @return  If parameter is valid --> attribute without quotation marks, Else --> null
     */
    private static String linkCorrection(String param){
        String parameter = null;
        char c = param.charAt(0);
        int length = param.length();
        char cEnd = param.charAt(length-1);

        if(c == '\"' && cEnd == '\"') {
            parameter = param.replace("\"", "");
        }
        return parameter;
    }

    /**
     * Validate and process second parameter
     * @param list of numbers from first parameter
     * @param param2 wanted second parameter from console
     * @return If second param is valid --> true, otherwise --> false
     */
    private static boolean secondParamValidation(List<Integer> list, String param2){
        boolean rightParam = false;

        if(list.size()>0) {
            list = new ArrayList<>(filteringIntegerList(list));

            if (param2!= null) {
                System.out.println("----NUMBERS_HAVE_BEEN_WRITTEN_TO_THE_FILE----");
                param2 = linkCorrection(param2);
                writeIntoFile(param2, list);

            }else{
                System.out.println("----WRITING_NUMBERS_INTO_THE_CONSOLE----");
                for(Integer integer : list){
                    System.out.println(integer);
                }
            }
            rightParam = true;
        }
        return rightParam;
    }

    /**
     * Validate first parameter
     * @param param wanted parameter from console
     * @return list of Integers from file or console
     */
    private static List<Integer> firstParamValidation(String param){

        int n;
        List<Integer> lister = new ArrayList<>();
        String parameter = linkCorrection(param);
        if(parameter != null) {
            param = parameter;
            lister = new ArrayList<>(getIntegerListFromFileInput(param));

        }else{
            try {
                n = Integer.parseInt(param);
                lister = new ArrayList<>(standardInput(n));

            }catch (NumberFormatException ex) {
                System.out.println("Parameters are not valid try it again");
            }
        }
        return lister;
    }

    /**
     * Function for inputting from console
     * @param n count of numbers
     * @return list of Integers with numbers for next processing
     */
    private static List<Integer> standardInput(int n){
        List<Integer> list = new ArrayList<>();

        if (n > 0){
            Scanner scanner = new Scanner(System.in);
            int i=0;
            while(i < n) {
                System.out.println("Add number: ");
                    try {
                        list.add(scanner.nextInt());
                        i++;
                    } catch (InputMismatchException exe) {
                        System.out.println("Error! Write a number");
                        scanner.next();
                    }
            }
        }else{
            System.out.println("Wrong Number! Number must bigger than 1");
        }
        return list;
    }

    /**
     * Filter for given numbers - If count of numbers is even then filter only the even one
     * If count of numbers is odd then filter only the odd one
     * @param list of numbers
     * @return list of Integers with even or odd numbers
     */
    private static List<Integer> filteringIntegerList(List<Integer> list) {

        List<Integer> newList = new ArrayList<>();
        int n = list.size();
        int number;

        if(n % 2 == 0){
            for (Integer integer : list) {
                number = integer;
                if (number % 2 == 0) {
                    newList.add(number);
                }
            }
        }else{
            for (Integer integer : list) {
                number = integer;
                if (number % 2 != 0) {
                    newList.add(number);
                }
            }
        }

        return newList;
    }

    /**
     * Function for getting integer list from file input
     * @param path to wanted file
     * @return list of Integers from file
     */
    private static List<Integer> getIntegerListFromFileInput(String path) {

        List<Integer> list = new ArrayList<>();

        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);

                while (myReader.hasNextInt()) {
                    list.add(myReader.nextInt());
                }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File was not found! Try it again");
        }

        return list;

    }

    /**
     * Function for writing modified numbers to file
     * @param fileName name of file with .txt
     * @param list of modified numbers
     */
    public static void writeIntoFile(String fileName, List<Integer> list){

        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName), StandardCharsets.UTF_8));
            for (Integer integer : list) {
                out.write(integer + "\n");
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Input/Output error! Try it again");
        }
    }


}
