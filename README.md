# web-app
- A Spring Boot application acting as a Resource Server
- Azure Active Directory for Authentication and Authorization
- Angular Frontend manages the retrieval and refreshing of tokens
- Uses Oauth2.0 Implicit flow
- Spring boot applications acts as a Resource Server and validates incoming JWT tokens
- Uses azure-webapp-maven-plugin to get the WAR deployed to Azure Web App
- Configures Azure Storage Blob and Queue 
- Uses Azure Cosmos Db for storing Todo in JSON format
- Integrated with Azure Key Vault

# Deployment Steps
1. Login to Azure platform and create a service principal using Azure CLI

```
az ad sp create-for-rbac --name "spring-boot-app-sp"
```
This will give client and tenant information which can be used to publish any application to Azure Web App

```json
{
  "appId": "xxxxxxxxxx",
  "displayName": "spring-boot-app",
  "name": "http://spring-boot-app",
  "password": "yyyyyyyyy",
  "tenant": "zzzzzzzzzzz"
}
```

2. Now we configure Azure service principal authentication settings in our Maven settings.xml, with the help of the following section, under <servers>:

```xml
<server>
    <id>azure-auth</id>
    <configuration>
        <client>xxxxxxxxxx</client>
        <tenant>zzzzzzzzzzz</tenant>
        <key>yyyyyyyyy</key>
        <environment>AZURE</environment>
    </configuration>
</server>
```

We'll rely on the authentication configuration above when uploading our Spring Boot application to the Microsoft platform, using azure-webapp-maven-plugin.

3. Let's add the following Maven plugin to the pom.xml:
```xml
<plugin>
    <groupId>com.microsoft.azure</groupId>
    <artifactId>azure-webapp-maven-plugin</artifactId>
    <version>${azure-webapp-maven-plugin.version}</version>
    <configuration>
        <authentication>
            <serverId>azure-auth</serverId>
        </authentication>
        <linuxRuntime>tomcat 8.5-jre8</linuxRuntime>
        <appName>spring-boot-app</appName>
        <resourceGroup>az-204-app-service-group</resourceGroup>
        <appServicePlanName>ASP-az204appservicegroup-81f2</appServicePlanName>
    </configuration>
</plugin>
```

We provide all the information about Azure Web App in the configuration along with reference to Tomcat Server under authentication.
Refer this page for more information on [com.microsoft.azure.azure-webapp-maven-plugin](https://docs.microsoft.com/en-us/azure/app-service/quickstart-java?tabs=javase&pivots=platform-linux) 

4. Make sure you deploy your application as a WAR file. 
Execute this command to get this application deployed to Azure Web App:

```text
mvn clean package azure-webapp:deploy
```

# Packaging Instructions

Follow these steps to package Spring Boot application as a WAR:
- Step 1: Change maven packaging to war in pom.xml
- Step 2: Add tomcat dependency

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-tomcat</artifactId>
   <scope>provided</scope>
</dependency>
```

- Step 3: Make WebAppApplication extend SpringBootServletInitializer

# References
- [Baeldung Spring Boot Azure](https://www.baeldung.com/spring-boot-azure) 
- [Create a Java app on Azure App Service](https://docs.microsoft.com/en-us/azure/app-service/quickstart-java?tabs=javase&pivots=platform-linux)
- [Deploy a Spring Boot WAR into a Tomcat Server](https://www.baeldung.com/spring-boot-war-tomcat-deploy)
- [Build a Java app to manage Azure Cosmos DB SQL API data](https://docs.microsoft.com/en-us/azure/cosmos-db/create-sql-api-java?tabs=sync)
- [Azure Storage libraries for Java](https://docs.microsoft.com/en-us/java/api/overview/azure/storage?view=azure-java-stable)
- [OAuth 2.0 and OpenID Connect protocols on Microsoft identity platform](https://docs.microsoft.com/en-us/azure/active-directory/develop/active-directory-v2-protocols)
- [Sign in users and call the Microsoft Graph API from an Angular single-page application](https://docs.microsoft.com/en-us/azure/active-directory/develop/tutorial-v2-angular)
- [Spring REST API + OAuth2 + Angular](https://www.baeldung.com/rest-api-spring-oauth2-angular)
- [An Angular single-page application that authenticates users with Azure AD and calls a protected ASP.NET Core web API](https://github.com/shank9918/ms-identity-javascript-angular-spa-aspnetcore-webapi)
- [Reading a secret from Azure Key Vault in a Spring Boot application](https://docs.microsoft.com/en-us/azure/developer/java/spring-framework/configure-spring-boot-starter-java-app-with-azure-key-vault)

## Azure AD for calls to secured Azure Function
- [Configure Authentication Provider AAD](https://docs.microsoft.com/en-us/azure/app-service/configure-authentication-provider-aad)
- [OAuth2 Client Credentials Grant Flow](https://docs.microsoft.com/en-us/azure/active-directory/azuread-dev/v1-oauth2-client-creds-grant-flow)
- [Add App Roles in Azure AD](https://docs.microsoft.com/en-us/azure/active-directory/develop/howto-add-app-roles-in-azure-ad-apps)
- [Microsoft Authentication Library for Java](https://github.com/AzureAD/microsoft-authentication-library-for-java)