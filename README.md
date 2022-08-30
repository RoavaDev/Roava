[![Discord Server](https://img.shields.io/discord/958199132026200084?label=Roava&logo=discord)](https://discord.gg/edKHJCqaYs)
[![CI](https://github.com/RoavaDev/Roava/actions/workflows/CI.yml/badge.svg)](https://github.com/RoavaDev/Roava/actions/workflows/CI.yml)

Roava is a quick, and easy to set up Roblox API wrapper written in Kotlin, but it can support other JVM languages as well. 

## Setup
This project has been uploaded to the Maven central repository. If you would like to add the project as a dependency, you may do so in Gradle by:
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation 'dev.roava:roava:VERSION'
}
```

If you would like to test a snapshot version, you must add the sonatype snapshot repository to your repositories list:
```groovy
repositories {
    maven {
        url = 'https://s01.oss.sonatype.org/content/repositories/snapshots'
    }
}

dependencies {
    implementation 'dev.roava:roava:VERSION-SNAPSHOT'
}
```

## Example

This creates a simple program which generates a new authenticated Client using the provided Cookie.

```java
public class Main {
    public static void main(String[] args) {
        RoavaClient client = new RoavaClient("Cookie");
        
        System.out.println(client.name);
    }
}
```

## Issues
If you have any questions, comments, or suggestions, you may either join our Discord server (preferred), or open an [issue](https://github.com/RoavaDev/Roava/issues).

## Contributions
Any and all contributions are welcome! If you would like to contribute to the project, please open a pull request.

## License
This project is licensed under the [MIT license](https://mit-license.org/). The license is attached to the project.