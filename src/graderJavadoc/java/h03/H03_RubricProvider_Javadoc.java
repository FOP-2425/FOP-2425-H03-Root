package h03;

import org.sourcegrade.jagr.api.rubric.*;

public class H03_RubricProvider_Javadoc implements RubricProvider {

    @Override
    public Rubric getRubric() {
        return Rubric.builder()
            .title("H03 | Mission Robots: The Ultimate Grid Race")
            .addChildCriteria(Criterion.builder()
                .shortDescription("Documentation")
                .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() -> JavadocExtractor.class.getDeclaredMethod("extractJavadoc")))
                    .pointsFailedMin()
                    .pointsPassedMax()
                    .build())
                .build())
            .build();
    }
}
