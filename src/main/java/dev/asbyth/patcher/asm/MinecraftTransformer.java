package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

public class MinecraftTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.Minecraft"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);
            String methodDesc = mapMethodDesc(methodNode);
            if ((methodName.equals("loadWorld") || methodName.equals("func_71353_a")) && methodDesc.equals("(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V")) {
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();

                    if (node instanceof MethodInsnNode && node.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) node).owner.equals("java/lang/System")) {
                        methodNode.instructions.insertBefore(node, setSystemTime());
                        break;
                    }
                }

                break;
            }

            if (methodName.equals("toggleFullscreen") || methodName.equals("func_71352_k")) {
                ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();

                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node instanceof MethodInsnNode && node.getOpcode() == Opcodes.INVOKESTATIC && ((MethodInsnNode) node).name.equals("setFullscreen")) {
                        methodNode.instructions.insert(node, setResizable());
                        break;
                    }
                }

                break;
            }
        }
    }

    private InsnList setResizable() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "dev/asbyth/patcher/config/Settings", "FULLSCREEN", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/Display", "setResizable", "(Z)V", false));
        list.add(new InsnNode(Opcodes.ICONST_1));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/Display", "setResizable", "(Z)V", false));
        list.add(ifeq);
        return list;
    }

    // systemTime = 0 before System.gc() in loadWorld (near RETURN)
    private InsnList setSystemTime() {
        InsnList list = new InsnList();
        list.add(new FieldInsnNode(Opcodes.GETSTATIC, "dev/asbyth/patcher/config/Settings", "WORLD_SWITCHING", "Z"));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFEQ, ifeq));
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new InsnNode(Opcodes.LCONST_0));
        list.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/Minecraft", "field_71423_H", "J")); // systemTime
        list.add(new InsnNode(Opcodes.RETURN));
        list.add(ifeq);
        return list;
    }
}
