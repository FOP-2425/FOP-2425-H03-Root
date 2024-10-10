package h03.mock;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

public class RobotsChallengeClassTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return "RobotsChallengeClassTransformer";
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        if (reader.getClassName().equals("h03/RobotsChallenge")) {
            reader.accept(new RobotsChallengeClassVisitor(writer), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } else {
            reader.accept(writer, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }

    private static class RobotsChallengeClassVisitor extends ClassVisitor {

        protected RobotsChallengeClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM9, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            if (name.equals("findWinners") && descriptor.equals("()[Lh03/robots/DoublePowerRobot;")) {
                return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        if (opcode == Opcodes.INVOKESTATIC &&
                            owner.equals("java/lang/Math") &&
                            name.equals("min")) {
                            super.visitMethodInsn(opcode, "h03/mock/RobotsChallengeAuxMock", name, descriptor, isInterface);
                        } else {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    }
                };
            } else {
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        }
    }
}
