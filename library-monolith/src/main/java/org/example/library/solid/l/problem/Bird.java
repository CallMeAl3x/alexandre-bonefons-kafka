package org.example.library.solid.l.problem;

/**
 * ❌ PROBLÈME LSP : Bird force tous les oiseaux à implémenter fly().
 * L'Autruche ne peut pas voler → elle lève une exception.
 * Ostrich n'est donc PAS substituable à Bird : violation du LSP.
 */
public abstract class Bird {
    public abstract void fly();
    public abstract void eat();
}

class Sparrow extends Bird {
    @Override public void fly() { System.out.println("Sparrow is flying"); }
    @Override public void eat() { System.out.println("Sparrow is eating seeds"); }
}

class Ostrich extends Bird {
    @Override public void fly() {
        throw new UnsupportedOperationException("Ostrich cannot fly!"); // ❌ LSP
    }
    @Override public void eat() { System.out.println("Ostrich is eating plants"); }
}
