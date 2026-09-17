package dev.raphaellee.altechwalletbackend;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.junit.jupiter.api.Assertions.*;

class ModulithDocumentationTest {

    @Test
    void generateDependencyDiagram() {
        ApplicationModules modules = ApplicationModules
                .of(AltechWalletBackendApplication.class);

        new Documenter(modules)
                .writeModulesAsPlantUml()
                .writeIndividualModulesAsPlantUml();
    }
}