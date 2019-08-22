package dev.asbyth.patcher.asm;

import dev.asbyth.patcher.tweaker.transformer.ITransformer;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

public class ScoreboardTransformer implements ITransformer {

    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.scoreboard.Scoreboard"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode methodNode : classNode.methods) {
            String methodName = mapMethodName(classNode, methodNode);

            if (methodName.equals("removeTeam") || methodName.equals("func_147194_f")) {
                methodNode.instructions.clear();
                methodNode.instructions.add(fixScoreboardNPE());
                break;
            }
        }
    }

    private InsnList fixScoreboardNPE() {
        InsnList list = new InsnList();
        list.add(new VarInsnNode(ALOAD, 1));
        LabelNode ifNonNull = new LabelNode();
        list.add(new JumpInsnNode(IFNONNULL, ifNonNull));
        list.add(new InsnNode(RETURN));
        list.add(ifNonNull);

        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/scoreboard/ScorePlayerTeam", "func_96661_b", "()Ljava/lang/String;", false)); // getRegisteredName
        LabelNode ifNull = new LabelNode();
        list.add(new JumpInsnNode(IFNULL, ifNull));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "net/minecraft/scoreboard/Scoreboard", "field_96542_e", "Ljava/util/Map;")); // teams
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/scoreboard/ScorePlayerTeam", "func_96661_b", "()Ljava/lang/String;", false)); // getRegisteredName
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        list.add(new InsnNode(POP));
        list.add(ifNull);

        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/scoreboard/ScorePlayerTeam", "func_96670_d", "()Ljava/util/Collection;", false)); // getMembershipCollection
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Collection", "iterator", "()Ljava/util/Iterator;", true));
        list.add(new VarInsnNode(ASTORE, 2));
        list.add(new VarInsnNode(ALOAD, 2));
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true));
        LabelNode ifeq = new LabelNode();
        list.add(new JumpInsnNode(IFEQ, ifeq));
        list.add(new VarInsnNode(ALOAD, 2));
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true));
        list.add(new TypeInsnNode(CHECKCAST, "java/lang/String"));
        list.add(new VarInsnNode(ASTORE, 3));
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new FieldInsnNode(GETFIELD, "net/minecraft/scoreboard/Scoreboard", "field_96540_f", "Ljava/util/Map;")); // teamMemberships
        list.add(new VarInsnNode(ALOAD, 3));
        list.add(new MethodInsnNode(INVOKEINTERFACE, "java/util/Map", "remove", "(Ljava/lang/Object;)Ljava/lang/Object;", true));
        list.add(new InsnNode(POP));
        LabelNode gotoNode = new LabelNode();
        list.add(new JumpInsnNode(GOTO, gotoNode));
        list.add(ifeq);
        list.add(gotoNode);

        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new VarInsnNode(ALOAD, 1));
        list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/scoreboard/Scoreboard", "func_96513_c",
                "(Lnet/minecraft/scoreboard/ScorePlayerTeam;)V", false)); // no mcp name
        list.add(new InsnNode(RETURN));
        return list;
    }
}
