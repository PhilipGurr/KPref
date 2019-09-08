# KPref

This is a very small Android library for Shared Preferences written in Kotlin. It contains a property delegate and a useful function to edit
and use Preferences more easily wihout any boilerplate code.

## Installation

Include KPref library in your app-level build.gradle using the following line:

```
implementation 'com.philipgurr.android:kpref:1.1.0'
```

or alternatively using Maven:

```xml
<dependency>
	<groupId>com.philipgurr.android</groupId>
	<artifactId>kpref</artifactId>
	<version>1.1.0</version>
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
    val intPref by SharedPreference("int_pref", IntConverter(), 0)
    val booleanPref by SharedPreference("bool_pref", BooleanConverter(), true)
    val stringPref by SharedPreference("string_pref", StringConverter(), "")
    val floatPref by SharedPreference("float_pref", FloatConverter(), 1.2f)
    
    // ...
```
or data classes and arbitrary objects using the built-in [Gson](https://github.com/google/gson) converter:
```kotlin
    val userPref by SharedPreference("user_pref", GsonConverter(User::class), User("Default", "User", 0))
    
    data class User(
        val name: String,
        val surname: String,
        val age: Int
    )
```
The values get converted and saved in the Preference using the specified `PrefTypeConverter`. You can create your own converter if necessary. Note that in this case you have to implement the logic for writing and reading yourself using the provided `SharePreferences` object.

You can also generate a default value by using a lambda:
```kotlin
    var lastStart by SharedPreference("last_started", LongConverter()) {
        Calendar.getInstance().timeInMillis
    }
```

<b>Attention:</b> If you don't specify a converter, the field will always return the default value. If you don't want to use converters,
use version 1.0.0.

### editPreferences
You can use the method `editPreferences` on a Context or SharedPreferences object and pass a function that gets invoked on the corresponding
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
