package com.example.fefs;
import java.util.*;

public class FEFS
{
    List<Student> students;
    List<Admin> administrators;
    List<Faculty> faculty;
    private static FEFS fefs = null;

    private FEFS()
    {
        students = new ArrayList<>();
        administrators = new ArrayList<>();
        faculty = new ArrayList<>();
    }

    public static FEFS getInstance()
    {
        if (fefs == null)
            fefs = new FEFS();
        return fefs;
    }

    public boolean login(String u, String p, String r)
    {
        Database db = Database.getInstance();
        return db.signIn(u, p, r);
    }
}
