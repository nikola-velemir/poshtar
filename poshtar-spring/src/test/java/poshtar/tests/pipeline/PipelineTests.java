package poshtar.tests.pipeline;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import poshtar.tests.MockTransactionConfig;
import poshtar.tests.TestApplication;

@SpringBootTest(classes = TestApplication.class)
@Import(MockTransactionConfig.class)
public class PipelineTests {
}
