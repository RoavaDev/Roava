[![Discord Server](https://img.shields.io/discord/958199132026200084?label=Roava&logo=discord&style=flat-square)](https://discord.gg/edKHJCqaYs)

Roava is a quick, and easy to set up library written in Kotlin, but it can support other JVM languages as well. 

Note that this project has not yet been fully released and still is a work-in-progress.

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