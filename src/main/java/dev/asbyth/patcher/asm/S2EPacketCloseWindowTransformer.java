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

    private InsnList checkScreen() {
        InsnList list = new InsnList();
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "func_71410_x",
                "()Lnet/minecraft/client/Minecraft;", false)); // getMinecraft
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "field_71462_r", "Lnet/minecraft/client/gui/GuiScreen;")); // currentScreen
        list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/client/gui/GuiChat"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);
        return list;
    }
}
