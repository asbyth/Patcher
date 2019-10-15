package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class EntityPlayerSPTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.entity.EntityPlayerSP"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        MethodNode removePotionEffectClient = new MethodNode(Opcodes.ACC_PUBLIC, "removePotionEffectClient", "(I)V", null, null);
        removePotionEffectClient.instructions.add(removePotionEffectClientMethod());
        classNode.methods.add(removePotionEffectClient);
    }

    private InsnList removePotionEffectClientMethod() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "dev/asbyth/patcher/config/Settings", "NAUSEA_EFFECT", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new VarInsnNode(Opcodes.ILOAD, 1));
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/potion/Potion", "field_76431_k", "Lnet/minecraft/potion/Potion;")); // confusion
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/potion/Potion", "field_76415_H", "I")); // id
        LabelNode ificmpne = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IF_ICMPNE, ificmpne));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.FCONST_0));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", "field_71080_cy", "F")); // prevTimeInPortal
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.FCONST_0));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", "field_71086_bY", "F")); // timeInPortal
        list.add(ifeq);
        list.add(ificmpne);
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ILOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/client/entity/AbstractClientPlayer", "func_70618_n", // removePotionEffectClient
                "(I)V", false));
        list.add(new InsnNode(Opcodes.RETURN));
        return list;
    }
}
