package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class RenderPlayerTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.renderer.entity.RenderPlayer"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("renderRightArm") || methodName.equals("func_177138_b")) {
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node instanceof VarInsnNode && node.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) node).var == 3 && node.getNext().getOpcode() == Opcodes.ICONST_0) {
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.remove(node.getNext());
                        methodNode.instructions.insertBefore(node, newArmLogic());
                        methodNode.instructions.remove(node);
                        break;
                    }
                }

                break;
            }
        }
    }

    // modelplayer.isRiding = modelplayer.isSneak = false;
    private InsnList newArmLogic() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RenderPlayer", "func_177087_b",
                "()Lnet/minecraft/client/model/ModelBase;", false)); // getMainModel
        list.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/client/model/ModelPlayer"));
        list.add(new VarInsnNode(Opcodes.ASTORE, 2));
        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new InsnNode(Opcodes.DUP_X1));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/model/ModelPlayer", "field_78117_n", "Z")); // isSneak
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/model/ModelPlayer", "field_78093_q", "Z")); // isRiding
        return list;
    }
}
