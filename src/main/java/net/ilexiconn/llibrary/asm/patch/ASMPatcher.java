package net.ilexiconn.llibrary.asm.patch;

import com.cloudbees.diff.ContextualPatch;
import com.cloudbees.diff.PatchException;
import net.ilexiconn.llibrary.LLibrary;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author gegy1000
 *
 * Feed it a patch file, and it automatically does the ASM
 */
public class ASMPatcher
{
    private String name, deobfName;
    private String patchFile;
    private byte[] originalBytes;

    public ASMPatcher(String name, String deobfName, String patchResource, byte[] orginalBytes)
    {
        this.name = name;
        this.deobfName = deobfName;
        this.patchFile = patchResource;
        this.originalBytes = orginalBytes;
    }

    public boolean isObfuscated()
    {
        return !name.equals(deobfName);
    }

    public byte[] patch() throws IOException, ClassNotFoundException, PatchException
    {
        File originalTemp = createTempDirectory("original_patch");
        File patchFile = createTempDirectory("patch_file");

        FileOutputStream originalOut = new FileOutputStream(originalTemp);
        originalOut.write(originalBytes);
        originalOut.close();

        InputStream patchFileIn = LLibrary.class.getResourceAsStream(this.patchFile);

        FileOutputStream newOut = new FileOutputStream(patchFile);
        byte[] patchBytes = IOUtils.toByteArray(patchFileIn);
        newOut.write(patchBytes);

        ContextualPatch contextualPatch = ContextualPatch.create(patchFile, originalTemp);
        contextualPatch.patch(true);

        byte[] patchedBytes = IOUtils.toByteArray(new FileInputStream(originalTemp));

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(patchedBytes);
        classReader.accept(classNode, 0);

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }

    private File createTempDirectory(String name) throws IOException
    {
        File temp = File.createTempFile("temp_" + name, Long.toString(System.nanoTime()));

        if (!(temp.delete()))
        {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir()))
        {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }
}
