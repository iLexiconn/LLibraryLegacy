package net.ilexiconn.llibrary.client.book;

import com.google.common.collect.Lists;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author iLexiconn
 */
@SideOnly(Side.CLIENT)
public class BookWikiAPI {
    private static List<IComponent> componentList = Lists.newArrayList();
    private static List<IRecipe> recipeList = Lists.newArrayList();

    public static void registerComponent(IComponent component) {
        for (IComponent c : componentList) {
            if (c.getID() == component.getID()) {
                throw new RuntimeException("Already a component with ID " + c.getID() + " registered! (" + c.toString() + " and " + component.toString() + ")");
            }
        }
        componentList.add(component);
    }

    public static IComponent[] getComponents() {
        return componentList.toArray(new IComponent[componentList.size()]);
    }

    public static void registerRecipe(IRecipe recipe) {
        for (IRecipe r : recipeList) {
            if (r.getType().equals(recipe.getType())) {
                throw new RuntimeException("Already a recipe with type " + r.getType() + " registered! (" + r.toString() + " and " + recipe.toString() + ")");
            }
        }
        recipeList.add(recipe);
    }

    public static IRecipe[] getRecipes() {
        return recipeList.toArray(new IRecipe[recipeList.size()]);
    }

    public static IRecipe getRecipeByType(String type) {
        for (IRecipe recipe : recipeList) {
            if (recipe.getType().equals(type)) {
                return recipe;
            }
        }
        return null;
    }
}
