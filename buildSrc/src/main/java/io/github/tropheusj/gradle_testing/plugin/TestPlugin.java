package io.github.tropheusj.gradle_testing.plugin;

import io.github.tropheusj.gradle_testing.plugin.transform.TestSourcesTransform;
import io.github.tropheusj.gradle_testing.plugin.transform.TestTransform;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.artifacts.type.ArtifactTypeDefinition;
import org.gradle.api.attributes.Attribute;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.DocsType;
import org.gradle.api.attributes.Usage;
import org.gradle.api.model.ObjectFactory;

public class TestPlugin implements Plugin<Project> {
	public static final Attribute<Boolean> TRANSFORMED = Attribute.of("io.github.tropheusj.gradle_testing.transformed", Boolean.class);

	@Override
	public void apply(Project project) {
		System.out.println("Applying to project " + project.getName());

		ObjectFactory objects = project.getObjects();
		DependencyHandler deps = project.getDependencies();
		ConfigurationContainer configurations = project.getConfigurations();

		// register attribute
		deps.getAttributesSchema().attribute(TRANSFORMED);
		// jars are non-transformed by default
		deps.getArtifactTypes().getByName("jar").getAttributes().attribute(TRANSFORMED, false);

		// require runtimeClasspath and compileClasspath to be transformed=true
		configurations.named("runtimeClasspath").configure(
				runtimeClasspath -> runtimeClasspath.getAttributes().attribute(TRANSFORMED, true)
		);
		configurations.named("compileClasspath").configure(
				compileClasspath -> compileClasspath.getAttributes().attribute(TRANSFORMED, true)
		);

		// register normal transform
		deps.registerTransform(TestTransform.class, spec -> {
			spec.getFrom()
					.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY))
					.attribute(TRANSFORMED, false);
			spec.getTo()
					.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY))
					.attribute(TRANSFORMED, true);
		});

		// register source transform
		deps.registerTransform(TestSourcesTransform.class, spec -> {
			spec.getFrom()
					.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME))
					.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL))
					.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.DOCUMENTATION))
					.attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.class, DocsType.SOURCES))
					.attribute(TRANSFORMED, false);
			spec.getTo()
					.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME))
					.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL))
					.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.DOCUMENTATION))
					.attribute(DocsType.DOCS_TYPE_ATTRIBUTE, objects.named(DocsType.class, DocsType.SOURCES))
					.attribute(TRANSFORMED, true);
		});
	}
}
