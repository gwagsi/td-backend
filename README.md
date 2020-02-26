# Procedure to import this web GWT project in Eclipse (Kepler).

1. Install GWT 2.5.1 inside your Eclipse Kepler.
2. If you want, install Window Builder pro for the design of interfaces.
3. Import the project inside your workspace, of if impossible to import the project, create a new project with existing sources.
4. Import librairies as follow (all needed librairies are in the repository named librairies, so you will have to clone it.) : 

## Add the following librairies in the folder `war/WEB-INF/lib/` (if `lib` folder does not exist, create it) :

        antlr-2.7.6.jar
        appserv-admin.jar
        appserv-deployment-client.jar
        appserv-ext.jar
        appserv-rt.jar
        axis.jar
        c3p0-0.9.1.jar
        cglib-2.2.jar
        commons-collections-3.1.jar
        commons-discovery-0.2.jar
        commons-logging.jar
        dom4j-1.6.1.jar
        dsn.jar
        eclipselink.jar
        gcm-server-1.0.2.jar
        gf-client.jar
        gf-client-l10n.jar
        gimap.jar
        hibernate-commons-annotations-3.2.0.Final.jar
        hibernate-core-3.6.10.Final.jar
        hibernate-entitymanager-3.6.10.Final.jar
        hibernate-jpa-2.0-api-1.0.1.Final.jar
        hibernate-tools-3.6.0.CR1.jar
        httpRestClient.jar
        imap.jar
        jackson-annotations-2.0.6.jar
        jackson-core-2.0.6.jar
        jackson-databind-2.0.6.jar
        javaee.jar
        javassist-3.12.0.GA.jar
        javax.persistence_2.1.0.v201304241213.jar
        jaxrpc.jar
        jndi-properties.jar
        json-simple-1.1.1.jar
        jta-1.1.jar
        load configuration
        log4j-api-2.5.jar
        log4j-core-2.5.jar
        mail.jar
        mailapi.jar
        mysql-connector-java-5.1.23-bin.jar
        org.eclipse.persistence.jpa.jpql_2.5.1.v20130918-f2b9fc5.jar
        oscache-2.1.jar
        pop3.jar
        postgresql-9.2-1002.jdbc4.jar
        saaj.jar
        slf4j-api-1.6.1.jar
        slf4j-simple-1.6.1.jar
        smtp.jar
        USAePayAPI-jaxws-1.6.4.jar
        wsdl4j.jar
        
# To compile the project in command line
        Open the terminal at the root of the project, and run the command : ant compile

# To package the project for deployment, in command line
        Open the terminal at the root of the project, and run the command : ant package , it will produce the file dashboard.war in dist/ folder.

