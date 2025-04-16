package io.github.tropheusj.gradle_testing.plugin.transform;

import org.gradle.api.artifacts.transform.InputArtifact;
import org.gradle.api.artifacts.transform.TransformAction;
import org.gradle.api.artifacts.transform.TransformOutputs;
import org.gradle.api.artifacts.transform.TransformParameters;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class TestTransform implements TransformAction<TransformParameters.None> {
	@InputArtifact
	public abstract Provider<FileSystemLocation> getInput();

	@Override
	public void transform(TransformOutputs outputs) {
		File input = this.getInput().get().getAsFile();
		System.out.println("Transforming: " + input);

		String outputName = input.getName().replace(".jar", "-transformed.jar");
		File output = outputs.file(outputName);

		try {
			Files.copy(input.toPath(), output.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
