package scripts

import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat

class SamlTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SamlTest.class, ["scripts/saml.groovy"])
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "saml-1.0.7.hpi", "icon-shim-2.0.3.hpi",
                 "bouncycastle-api-2.16.1.hpi", "mailer-1.20.hpi", "display-url-api-2.2.0.hpi"])
    void shouldConfigureSaml() {

        def descriptor = jenkinsRule.instance.getDescriptor('org.jenkinsci.plugins.saml.SamlSecurityRealm')

        //Thread.sleep(1000 * 60 * 5)
        //assertThat(descriptor.getUsernameAttributeName()).isEqualTo('http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name')


    }
}
