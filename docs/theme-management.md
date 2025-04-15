# Theme Management System
This document will outline:
- The Theme Management System and how it works
- How to create new themes
- How to modify existing themes
- Current limitations

# What it is, and how it works
The theme management system is a system developed for developing, swapping between, and handling different styles/themes for our application. <br> 
**It has a few core components:**
- The config enum, [`Theme`](../src/main/java/com/qut/cab302_project_pomodora/config/Theme.java) which has the purpose of defining the existing themes. This causes a limitation[<sup>1</sup>](#current-limitations) and so may be reworked
- The util singleton class, [`ThemeManager`](../src/main/java/com/qut/cab302_project_pomodora/util/ThemeManager.java), which handles themes globally. It has the following methods:
  1. `getInstance()` - This serves the purpose of ensuring the singleton nature of the ThemeManager
  2. `applyTheme(Scene scene, Theme theme)` - Applies the passed theme to the scene
  3. `applyCurrentTheme(Scene scene)` - Applies the current theme to the scene
  4. `setCurrentTheme(Theme theme)` - A setter: Sets the passed theme as the currentTheme
  5. `getCurrentTheme` - A getter: gets the currentTheme
- The base stylesheet, [`common/base.css`](../src/main/resources/com/qut/cab302_project_pomodora/css/common/base.css) which defines all primitives, default tokens, and styles for all objects
- The theme stylesheets, [`themes/<themeName>.css`](../src/main/resources/com/qut/cab302_project_pomodora/css/themes/) which contain the theme specific tokens, overriding the defaults of the base

# How to create new themes
Creating a new theme is straight forwards using this system, requiring only two major steps:
1. Create your <themeName.css> file in the [themes](../src/main/resources/com/qut/cab302_project_pomodora/css/themes/) folder, and define the tokens you wish to override from the base style
2. Add your theme to the config enum [`Theme`](../src/main/java/com/qut/cab302_project_pomodora/config/Theme.java)
3. If you want to modify a specific styleClass, such as to change the border, font, etc, this can also easily be done be defining it in your <themeName.css>, however if you are constructing a page and realise that a styleClass necessary hasn't yet been defined in the base, ensure you do so

# How to modify existing themes
Modifying an existing theme is as easy as modifying your <themeName.css> to reflect your desired changes. As per limitation[<sup>1</sup>](#current-limitations), themes cannot be modified at runtime.

# Current Limitations
1. Since we're currently using an enum to define themes, themes cannot be created, edited, or deleted by the user at runtime. Swapping to another list type can change this if said functionality is needed later