package dev.asbyth.patcher.asm.forge;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

// this is a forge issue, not a minecraft issue, so it gets its own package
// thanks to prplz for the original code
public class ClientCommandHandlerTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraftforge.client.ClientCommandHandler"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("executeCommand") || methodName.equals("func_71556_a")) {
                methodNode.instructions.insertBefore(methodNode.instructions.getFirst(), checkCommandString());
                break;
            }
        }
    }

    private InsnList checkCommandString() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 2));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;", false));
        list.add(new LdcInsnNode("/"));
        list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "startsWith", "(Ljava/lang/String;)Z", false));
        LabelNode ifne = new LabelNode();
        list.add(new JumpInsnNode(Opcodes.IFNE, ifne));
        list.add(new InsnNode(Opcodes.ICONST_0));
        list.add(new InsnNode(Opcodes.IRETURN));
        list.add(ifne);
        return list;
    }
}
