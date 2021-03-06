package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class GuiGameOverTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.gui.GuiGameOver"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("initGui") || methodName.equals("func_73866_w_")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), initializeEnableButtonsTimer());
                break;
            }
        }
    }

    // enableButtonsTimer = 0 at HEAD
    private InsnList initializeEnableButtonsTimer() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "dev/asbyth/patcher/config/Settings", "DEATH_SCREEN", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiGameOver", "field_146347_a", "I")); // enableButtonsTimer
        list.add(ifeq);
        return list;
    }
}
