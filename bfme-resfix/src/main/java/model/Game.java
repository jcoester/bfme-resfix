package model;

import java.nio.file.Path;
import java.util.Objects;

public class Game {
    private GameID id;
    private String language;
    private Path installationPath;
    private Path userDataPath;
    private Version versionInstalled;
    private Version versionAvailablePatch;
    private Resolution inGameResolution;
    private Maps maps;
    private HUD hud;
    private DVD dvd;
    private Intro intro;
    private boolean isInstalled;
    private boolean isPatched;
    private boolean isRunning;

    public Game() {
    }

    public GameID getId() {
        return id;
    }

    public void setId(GameID id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Path getInstallationPath() {
        return installationPath;
    }

    public void setInstallationPath(Path installationPath) {
        this.installationPath = installationPath;
    }

    public Path getUserDataPath() {
        return userDataPath;
    }

    public void setUserDataPath(Path userDataPath) {
        this.userDataPath = userDataPath;
    }

    public Version getVersionInstalled() {
        return versionInstalled;
    }

    public void setVersionInstalled(Version versionInstalled) {
        this.versionInstalled = versionInstalled;
    }

    public Version getVersionAvailablePatch() {
        return versionAvailablePatch;
    }

    public void setVersionAvailablePatch(Version versionAvailablePatch) {
        this.versionAvailablePatch = versionAvailablePatch;
    }

    public Resolution getInGameResolution() {
        return inGameResolution;
    }

    public void setInGameResolution(Resolution inGameResolution) {
        this.inGameResolution = inGameResolution;
    }

    public Maps getMaps() {
        return maps;
    }

    public void setMaps(Maps maps) {
        this.maps = maps;
    }

    public HUD getHud() {
        return hud;
    }

    public void setHud(HUD hud) {
        this.hud = hud;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public boolean isPatched() {
        return isPatched;
    }

    public void setPatched(boolean patched) {
        isPatched = patched;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public DVD getDvd() {
        return dvd;
    }

    public void setDvd(DVD dvd) {
        this.dvd = dvd;
    }

    public Intro getIntro() {
        return intro;
    }

    public void setIntro(Intro intro) {
        this.intro = intro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return isInstalled == game.isInstalled && isPatched == game.isPatched && isRunning == game.isRunning && id == game.id && Objects.equals(language, game.language) && Objects.equals(installationPath, game.installationPath) && Objects.equals(userDataPath, game.userDataPath) && Objects.equals(versionInstalled, game.versionInstalled) && Objects.equals(versionAvailablePatch, game.versionAvailablePatch) && Objects.equals(inGameResolution, game.inGameResolution) && Objects.equals(maps, game.maps) && Objects.equals(hud, game.hud) && Objects.equals(dvd, game.dvd) && Objects.equals(intro, game.intro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, language, installationPath, userDataPath, versionInstalled, versionAvailablePatch, inGameResolution, maps, hud, dvd, intro, isInstalled, isPatched, isRunning);
    }
}
