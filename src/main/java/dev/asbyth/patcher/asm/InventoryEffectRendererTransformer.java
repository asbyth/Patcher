package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class InventoryEffectRendererTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.InventoryEffectRenderer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("updateActivePotionEffects") || methodName.equals("func_175378_g")) {
                methodNode.instructions.clear();
                methodNode.localVariables.clear();

                methodNode.instructions.insert(newEffectLogic());
                break;
            }
        }
    }

    private InsnList newEffectLogic() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/Minecraft", "func_71410_x", // getMinecraft
                "()Lnet/minecraft/client/Minecraft;", false));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "field_71439_g", // thePlayer
                "Lnet/minecraft/client/entity/EntityPlayerSP;"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "func_70651_bq", // getActivePotionEffects
                "()Ljava/util/Collection;", false));
        list.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/Collection", "isEmpty", "()Z", true));
        LabelNode ifne = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, ifne));
        list.add(new InsnNode(Opcodes.ICONST_1));
        LabelNode gotoInsn = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.GOTO, gotoInsn));
        list.add(ifne);
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(gotoInsn);
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/InventoryEffectRenderer", "field_147045_u", "Z")); // hasActivePotionEffects
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/InventoryEffectRenderer", "field_146294_l", "I")); // width
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/renderer/InventoryEffectRenderer", "field_146999_f", "I")); // xSize
        list.add(new InsnNode(Opcodes.ISUB));
        list.add(new InsnNode(Opcodes.ICONST_2));
        list.add(new InsnNode(Opcodes.IDIV));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/renderer/InventoryEffectRenderer", "field_147003_i", "I")); // guiLeft
        list.add(new InsnNode(Opcodes.RETURN));
        return list;
    }
}
