package com.example.fefs;
import java.sql.ResultSet;
import java.util.*;

public class FEFS
{
    private static FEFS fefs = null;

    private FEFS()
    {
    }

    public static FEFS getInstance()
    {
        if (fefs == null)
            fefs = new FEFS();
        return fefs;
    }

    public static void submitFeedback(int instructorID, int courseID, int i, String text)
    {
        Database.getInstance().submitFeedback(instructorID, courseID, i ,text);
    }

    public boolean login(String u, String p, String r)
    {
        Database db = Database.getInstance();
        return db.signIn(u, p, r);
    }

    public List<String[]> returnCourses()
    {
        return Database.getInstance().returnCourses();
    }

    public List<String[]> getInstructors(String c)
    {
        return Database.getInstance().returnInstructors(c);
    }

    public int getInstructorID(String u, String p, String r)
    {
        return Database.getInstance().getInstructorID(u, p, r);
    }

    public static List<String[]> getFeedback(int user_id)
    {
        return Database.getInstance().getFeedback(user_id);
    }

    public boolean addFaculty(String f_nameText, String f_emailText, String f_passText, String f_depart)
    {
        return Database.getInstance().addFaculty(f_nameText, f_emailText, f_passText,f_depart);
    }

    public boolean remFaculty(String text, String text1, String text2, String text3)
    {
        return Database.getInstance().remFaculty(text, text1, text2, text3);
    }

    public boolean addStudent(String text, String text1, String text2, String text3)
    {
        return Database.getInstance().addStudent(text, text1, text2, text3);
    }

    public boolean remStudent(String text, String text1, String text2, String text3)
    {
        return Database.getInstance().remStudent(text, text1, text2, text3);
    }
}
