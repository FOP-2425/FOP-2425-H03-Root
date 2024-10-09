package h03.mock;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

import java.util.function.Predicate;

public class HackingRobotClassTransformer implements ClassTransformer {

    private static boolean useMock;

    @Override
    public String getName() {
        return "HackingRobotClassTransformer";
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if (reader.getClassName().equals("h03/robots/HackingRobot")) {
            reader.accept(new HackingRobotClassVisitor(writer), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } else {
            reader.accept(writer, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }

    public static void useMock(boolean useMock) {
        HackingRobotClassTransformer.useMock = useMock;
    }

    public static boolean getUseMock() {
        return useMock;
    }

    private static class HackingRobotClassVisitor extends ClassVisitor {

        protected HackingRobotClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM9, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.equals("getType") && descriptor.equals("()Lh03/robots/MovementType;") ||
                name.equals("getNextType") && descriptor.equals("()Lh03/robots/MovementType;") ||
                name.equals("getRandom") && descriptor.equals("(I)I") ||
                (name.equals("shuffle") && descriptor.equals("(I)Z") || descriptor.equals("()V"))) {
                Predicate<String> returnsMovementType = s -> s.equals("getType") || s.equals("getNextType");
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitCode() {
                        Label studentCode = new Label();
                        super.visitMethodInsn(Opcodes.INVOKESTATIC,
                            "h03/mock/HackingRobotClassTransformer",
                            "getUseMock",
                            "()Z",
                            false);
                        super.visitJumpInsn(Opcodes.IFEQ, studentCode);
                        super.visitMethodInsn(Opcodes.INVOKESTATIC,
                            "h03/mock/HackingRobotMock",
                            "getInstance",
                            "()Lh03/mock/HackingRobotMock;",
                            false);
                        Type[] argTypes = Type.getArgumentTypes(descriptor);
                        for (int i = 0; i < argTypes.length; i++) {
                            super.visitVarInsn(argTypes[i].getOpcode(Opcodes.ILOAD), i + 1);
                        }
                        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "h03/mock/HackingRobotMock",
                            name,
                            returnsMovementType.test(name) ?
                                "()Ljava/lang/String;" :
                                descriptor,
                            false);
                        Type returnType = Type.getReturnType(descriptor);
                        if (returnType != Type.VOID_TYPE) {
                            if (returnsMovementType.test(name)) {
                                super.visitMethodInsn(Opcodes.INVOKESTATIC,
                                    "h03/robots/MovementType",
                                    "valueOf",
                                    "(Ljava/lang/String;)Lh03/robots/MovementType;",
                                    false);
                            }
                            super.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
                        } else {
                            super.visitInsn(Opcodes.RETURN);
                        }
                        super.visitLabel(studentCode);
                        super.visitCode();
                    }
                };
            } else {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }
    }
}
