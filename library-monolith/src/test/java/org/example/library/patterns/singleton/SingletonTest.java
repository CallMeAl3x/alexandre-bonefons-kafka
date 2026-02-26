package org.example.library.patterns.singleton;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pattern 3 — Singleton")
class SingletonTest {

    // -----------------------------------------------------------------------
    // DatabaseConfig — Lazy Initialization
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("DatabaseConfig : deux appels retournent la même instance")
    void databaseConfigSameInstance() {
        DatabaseConfig i1 = DatabaseConfig.getInstance();
        DatabaseConfig i2 = DatabaseConfig.getInstance();
        assertSame(i1, i2, "getInstance() doit toujours retourner la même instance");
    }

    @Test
    @DisplayName("DatabaseConfig : la config est correctement initialisée")
    void databaseConfigValues() {
        DatabaseConfig cfg = DatabaseConfig.getInstance();
        assertNotNull(cfg.getUrl());
        assertTrue(cfg.getUrl().startsWith("jdbc:"));
        assertTrue(cfg.getMaxPoolSize() > 0);
    }

    @Test
    @DisplayName("DatabaseConfig : une modification est visible depuis toutes les références")
    void databaseConfigMutationShared() {
        DatabaseConfig ref1 = DatabaseConfig.getInstance();
        DatabaseConfig ref2 = DatabaseConfig.getInstance();
        ref1.setMaxPoolSize(42);
        assertEquals(42, ref2.getMaxPoolSize(),
                "La modification via ref1 doit être visible via ref2 (même instance)");
    }

    // -----------------------------------------------------------------------
    // LibraryConfig — Enum Singleton
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("LibraryConfig : INSTANCE est unique (==)")
    void libraryConfigSameInstance() {
        LibraryConfig c1 = LibraryConfig.INSTANCE;
        LibraryConfig c2 = LibraryConfig.INSTANCE;
        assertSame(c1, c2);
    }

    @Test
    @DisplayName("LibraryConfig : une modif via une référence est visible depuis l'autre")
    void libraryConfigMutationShared() {
        LibraryConfig.INSTANCE.setMaxBooksPerUser(3);
        assertEquals(3, LibraryConfig.INSTANCE.getMaxBooksPerUser());
    }

    @Test
    @DisplayName("LibraryConfig : les valeurs par défaut sont cohérentes")
    void libraryConfigDefaults() {
        assertNotNull(LibraryConfig.INSTANCE.getLibraryName());
        assertTrue(LibraryConfig.INSTANCE.getLoanDurationDays() > 0);
        assertTrue(LibraryConfig.INSTANCE.getDailyFineAmount() >= 0);
    }
}
