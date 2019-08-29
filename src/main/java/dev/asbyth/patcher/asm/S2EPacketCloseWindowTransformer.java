package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class S2EPacketCloseWindowTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.network.play.server.S2EPacketCloseWindow"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("processPacket") || methodName.equals("func_148833_a")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), checkScreen());
                break;
            }
        }
    }

    /**
     * if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
     *     return;
     * }
     */
    private InsnList checkScreen() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "dev/asbyth/patcher/config/Settings", "CLOSING_CHAT", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "func_71410_x", // getMinecraft
                "()Lnet/minecraft/client/Minecraft;", false));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "field_71462_r", "Lnet/minecraft/client/gui/GuiScreen;")); // currentScreen
        list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/client/gui/GuiChat"));
        LabelNode ifeq1 = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq1));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);
        list.add(ifeq1);
        return list;
    }
}
