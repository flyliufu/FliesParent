# FliesParent
In your project `build.gradle`
```gradle
allprojects {
  repositories {
    jcenter()
    // add this line in you file
    maven { url "https://dl.bintray.com/flyliufu/maven/" }
  }
}
```
In your custom model's `build.gradle`
```gradle
dependencies {
  // import this 
  compile 'com.lefu8.flies:flies:${laster_version}'
 }
```