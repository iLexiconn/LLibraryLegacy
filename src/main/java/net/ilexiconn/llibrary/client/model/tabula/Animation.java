package net.ilexiconn.llibrary.client.model.tabula;

import com.google.common.collect.Ordering;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Container for Tabula animations.
 *
 * @author Gegy1000
 * @since 0.1.0
 */
public class Animation {
    public String name;
    public String identifier;

    public boolean loops;

    public TreeMap<String, ArrayList<AnimationComponent>> sets = new TreeMap<String, ArrayList<AnimationComponent>>(Ordering.natural()); // cube identifier to animation component
}
