<div align="center">

<img src="src/main/resources/assets/pingback/icon.png" alt="Pingback icon" width="112">

# Pingback

**Keep Minecraft chat connected.**

Reply to a recent chat message with a quoted snippet and an `@player` reference — without manually copying the original message.

[![Minecraft 1.21.4](https://img.shields.io/badge/Minecraft-1.21.4-62A744?style=flat-square&logo=minecraft&logoColor=white)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-DBD0B4?style=flat-square)](https://fabricmc.net/)
[![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![MIT License](https://img.shields.io/badge/License-MIT-3DA639?style=flat-square)](LICENSE)

[Features](#features) · [Installation](#installation) · [Usage](#usage) · [Configuration](#configuration) · [Limitations](#compatibility--limitations)

</div>

---

## Features

- **Contextual replies:** turn a recent player message into a reply with a quote, sender reference, and configurable separator.
- **Quick controls:** select the latest message, cycle targets, cancel a selection, or toggle Pingback with keybinds.
- **Quote cleanup:** trim selected prefixes and handle mentions using configurable rules.
- **Formatting options:** adjust quote length, delimiters, separator, and message-length limit.
- **Status indicators:** show the current target in the Action Bar or a small HUD label.
- **Local configuration:** settings are stored in `config/pingback.json`.
- **Optional config screen:** install Mod Menu and YetAnotherConfigLib (YACL) for in-game configuration.

## Requirements

| Component | Requirement |
|---|---|
| Minecraft | **1.21.4** |
| Mod loader | Fabric Loader **0.16.10+** |
| Java | **21+** |
| Required mod dependency | Fabric API |
| Optional | Mod Menu and YACL for the configuration screen |

These are the versions declared by the project; they do not imply compatibility with every modpack or server setup.

## Installation

1. Install Fabric for Minecraft **1.21.4**: [Fabric Installer](https://fabricmc.net/use/installer/).
2. Install the matching [Fabric API](https://modrinth.com/mod/fabric-api).
3. Download a Pingback JAR from [GitHub Releases](https://github.com/iKiuox/Pingback-/releases). If no release asset is available, [build from source](#build-from-source).
4. Put the JAR in the `mods` folder for the Minecraft instance you use.
5. Launch the game with the Fabric profile.

For the in-game config screen, also install compatible versions of [Mod Menu](https://modrinth.com/mod/modmenu) and [YACL](https://modrinth.com/mod/yacl). They are optional for the core reply flow.

## Usage

1. Receive player messages in chat.
2. Press **R** to select the latest stored message.
3. Press **J** to cycle to another available target, or **B** to cancel.
4. Type your response normally and send it. Pingback composes the contextual text for you.
5. Press **K** to enable or disable Pingback.

Example format:

```text
@Alex: [Can you bring food?] ---> Sure, I'll bring some.
```

The exact output depends on your settings and the original chat format. The default keybinds can be changed in **Options → Controls**.

### Client commands

| Command | Purpose |
|---|---|
| `/pingback` | Show enabled state and stored-message count |
| `/pingback status` | Show status, quote length, and stored-message count |
| `/pingback toggle` | Toggle Pingback |
| `/pingback config` | Request the configuration screen (YACL required) |
| `/pingback reload` | Reload the JSON configuration |
| `/pingback reply last` | Select the latest stored message |

## Configuration

File: `.minecraft/config/pingback.json` (the exact game directory depends on your launcher/instance).

The file is created automatically. You can use the config screen when available or edit the JSON directly.

| Setting | Default | Purpose |
|---|---|---|
| `enabled` | `true` | Enable or disable Pingback |
| `quoteLength` | `26` | Maximum retained quote length (clamped to 8–40) |
| `maxMessageLength` | `250` | Target maximum generated message length (clamped to 40–250) |
| `quoteBox` | `true` | Wrap the quote in delimiters |
| `quoteStyle` | `BRACKET` | `[]`, `«»`, `()`, or no delimiters |
| `arrow` | `--->` | Separator between quote and response |
| `includeSenderInQuote` | `true` | Include `@PlayerName:` in the generated prefix |
| `mentionMode` | `LEADING_ONLY` | `OFF`, `LEADING_ONLY`, `ALL_MENTIONS`, or `PRESERVE` |
| `onlyRealPlayers` | `true` | Restrict mention stripping to recognized players |
| `stripNamePrefixes` | `true` | Enable cleanup of selected name/rank prefixes |
| `indicatorMode` | `ACTIONBAR` | `OFF`, `ACTIONBAR`, or `HUD` |
| `debug` | `false` | Enable extra logging to `logs/pingback-debug.log` |

If you edit the JSON manually, keep it valid. With `debug` enabled, reproduce the issue and check `logs/pingback-debug.log` alongside the normal Minecraft log.

## Compatibility & limitations

- **Client-side only:** Pingback does not require a Pingback mod on the server. It modifies outgoing chat text locally; it does not create native Minecraft reply metadata.
- **Mentions are text:** an `@PlayerName` prefix is not guaranteed to trigger a notification on the server or chat platform.
- **Server rules still apply:** servers may reject messages due to length limits, chat filters, moderation, or custom chat systems. The configured length limit cannot guarantee acceptance everywhere.
- **Chat formats vary:** quote and prefix cleanup is pattern-based. Custom server formatting and third-party chat mods may produce imperfect quotes or leave prefixes untouched.
- **Local history only:** reply targets come from messages observed during the current client session; Pingback does not retrieve older chat history.
- **Testing scope:** the project targets Minecraft 1.21.4 with Fabric and Java 21. Compatibility with every server, mod combination, launcher, and custom chat format has **not** been verified. Treat those combinations as untested unless confirmed separately.

## Build from source

Requires Java 21 and internet access for Gradle dependencies.

```bash
git clone https://github.com/iKiuox/Pingback-.git
cd Pingback-
./gradlew build
```

On Windows, run `gradlew.bat build`. The built JAR is normally placed in `build/libs/`.

This README documents the project configuration and source behavior; it is not a claim that every runtime environment or build configuration has been tested.

## Bugs and contributions

Before opening an issue, include your Minecraft version, Fabric Loader version, Pingback version, reproduction steps, and relevant log errors. Do not assume an issue is specific to Pingback until it can be reproduced with the relevant details.

[Report a bug or request a feature](https://github.com/iKiuox/Pingback-/issues)

## License

Pingback is licensed under the [MIT License](LICENSE).

---

<div align="center">

Made by [Kiuox](https://github.com/iKiuox) · [Source code](https://github.com/iKiuox/Pingback-)

</div>
