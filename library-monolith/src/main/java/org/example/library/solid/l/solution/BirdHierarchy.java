package org.example.library.solid.l.solution;

/**
 * ✅ LSP — Hiérarchie d'interfaces correcte.
 *
 * On sépare les comportements en interfaces distinctes.
 * Chaque classe n'implémente que ce qu'elle EST CAPABLE de faire.
 *
 * RÉPONSE : Les interfaces résolvent le problème car elles permettent
 * de composer les comportements. Un oiseau volant implémente Bird +
 * FlyingBird. Un oiseau non-volant n'implémente que Bird.
 * Le code client qui travaille avec Bird peut utiliser Sparrow ET
 * Ostrich sans risque d'exception → LSP respecté.
 */

// Contrat de base : tout oiseau peut manger
interface Bird {
    void eat();
    String getName();
}

// Extension optionnelle : seulement les oiseaux capables de voler
interface FlyingBird extends Bird {
    void fly();
}

// ✅ Moineau : vole ET mange — implémente les deux contrats
class Sparrow implements FlyingBird {
    @Override public void fly()  { System.out.println("Sparrow is flying"); }
    @Override public void eat()  { System.out.println("Sparrow is eating seeds"); }
    @Override public String getName() { return "Sparrow"; }
}

// ✅ Autruche : mange seulement — n'implémente PAS FlyingBird
class Ostrich implements Bird {
    @Override public void eat()  { System.out.println("Ostrich is eating plants"); }
    @Override public String getName() { return "Ostrich"; }
}

// ✅ Aigle : vole ET mange
class Eagle implements FlyingBird {
    @Override public void fly()  { System.out.println("Eagle is soaring high"); }
    @Override public void eat()  { System.out.println("Eagle is eating fish"); }
    @Override public String getName() { return "Eagle"; }
}
