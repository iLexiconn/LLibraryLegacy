package net.ilexiconn.llibrary.common.asm.hook;

public class Hook {
    private MethodSignature signature;

    private boolean includeCallerInInvocation;

    private String callee;

    private Bait bait;

    public Hook(MethodSignature signature, boolean includeCallerInInvocation, String callee, Bait bait) {
        this.signature = signature;
        this.includeCallerInInvocation = includeCallerInInvocation;
        this.callee = callee;
        this.bait = bait;
    }
}
