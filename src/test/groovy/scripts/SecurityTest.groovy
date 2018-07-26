package scripts

import hudson.security.*
import org.junit.BeforeClass
import org.junit.Test
import org.jvnet.hudson.test.recipes.LocalData
import org.jvnet.hudson.test.recipes.WithPlugin
import utilities.ZipTestFiles

import static org.assertj.core.api.Assertions.assertThat

class SecurityTest extends StartupTest {

    @BeforeClass
    public static void setUp() {
        setUp(SecurityTest.class, ["scripts/users.groovy", "scripts/security.groovy"])
        System.metaClass.static.getenv = { String key ->
            return [TEST_CONFIG_FILE: "jenkins.config,loggedinusers.config"].get(key)
        }
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["jenkins.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void matrix_auth() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        def authStrategy = this.jenkinsRule.getInstance().getAuthorizationStrategy()

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy.getClass().getCanonicalName()).isEqualTo('hudson.security.GlobalMatrixAuthorizationStrategy')

        def users = securityRealm.getAllUsers()
        assertThat(users).extracting("id").containsExactly("jimbo", "timbo")

        //Thread.sleep(1000 * 60 * 5)
    }

    @Test
    @LocalData
    @ZipTestFiles(files = ["loggedinusers.config"])
    @WithPlugin(["matrix-auth-1.4.hpi", "icon-shim-2.0.3.hpi"])
    void logged_in_users_can_do_anything() {

        def securityRealm = jenkinsRule.instance.getSecurityRealm()
        def authStrategy = this.jenkinsRule.getInstance().getAuthorizationStrategy()

        assertThat(securityRealm).isExactlyInstanceOf(HudsonPrivateSecurityRealm.class)
        assertThat(securityRealm.allowsSignup()).isFalse()

        assertThat(authStrategy).isExactlyInstanceOf(FullControlOnceLoggedInAuthorizationStrategy.class)
        assertThat(authStrategy.isAllowAnonymousRead()).isTrue()

        //Thread.sleep(1000 * 60 * 5)

    }
}
