package com.pldprojects.myinboxfsg.Fragments.Class;

import java.util.ArrayList;

public class Util {

    public boolean Valida(ArrayList<String> values) {
        for (var obj : values)
        {
            if(obj.equals(""))
            {return false;}
        }
        return true;
    }
}
