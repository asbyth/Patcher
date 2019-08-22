package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class EntityPlayerSPTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.entity.EntityPlayerSP"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("removePotionEffectClient")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), removeNauseaPortalEffect());
            }
        }
    }

    private InsnList removeNauseaPortalEffect() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(ILOAD, 1));
        list.add(new FieldInsnNode(GETSTATIC, "net/minecraft/potion/Potion", "confusion", "Lnet/minecraft/potion/Potion;"));
        list.add(new FieldInsnNode(GETFIELD, "net/minecraft/potion/Potion", "id", "I"));
        LabelNode labelNode = new LabelNode();
        list.add(new JumpInsnNode(IF_ICMPNE, labelNode));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new InsnNode(FCONST_0));
        list.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", "prevTimeInPortal", "F"));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new InsnNode(FCONST_0));
        list.add(new FieldInsnNode(PUTFIELD, "net/minecraft/client/entity/EntityPlayerSP", "timeInPortal", "F"));
        list.add(labelNode);
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ILOAD, 1));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "removePotionEffect", "(I)V", false));
        list.add(new InsnNode(RETURN));
        return list;
    }
}
