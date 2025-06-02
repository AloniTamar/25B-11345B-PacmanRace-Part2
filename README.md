
# Packman Race

Second Assignment – Mobile Application Development Course

Pacman Race is a reflex-based Android arcade game where the player avoids ghosts and collects fruit while moving along a five-lane path. The game includes tilt-based movement and multiple difficulty modes for an engaging mobile experience.



## Features

- Classic 5-lane endless runner gameplay
- Sensor-based control (tilt your device to move left/right)
- Two speed modes (Slow / Fast) triggered by Y-axis tilt
- Crash and coin sound effects
- Game-over score saving with geolocation
- View high scores and their locations on a map
- Light and intuitive UI
## Techniques

- **Language:** Kotlin
- **IDE:** Android Studio
- **Platform:** Android
- **APIs:** Google Maps API, FusedLocationProviderClient
- **Persistence:** SharedPreferences
- **UI Libraries:** Material Components, AppCompat


## How To Run

1. Install PacmanRace (Part 2) with git:

```bash
  git pull 25B_11345B_PacmanRace_Part2
```
    
2. Open the project in Android Studio.

    
3. Replace the placeholder YOUR_API_KEY_HERE in AndroidManifest.xml with your own Google Maps API key.


4. Build and run the project on a physical device (location features may not work correctly on emulators).
    
## Permissions & Notes

- The game requests location access to save the location of high scores.
- Location is only saved if the player grants permission and GPS is enabled.
- If permission is denied, default coordinates are saved (Afeka College).
## Authors

- [@AloniTamar](https://github.com/AloniTamar)

