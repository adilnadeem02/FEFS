package com.example.fefs;
import java.util.*;

public class FEFS
{
    List<Student> students;
    List<Administrator> administrators;
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
}
