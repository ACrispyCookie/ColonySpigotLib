package net.colonymc.api.itemstacks;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.BaseEncoding;

public class Serializer {
	
	public static String serializeItemStack(ItemStack itemStack){
        if(itemStack == null) return "null";
       
        ByteArrayOutputStream outputStream = null;
        try{
            Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
            Constructor<?> nbtTagCompoundConstructor = nbtTagCompoundClass.getConstructor();
            Object nbtTagCompound = nbtTagCompoundConstructor.newInstance();
            Object nmsItemStack = getOBClass("inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
            getNMSClass("ItemStack").getMethod("save", nbtTagCompoundClass).invoke(nmsItemStack, nbtTagCompound);
            outputStream = new ByteArrayOutputStream();
            getNMSClass("NBTCompressedStreamTools").getMethod("a", nbtTagCompoundClass, OutputStream.class).invoke(null, nbtTagCompound, outputStream);
        }catch(SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
            e.printStackTrace();
        }
       
        return BaseEncoding.base64().encode(outputStream.toByteArray());
    }
	
	public static ItemStack deserializeItemStack(String itemStackString){
        if(itemStackString.equals("null")) return null;
       
        ByteArrayInputStream inputStream = new ByteArrayInputStream(BaseEncoding.base64().decode(itemStackString));
       
        Class<?> nbtTagCompoundClass = getNMSClass("NBTTagCompound");
        Class<?> nmsItemStackClass = getNMSClass("ItemStack");
        Object nbtTagCompound = null;
        ItemStack itemStack = null;
        try{
            nbtTagCompound = getNMSClass("NBTCompressedStreamTools").getMethod("a", InputStream.class).invoke(null, inputStream);
            Object craftItemStack = nmsItemStackClass.getMethod("createStack", nbtTagCompoundClass).invoke(null, nbtTagCompound);
            itemStack = (ItemStack) getOBClass("inventory.CraftItemStack").getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, craftItemStack);
        }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e){
            e.printStackTrace();
        }
       
        return itemStack;
    }
	
	public static Class<?> getNMSClass(String className){
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "net.minecraft.server." + version + "." + className;
        Class<?> nmsClass = null;
        try{
            nmsClass = Class.forName(classLocation);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }


   
    public static Class<?> getOBClass(String className){
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.replace(".", ",").split(",")[3];
        String classLocation = "org.bukkit.craftbukkit." + version + "." + className;
        Class<?> nmsClass = null;
        try{
            nmsClass = Class.forName(classLocation);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.println("Unable to find reflection class " + classLocation + "!");
        }
        return nmsClass;
    }

}
