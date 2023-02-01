# Confident Spring Professional

## Profile configuration

## 1.1. JVM System Parameter
The profile names can also be passed in via a JVM system parameter. These profiles will be activated during application startup:

-Dspring.profiles.active=dev
Copy
## 1.2. Environment Variable
In a Unix environment, profiles can also be activated via the environment variable:

export spring_profiles_active=dev
Copy
## 1.3. Maven Profile
Spring profiles can also be activated via Maven profiles, by specifying the spring.profiles.active configuration property.

In every Maven profile, we can set a spring.profiles.active property:

<profiles>
    <profile>
        <id>dev</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <properties>
            <spring.profiles.active>dev</spring.profiles.active>
        </properties>
    </profile>
    <profile>
        <id>prod</id>
        <properties>
            <spring.profiles.active>prod</spring.profiles.active>
        </properties>
    </profile>
</profiles>
