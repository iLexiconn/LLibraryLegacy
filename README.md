[<p align="center"><img src="http://i.imgur.com/hIVt6wO.png"/></p>](http://www.planetminecraft.com/mod/llibrary)
<p align="center">LLibrary - The modding library that claims to be lightweight.</p>

## About
LLibrary contains the most contemporary and advanced modding tools which are essential for any modder looking for an easier experience and a new standard of content. Additionally, mods using LLibrary will interact more smoothly and more complexly. This means less crashes and a smoother experience overall. Modding with LLibrary becomes much easier with clean workspace it allows. It also contains animation tools which you can use to animate your models in ways you could have only previously imagined. Your mods always stay up to date because of the built-in update checker. It also adds a weapon dictionary, allowing other mods to see what kind of weapon your mod adds and react appropriately. 

LLibrary also adds support for multiple entity hitboxes, an essential feature for any mob mod. For example, if you make an entity that has a complex model, you don’t want players to be able to hit the air around it. With LLibrary you can make an hitbox for every part of your entity, creating boundaries of any shape. 

Abstract classes, which have already completed methods that you can extend in your code, will obviously save some time. 
You can also make use of a web helper to easily parse web content. 
On top of that, you can easily edit player models and even draw your own GUI over an already existing GUI! 
These examples are just a taste of everything you can do with LLibrary. 

## Quickstart
Adding LLibrary to your project is easy. Assuming you're using [Gradle](http://gradle.org/), you only have to add the following snippet to your build file:
```gradle
repositories {
    maven {
        name = "ilexiconn"
        url = "http://maven.ilexiconn.net"
    }
}

dependencies {
    compile "net.ilexiconn:llibrary:VERSION-MCVERSION:dev"
}
```
You can find a complete list with all available versions [here](http://maven.ilexiconn.net/net/ilexiconn/llibrary/).
