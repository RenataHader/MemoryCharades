package org.example;

import java.util.*;
import java.util.concurrent.*;

public class CharadesGameController {
    private final CharadesGame game;
    private ScheduledExecutorService timer;

    private boolean drawingPhase;
    private long phaseStartTime;

    public CharadesGameController(CharadesGame game) {
        this.game = game;
    }


    public void assignPhrasesToPlayers() {
        Map<Player, String> assigned = new HashMap<>();
        List<String> pool = game.getPhrases();
        int baseIndex = (game.getCurrentRound() - 1) * game.getPlayers().size();

        for (int i = 0; i < game.getPlayers().size(); i++) {
            assigned.put(game.getPlayers().get(i), pool.get(baseIndex + i));
        }
        game.setCurrentPhrases(assigned);
    }

    public void startDrawingPhase() {
        drawingPhase = true;
        phaseStartTime = System.currentTimeMillis();
        System.out.println("Faza rysowania rozpoczęta.");
        schedulePhaseSwitch();
    }

    public void startGuessingPhase() {
        drawingPhase = false;
        phaseStartTime = System.currentTimeMillis();
        System.out.println("Faza zgadywania rozpoczęta.");
        schedulePhaseSwitch();
    }

    private void schedulePhaseSwitch() {
        if (timer != null && !timer.isShutdown()) {
            timer.shutdownNow();
        }
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.schedule(() -> {
            if (drawingPhase) {
                startGuessingPhase();
            } else {
                evaluateGuesses(new HashMap<>());
                if (!game.isGameOver()) {
                    game.incrementRound();
                    assignPhrasesToPlayers();
                    startDrawingPhase();
                } else {
                    System.out.println("Gra zakończona!");
                    timer.shutdown();
                }
            }
        }, 60, TimeUnit.SECONDS);
    }

    public void evaluateGuesses(Map<Player, Map<Player, Boolean>> guesses) {
        for (Map.Entry<Player, Map<Player, Boolean>> entry : guesses.entrySet()) {
            Player guesser = entry.getKey();
            for (Map.Entry<Player, Boolean> result : entry.getValue().entrySet()) {
                if (result.getValue()) {
                    guesser.increaseScore(1);
                }
            }
        }
    }

    public long getRemainingTimeSeconds() {
        long elapsed = (System.currentTimeMillis() - phaseStartTime) / 1000;
        return Math.max(0, 60 - elapsed);
    }

    public boolean isDrawingPhase() {
        return drawingPhase;
    }

    public CharadesGame getGame() {
        return game;
    }
}
