package net.ilexiconn.llibrary.client.model.tabula;

import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.TreeMap;

public class Animation
{
    public String name;
    public String identifier;

    public boolean loops;

    public TreeMap<String, ArrayList<AnimationComponent>> sets = new TreeMap<String, ArrayList<AnimationComponent>>(Ordering.natural()); // cube identifier to animation component
}

