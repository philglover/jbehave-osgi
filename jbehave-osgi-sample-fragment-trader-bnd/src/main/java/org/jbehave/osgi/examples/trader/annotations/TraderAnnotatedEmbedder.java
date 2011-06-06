package org.jbehave.osgi.examples.trader.annotations;

import static org.jbehave.core.reporters.Format.CONSOLE;
import static org.jbehave.core.reporters.Format.HTML;
import static org.jbehave.core.reporters.Format.TXT;
import static org.jbehave.core.reporters.Format.XML;

import java.text.SimpleDateFormat;
import java.util.List;

import org.jbehave.core.InjectableEmbedder;
import org.jbehave.core.annotations.Configure;
import org.jbehave.core.annotations.UsingEmbedder;
import org.jbehave.core.annotations.UsingSteps;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.junit.AnnotatedEmbedderRunner;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.examples.trader.steps.AndSteps;
import org.jbehave.examples.trader.steps.BeforeAfterSteps;
import org.jbehave.examples.trader.steps.CalendarSteps;
import org.jbehave.examples.trader.steps.PriorityMatchingSteps;
import org.jbehave.examples.trader.steps.SandpitSteps;
import org.jbehave.examples.trader.steps.SearchSteps;
import org.jbehave.examples.trader.steps.TraderSteps;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyDateConverter;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyEmbedder;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyRegexPrefixCapturingPatternParser;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyReportBuilder;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyStoryControls;
import org.jbehave.osgi.examples.trader.annotations.TraderAnnotatedEmbedder.MyStoryLoader;
import org.jbehave.osgi.io.OsgiLoadFromClasspath;
import org.jbehave.osgi.io.OsgiStoryFinder;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AnnotatedEmbedderRunner.class)
@Configure(stepPatternParser = MyRegexPrefixCapturingPatternParser.class, storyControls = MyStoryControls.class, storyLoader = MyStoryLoader.class, storyReporterBuilder = MyReportBuilder.class, parameterConverters = { MyDateConverter.class })
@UsingEmbedder(embedder = MyEmbedder.class, generateViewAfterStories = true, ignoreFailureInStories = true, ignoreFailureInView = true, storyTimeoutInSecs = 100, threads = 1, metaFilters = "-skip")
@UsingSteps(instances = { TraderSteps.class, BeforeAfterSteps.class,
		AndSteps.class, CalendarSteps.class, PriorityMatchingSteps.class,
		SandpitSteps.class, SearchSteps.class })
public class TraderAnnotatedEmbedder extends InjectableEmbedder {

	@Test
	public void run() {
		List<String> storyPaths = new OsgiStoryFinder().findPaths(
				"/", "**/*.story",
				"**/examples_table_loaded*");
		injectedEmbedder().runStoriesAsPaths(storyPaths);
	}

	public static class MyEmbedder extends Embedder {
		public MyEmbedder() {
			// Properties properties = new Properties();
			// properties.setProperty("project.dir",
			// System.getProperty("project.dir", "N/A"));
			// useSystemProperties(properties);
		}
	}

	public static class MyStoryControls extends StoryControls {
		public MyStoryControls() {
			doDryRun(false);
			doSkipScenariosAfterFailure(false);
		}
	}

	public static class MyStoryLoader extends OsgiLoadFromClasspath {
		public MyStoryLoader() {
			super();
		}
	}

	public static class MyReportBuilder extends StoryReporterBuilder {
		public MyReportBuilder() {
			this.withFormats(CONSOLE, TXT, HTML, XML).withDefaultFormats();
		}
	}

	public static class MyRegexPrefixCapturingPatternParser extends
			RegexPrefixCapturingPatternParser {
		public MyRegexPrefixCapturingPatternParser() {
			super("%");
		}
	}

	public static class MyDateConverter extends DateConverter {
		public MyDateConverter() {
			super(new SimpleDateFormat("yyyy-MM-dd"));
		}
	}

}
