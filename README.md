# BFME-ResFix

**All-in-One Tool for **"The Battle for Middle Earth"** Anthology**

**Restoring the 20-year-old games to their former glory**

**Features:**
- Run the games on Windows 7, 8, 10 & 11
- Supports all official game languages
- Includes the latest official game patches
- Enables Widescreen resolutions up to 4K or higher
- Retains the original HUD, Zoom, and Fog

# How to use

[Download tool here](https://github.com/jcoester/bfme-resfix/releases)

*Note: The games are expected to be installed separately from the disks or from mounted [digital images](https://www.mediafire.com/folder/qipu589jehkpm/The_Battle_for_Middle_Earth_Anthology).*

[![Click the image to watch the Video Demo](https://github.com/jcoester/bfme-resfix/blob/main/images/Thumbnail.jpg)](https://youtu.be/4pJYwUAgK-g)

# Result

### Original vs. BFME-ResFix Modded
![](https://github.com/jcoester/bfme-resfix/blob/main/images/01_Before-After.gif)

# Features 

### 1. Retains the original HUD
![](https://github.com/jcoester/bfme-resfix/blob/main/images/02_HUD.gif)

### 2. Retains the original Zoom
![](https://github.com/jcoester/bfme-resfix/blob/main/images/03_Zoom_Level.gif)

### 3. Retains the original Fog
![](https://github.com/jcoester/bfme-resfix/blob/main/images/04_Fog.gif)

### 4. Enables Widescreen resolutions up to 4K or higher
![](https://github.com/jcoester/bfme-resfix/blob/main/images/05_High-Res.gif)

# Technical Info

### High-Resolution scaling
Some in-game fonts do not scale well on High DPI displays. **bfme-resfix** uses the Windows Display Scaling factor and recommends the most appropriate resolution for the screen. Example: *On a 3840 x 2160 screen with 200% scaling, 1920 x 1080 is recommended. Choosing a higher resolution like 2560 x 1440 can work depending on user preference, screen size and viewing distance.*

### Retaining Zoom & Fog
To match the original Zoom level, the following **Camera Angle** and **Height Multiplier** are applied to each map's original heights. 
The same values are used to retain the original Fog level. Each Aspect Ratio requires its respective *Maps.big*-file to match the original look.

| Aspect Ratio | Camera Angle | Height Multiplier | Min. Height | Max. Height | 
|--------------|--------------|-------------------|-------------|-------------|
| 4:3          | 37.5°        | 1,000             | 120         | 300         |
| 16:9         | 35°          | 1,333             | 160         | 400         |
| 21:9         | 32.5°        | 1,750             | 210         | 525         |
| 32:9 [1]     | 30°          | 2,667             | 320         | 800         |

[1] *Engine breaks with 32:9 settings, displaying a black curtain at max. height 800. Reducing the height would effectively decrease the horizontal FOV compared to other aspect ratios, hence it is not advised.*

[Maps.big direct downloads](https://github.com/jcoester/bfme-resfix-storage/tree/main/Maps)

### Retaining HUD

The HUD mods used by **bfme-resfix** are

| Game  | Link | 
|-------|------|
| BFME1 | [BFME1 Widescreen APT Fix](https://www.the3rdage.net/item-817) |
| BFME2 | [BfMe II / RotWk Widescreen UI Mod (alpha)](https://www.the3rdage.net/item-717) | 
| ROTWK | [BfMe II / RotWk Widescreen UI Mod (alpha)](https://www.the3rdage.net/item-717) |

# Self-creating the Executable

1. Clone this repo: ```https://github.com/jcoester/bfme-resfix.git```
2. In [IntelliJ IDEA Community Edition](https://www.jetbrains.com/de-de/idea/download/download-thanks.html?platform=windows&code=IIC) or similar: Open project
3. In **IntelliJ**: ```Build``` > ```Build Artifacts...``` > ```bfme-resfix:jar``` > ```Build```
4. In [Launch4j](https://launch4j.sourceforge.net/): Open ```.\launch4j\config.xml``` 
5. In **Launch4j** ```Basic``` & ```Splash``` tab: Adjust paths to your system
6. In **Launch4j**: ```Build wrapper``` by clicking the settings wheel icon
7. Done!
