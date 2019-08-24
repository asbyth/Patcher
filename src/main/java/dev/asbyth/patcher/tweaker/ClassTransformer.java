package dev.asbyth.patcher.tweaker;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.asbyth.patcher.Patcher;
import dev.asbyth.patcher.asm.*;
import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.FileOutputStream;
import java.util.Collection;

public class ClassTransformer implements IClassTransformer {

    private final Multimap<String, ITransformer> transformerMap = ArrayListMultimap.create();

    public ClassTransformer() {
        registerTransformer(new EntityLivingBaseTransformer());
        registerTransformer(new WorldTransformer());
        registerTransformer(new ScoreboardTransformer());
        registerTransformer(new S2EPacketCloseWindowTransformer());
        registerTransformer(new GuiGameOverTransformer());
        registerTransformer(new MinecraftTransformer());
        registerTransformer(new RenderPlayerTransformer());
    }

    private void registerTransformer(ITransformer transformer) {
        for (String cls : transformer.getClassName()) {
            transformerMap.put(cls, transformer);
        }
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null) return null;

        Collection<ITransformer> transformers = transformerMap.get(transformedName);
        if (transformers.isEmpty()) return bytes;

        Patcher.LOGGER.info("Found {} transformers for {}", transformers.size(), transformedName);

        ClassReader reader = new ClassReader(bytes);
        ClassNode node = new ClassNode();
        reader.accept(node, ClassReader.EXPAND_FRAMES);

        transformers.forEach(transformer -> {
            Patcher.LOGGER.info("Applying transformer {} on {}...", transformer.getClass().getName(), transformedName);
            transformer.transform(node, transformedName);
        });

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        try {
            node.accept(writer);
        } catch (Throwable t) {
            Patcher.LOGGER.error("Exception when transforming " + transformedName + " : " + t.getClass().getSimpleName());
            t.printStackTrace();
        }

        return writer.toByteArray();
    }
}
