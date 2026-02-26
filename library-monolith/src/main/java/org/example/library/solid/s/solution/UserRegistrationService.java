package org.example.library.solid.s.solution;

/**
 * ✅ SRP — Orchestrateur : coordonne les 4 classes spécialisées.
 *
 * UserRegistrationService n'a qu'une seule responsabilité :
 * orchestrer le flux d'enregistrement d'un utilisateur.
 * Il délègue chaque tâche à la classe qui en est responsable.
 *
 * RÉPONSE : C'est plus facile à tester car chaque classe peut être
 * testée/mockée indépendamment. On peut tester la validation sans
 * toucher à la DB, ou simuler la DB sans vrai email, etc.
 */
public class UserRegistrationService {

    private final UserValidator  validator;
    private final UserRepository repository;
    private final UserNotifier   notifier;
    private final UserStore      store;

    public UserRegistrationService(UserValidator validator,
                                   UserRepository repository,
                                   UserNotifier notifier,
                                   UserStore store) {
        this.validator  = validator;
        this.repository = repository;
        this.notifier   = notifier;
        this.store      = store;
    }

    public boolean register(String username, String email) {
        if (!validator.isValidUsername(username)) {
            System.out.println("[SRP] Username invalide : " + username);
            return false;
        }
        if (!validator.isValidEmail(email)) {
            System.out.println("[SRP] Email invalide : " + email);
            return false;
        }

        store.add(username);
        repository.save(username);
        notifier.notify(username, "Bienvenue dans la bibliothèque !");
        return true;
    }

    public void unregister(String username) {
        store.remove(username);
        repository.delete(username);
    }
}
