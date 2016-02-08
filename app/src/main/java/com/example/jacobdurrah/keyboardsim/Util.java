package com.example.jacobdurrah.keyboardsim;

/**
 * Created by jacobdurrah on 2/8/16.
 */
public class Util {

 static   public String getNewCharacter(String oldString, String newString)
    {
        boolean difference = false;
        String addedChar = "";
        if(oldString.compareTo(newString)<0)
        {
            if(oldString.length() == 0)
            {
                addedChar = newString;
            }
            else if(oldString.length() > newString.length())
            {
                addedChar = "\b";
            }
            else
            {
                addedChar = newString.substring(newString.length()-1);
            }
        }
        return addedChar;
    }
}
