package org.kefirsf.bb.test.blackbox;

import org.junit.Test;
import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.ConfigurationFactory;
import org.kefirsf.bb.TextProcessor;
import org.kefirsf.bb.conf.*;
import org.kefirsf.bb.test.Assert;

import java.util.Arrays;

/**
 * Test dynamic configuration
 *
 * @author Vitaliy Samolovskih aka Kefir
 */
public class DynamicConfigurationTest {
    @Test
    public void testAddCodeToRoot() {
        Configuration cfg = ConfigurationFactory.getInstance().create();
        cfg.setParam("path", "/img/");
        Code code = new Code();
        code.setPattern(new Pattern(Arrays.asList(new Constant(":)"))));
        code.setTemplate(
                new Template(
                        Arrays.asList(
                                new Constant("<img src=\""),
                                new NamedValue("path"),
                                new Constant("smile.gif"),
                                new Constant("\"/>")
                        )
                )
        );
        cfg.getRootScope().addCode(code);

        TextProcessor processor = BBProcessorFactory.getInstance().create(cfg);

        Assert.assertEquals("<img src=\"/img/smile.gif\"/>", processor.process(":)"));
    }
}
