package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class ScreenShotHelperTransformer implements ITransformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.util.ScreenShotHelper"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);
            String methodDesc = mapMethodDesc(methodNode);

            if ((methodName.equals("saveScreenshot")) && methodDesc.equals("(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;")) {
                methodNode.instructions.clear();
                methodNode.tryCatchBlocks.clear();
                methodNode.localVariables.clear();
                methodNode.exceptions.clear();

                methodNode.instructions.add(betterScreenshots());
                break;
            }
        }
    }

    private InsnList betterScreenshots() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new VarInsnNode(Opcodes.ILOAD, 2));
        list.add(new VarInsnNode(Opcodes.ILOAD, 3));
        list.add(new VarInsnNode(Opcodes.ALOAD, 4));
        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "dev/asbyth/patcher/asm/helpers/ScreenShotHelperHook",
                "saveScreenshot", "(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;",
                false));
        list.add(new InsnNode(Opcodes.ARETURN));
        return list;
    }
}
