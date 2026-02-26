package org.example.library.patterns.singleton;

/**
 * SINGLETON PATTERN — Approche A : Lazy Initialization (Double-Checked Locking)
 *
 * DatabaseConfig représente la connexion à la base de données.
 * Il ne doit exister qu'UNE SEULE instance pour toute l'application
 * (pas de connexions dupliquées, cohérence de la config).
 *
 * ✅ Lazy initialization : l'instance n'est créée que la première fois qu'on en a besoin.
 * ✅ Thread-safe : le mot-clé synchronized + volatile garantit la sécurité
 *    dans un contexte multi-thread.
 * ❌ Plus verbeux que l'Enum Singleton.
 */
public class DatabaseConfig {

    // volatile garantit la visibilité de l'écriture entre threads
    private static volatile DatabaseConfig instance;

    private String url;
    private String username;
    private String password;
    private int    maxPoolSize;

    // Constructeur privé : interdit l'instanciation directe
    private DatabaseConfig() {
        // Simulation d'une initialisation coûteuse
        this.url         = "jdbc:h2:mem:librarydb";
        this.username    = "sa";
        this.password    = "";
        this.maxPoolSize = 10;
        System.out.println("[DatabaseConfig] Instance créée (connexion établie)");
    }

    /**
     * Point d'accès global unique.
     * Double-Checked Locking pour la performance et la thread-safety.
     */
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    // --- Getters ---
    public String getUrl()         { return url; }
    public String getUsername()    { return username; }
    public String getPassword()    { return password; }
    public int    getMaxPoolSize() { return maxPoolSize; }

    // --- Setters (configuration modifiable après création) ---
    public void setMaxPoolSize(int maxPoolSize) { this.maxPoolSize = maxPoolSize; }

    @Override
    public String toString() {
        return "DatabaseConfig{url='" + url + "', username='" + username +
               "', maxPoolSize=" + maxPoolSize + '}';
    }
}
