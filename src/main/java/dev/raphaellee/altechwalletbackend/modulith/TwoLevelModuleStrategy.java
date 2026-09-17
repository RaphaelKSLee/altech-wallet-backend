package dev.raphaellee.altechwalletbackend.modulith;

import org.springframework.modulith.core.ApplicationModuleDetectionStrategy;
import org.springframework.modulith.core.JavaPackage;

import java.util.stream.Stream;

public class TwoLevelModuleStrategy
        implements ApplicationModuleDetectionStrategy {
    @Override
    public Stream<JavaPackage> getModuleBasePackages(JavaPackage basePackage) {
        // Custom logic to return packages that are exactly 2 levels deep
        return basePackage.getDirectSubPackages().stream()
                .flatMap(pkg -> pkg.getDirectSubPackages().stream());
    }
}
