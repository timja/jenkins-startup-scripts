import hudson.security.HudsonPrivateSecurityRealm
import jenkins.model.Jenkins


// Leave open to adding other types of users
if (config.users.users) {

    if (!config.security) {
        throw new RuntimeException("You MUST have a security block configured if you are configuring users.")
    }

    def instance = Jenkins.getInstance()
    def realm = new HudsonPrivateSecurityRealm(false, false, null)
    //def strategy = new FullControlOnceLoggedInAuthorizationStrategy()

    //strategy.setAllowAnonymousRead(false)
    instance.setSecurityRealm(realm)
    //instance.setAuthorizationStrategy(strategy)

    config.users.users.each {username, password ->
        realm.createAccount(username, password)
    }

    instance.save()
}


