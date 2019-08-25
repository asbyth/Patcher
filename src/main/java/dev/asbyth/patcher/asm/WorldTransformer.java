package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

public class WorldTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.world.World"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        List<String> brightness = Arrays.asList(
                "checkLightFor", "func_180500_c",
                "getLightFromNeighborsFor", "func_175671_l",
                "getLightFromNeighbors", "func_175705_a",
                "getRawLight", "func_175638_a",
                "getLight", "func_175699_k", "func_175721_c"
        );

        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("getHorizon") || methodName.equals("func_72919_O")) {
                methodNode.instructions.clear();
                methodNode.instructions.add(returnDouble());
                break;
            }

            if (brightness.contains(methodName)) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), setBrightness());
                break;
            }
        }
    }

    private InsnList setBrightness() {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "func_71410_x",
                "()Lnet/minecraft/client/Minecraft;", false)); // getMinecraft
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/Minecraft", "func_152345_ab",
                "()Z", false)); // isCallingFromMinecraftThread
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new IntInsnNode(Opcodes.BIPUSH, 15));
        list.add(new InsnNode(Opcodes.IRETURN));
        list.add(ifeq);
        return list;
    }

    /**
     * return 0.0D;
     */
    private InsnList returnDouble() {
        InsnList list = new InsnList();
        list.add(new InsnNode(Opcodes.DCONST_0));
        list.add(new InsnNode(Opcodes.DRETURN));
        return list;
    }
}
