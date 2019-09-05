# KPref

This is a very small Android library for Shared Preferences written in Kotlin. It contains a property delegate and a useful function to edit
and use Preferences more easily wihout any boilerplate code.

## Installation

Include KPref library in your app-level build.gradle using the following line:

```
implementation 'com.philipgurr.android:kpref:1.0.0'
```

or alternatively using Maven:

```xml
<dependency>
	<groupId>com.philipgurr.android</groupId>
	<artifactId>kpref</artifactId>
	<version>1.0.0</version>
	<type>pom</type>
</dependency>
```

## Usage

### Property Delegate
To be able to use the property delegate, your class has to implement `ContextProvider` and specify a `Context` to use for the 
preferences:
```kotlin
class MyActivity : AppCompatActivity(), ContextProvider {
    override val context: Context
        get() = applicationContext
```
Now you can use the `SharedPreference` delegate to use primitives in your preferences:
```kotlin
    // Provide a key and a default value for each preference
    val intPref by SharedPreference("int_pref", 0)
    val booleanPref by SharedPreference("bool_pref", true)
    val stringPref by SharedPreference("string_pref", "")
    val floatPref by SharedPreference("float_pref", 1.2f)
    
    // ...
```
or data classes and arbitrary objects:
```kotlin
    val userPref by SharedPreference("user_pref", User("Default", "User", 0))
    
    data class User(
        val name: String,
        val surname: String,
        val age: Int
    )
```
Non-primitives get serialized and saved in a String preference using [Gson](https://github.com/google/gson). When reading from the
variable, the string gets deserialized to an object of the same type as the provided default value will be created.

### editPreferences
You can also use the extension function `editPreferences` on any Context and pass a function that gets invoked on the corresponding
`SharedPreferences.Editor`:

```kotlin
context.editPreferences {
            remove("int_pref")
            putLong("long_pref", Long.MAX_VALUE)
        }
```
## Contributing
Pull requests are welcome. Please make sure to update tests as appropriate.

## License
[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
