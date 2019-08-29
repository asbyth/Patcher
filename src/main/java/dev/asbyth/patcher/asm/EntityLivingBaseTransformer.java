package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class EntityLivingBaseTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.entity.EntityLivingBase"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("getLook") || methodName.equals("func_70676_i")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), returnIfPlayer());
                break;
            }
        }
    }

    /**
     * if (this instanceof EntityPlayerSP) {
     *      return super.getLook();
     * }
     *
     * at HEAD
     */
    private InsnList returnIfPlayer() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new TypeInsnNode(Opcodes.INSTANCEOF, "net/minecraft/client/entity/EntityPlayerSP"));
        LabelNode labelNode = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.FLOAD, 1));
        list.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/entity/Entity", "func_70676_i", "(F)Lnet/minecraft/util/Vec3;", false)); // getLook
        list.add(new InsnNode(Opcodes.ARETURN));
        list.add(labelNode);
        return list;
    }
}
