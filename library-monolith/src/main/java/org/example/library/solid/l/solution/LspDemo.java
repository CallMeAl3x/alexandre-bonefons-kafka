package org.example.library.solid.l.solution;

import java.util.List;

/**
 * Point d'entrée public pour démontrer le LSP depuis l'extérieur du package.
 */
public class LspDemo {

    public static void run() {
        System.out.println("\n--- PRINCIPLE 3 : LSP ---");
        System.out.println("\n[❌ Problème] Ostrich.fly() lève UnsupportedOperationException");
        System.out.println("  → Code client cassé s'il appelle bird.fly() sur une Ostrich");

        System.out.println("\n[✅ Solution] Hiérarchie d'interfaces distinctes");

        // Tous les oiseaux peuvent manger (contrat Bird homogène)
        List<Bird> allBirds = List.of(new Sparrow(), new Ostrich(), new Eagle());
        System.out.println("Tous les oiseaux mangent :");
        allBirds.forEach(Bird::eat);

        // Seulement les oiseaux volants (FlyingBird) peuvent voler
        List<FlyingBird> flyingBirds = List.of(new Sparrow(), new Eagle());
        System.out.println("\nOiseaux capables de voler :");
        flyingBirds.forEach(FlyingBird::fly);

        // Substitutabilité : Sparrow et Eagle sont utilisables partout où Bird est attendu
        System.out.println("\nSubstituabilité démontrée :");
        feedBird(new Sparrow());
        feedBird(new Ostrich()); // ✅ fonctionne sans exception
        feedBird(new Eagle());

        System.out.println("\n→ RÉPONSE : Les interfaces résolvent le LSP car elles permettent de " +
                "composer les comportements sans forcer une classe à implémenter ce qu'elle " +
                "ne peut pas faire. Ostrich n'implémente pas FlyingBird → jamais d'exception.");
    }

    // Méthode qui accepte n'importe quel Bird — substituabilité garantie
    private static void feedBird(Bird bird) {
        System.out.println("  Feeding " + bird.getName() + "...");
        bird.eat(); // ✅ fonctionne pour tous sans exception
    }
}
