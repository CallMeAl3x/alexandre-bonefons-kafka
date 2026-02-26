package org.example.library.solid;

import org.example.library.solid.d.solution.DipDemo;
import org.example.library.solid.i.solution.IspDemo;
import org.example.library.solid.l.solution.LspDemo;
import org.example.library.solid.o.solution.*;
import org.example.library.solid.s.solution.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Démonstration de l'Exercice 2 : les 5 principes SOLID.
 * S'exécute après PatternsDemo (order = 2).
 */
@Component
@Order(2)
public class SolidDemo implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("\n========================================================");
        System.out.println("  EXERCICE 2 — SOLID PRINCIPLES");
        System.out.println("========================================================");

        demoSrp();
        demoOcp();
        LspDemo.run();
        IspDemo.run();
        DipDemo.run();

        System.out.println("\n========================================================");
        System.out.println("  FIN EXERCICE 2");
        System.out.println("========================================================\n");
    }

    // =========================================================================
    // S — Single Responsibility Principle
    // =========================================================================
    private void demoSrp() {
        System.out.println("\n--- PRINCIPLE 1 : SRP ---");
        System.out.println("\n[❌ Problème] UserManager : 4 responsabilités dans une seule classe");

        System.out.println("\n[✅ Solution] 4 classes spécialisées + orchestrateur");

        UserValidator  validator  = new UserValidator();
        UserRepository repository = new UserRepository();
        UserNotifier   notifier   = new UserNotifier();
        UserStore      store      = new UserStore();
        UserRegistrationService service = new UserRegistrationService(
                validator, repository, notifier, store
        );

        System.out.println("\n[Test] Enregistrement valide :");
        boolean ok = service.register("alice", "alice@example.com");
        System.out.println("  Résultat : " + (ok ? "✅ succès" : "❌ échec"));

        System.out.println("\n[Test] Username trop court (< 3 chars) :");
        boolean ko1 = service.register("ab", "ab@example.com");
        System.out.println("  Résultat : " + (ko1 ? "✅ succès" : "❌ rejeté"));

        System.out.println("\n[Test] Email invalide :");
        boolean ko2 = service.register("bob", "not-an-email");
        System.out.println("  Résultat : " + (ko2 ? "✅ succès" : "❌ rejeté"));

        System.out.println("\n  UserStore contient : " + store.getAll());

        System.out.println("\n→ RÉPONSE : C'est plus facile à tester car chaque classe peut " +
                "être testée/mockée indépendamment. On valide UserValidator sans DB, " +
                "on teste UserRepository sans notifier, etc.");
    }

    // =========================================================================
    // O — Open/Closed Principle
    // =========================================================================
    private void demoOcp() {
        System.out.println("\n--- PRINCIPLE 2 : OCP ---");
        System.out.println("\n[❌ Problème] BookRater avec switch → modifier la classe pour chaque nouveau type");

        System.out.println("\n[✅ Solution] Map de stratégies → BookRater ne change jamais");

        BookRater rater = new BookRater(List.of(
                new FictionRatingStrategy(),
                new ScienceRatingStrategy(),
                new HistoryRatingStrategy()
        ));

        System.out.println("fiction  (20 avis) : " + rater.calculateRating("fiction",  20));
        System.out.println("science  (30 avis) : " + rater.calculateRating("science",  30));
        System.out.println("history  (10 avis) : " + rater.calculateRating("history",  10));

        // ✅ Ajout d'un nouveau type SANS modifier BookRater
        System.out.println("\n[Extension] Ajout du type 'biography' sans toucher à BookRater :");
        rater.register(new RatingStrategy() {
            @Override public double calculate(int reviewCount) {
                return Math.min(5, reviewCount * 0.12 + 1.8);
            }
            @Override public String getBookType() { return "biography"; }
        });

        System.out.println("biography (15 avis) : " + rater.calculateRating("biography", 15));

        System.out.println("\n→ RÉPONSE : Les stratégies rendent la classe OCP : BookRater est " +
                "fermée à la modification (on ne la touche jamais) et ouverte à l'extension " +
                "(on crée une nouvelle RatingStrategy et on l'enregistre).");
    }
}
