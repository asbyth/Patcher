package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

public class ChunkTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.world.chunk.Chunk"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        List<String> brightness = Arrays.asList(
                "getLightFor", "func_177413_a",
                "getLightSubtracted", "func_177443_a"
        );

        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);
            if (brightness.contains(methodName)) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), setBrightness());
                break;
            }
        }
    }

    private InsnList setBrightness() {
        InsnList list = new InsnList();
        list.add(new IntInsnNode(Opcodes.BIPUSH, 15));
        list.add(new InsnNode(Opcodes.IRETURN));
        return list;
    }
}
