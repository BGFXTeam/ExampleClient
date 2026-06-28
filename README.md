# ExampleClient

ExampleClient is a Minecraft-style client modification framework for BGFX.

It provides a ClickGUI system, module management, categories, settings, and custom modules.

## Features

- ClickGUI interface
- Module system
- Module categories
- Boolean settings
- Slider settings
- Custom modules
- BGFX script integration

## Structure

```
ExampleClient/
├── ExampleClient.java
├── ClickGUI.java
├── Module.java
├── ModuleManager.java
├── ModuleCategory.java
├── Setting.java
└── impl/
```

## Module System

Modules are created by extending:

```java
Module
```

Example:

```java
public class Fly extends Module {

    public Fly(){
        super("Fly", ModuleCategory.MOVEMENT);
    }

}
```

## Categories

```
COMBAT
MOVEMENT
WORLD
VISUAL
MISC
```

## Settings

Supported:

- Boolean settings
- Slider settings

## ClickGUI

Features:

- Category panels
- Module list
- Toggle modules
- Settings controls

## Building

```bash
./gradlew assembleRelease
```

## BGFX Integration

Uses:

```
bgfxwrapper.jar
```

Provides:

```java
com.executor.bgfxui.ScriptManager
```

## Example Modules

Combat:
- KillAura
- Aimbot
- AutoClicker

Movement:
- Fly
- Speed
- HighJump

World:
- Nuker
- FastBreak

Visual:
- Fullbright

## License

Free to modify and create your own BGFX clients.
