/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.ticktacktoe.core;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import pl.michal.szymanski.ticktacktoe.core.PlaySettings.PlaySettingsSetters;
import pl.michal.szymanski.ticktacktoe.core.model.Board;
import pl.michal.szymanski.ticktacktoe.core.model.BoardField;
import pl.michal.szymanski.ticktacktoe.core.model.GameMaster;
import pl.michal.szymanski.ticktacktoe.core.model.Move;
import pl.michal.szymanski.ticktacktoe.core.model.Player;
import pl.michal.szymanski.ticktacktoe.core.model.Point;
import pl.michal.szymanski.ticktacktoe.transport.GameWatcher;
import pl.michal.szymanski.ticktacktoe.transport.Participant;
import pl.michal.szymanski.ticktacktoe.transport.MultiplayerParticipant;

/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public abstract class Play<T extends Participant> extends PlayBase<T> {

    private Board board = new Board(3);
    private PlayersPair<T> players = new PlayersPair();
    private LinkedBlockingDeque<Move> moves = new LinkedBlockingDeque(9);
    private LinkedBlockingDeque<GameWatcher> watchers = new LinkedBlockingDeque(100);
    private PlaySettings limits = new PlaySettings();
    private Optional<Player> winner = Optional.empty();

    protected Board getBoard() {
        return this.board;
    }

    protected PlayersPair<T> players() {
        return this.players;
    }

    public void watch(GameWatcher watcher) {
        this.watchers.add(watcher);
    }

    private boolean isDone() {
        return GameMaster.isDone(board);
    }

    public PlaySettingsSetters settings() {
        return this.limits.setters();
    }

    public void begin() {
        onStart();
        while (!isDone()) {
            Player<T> player = this.moves.isEmpty() ? getRandomPlayer() : getNextPlayer();
            doTurn(player, limits.getters().getTurnLimit());
            sendBoard();
        }
        onFinish();
    }

    public Optional<Player> getWinner() {
        return winner;
    }

    private void sendBoard() {
        watchers.stream().forEach((el) -> el.receiveBoard(board));
    }

    private void doTurn(Player<T> player, long timeout) {
        Move move = null;
        Stopwatch stopwatch = Stopwatch.createStarted();

        while (stopwatch.elapsed(TimeUnit.SECONDS) < timeout) {
            Point field = player.connector().getMoveField();
            move = new Move(player, field);

            if (move != null && GameMaster.isValidMove(move)) {
                board.doMove(move);
                this.moves.addLast(move);
                break;
            }
        }
    }

    @Override
    protected  void onStart() {
        this.watchers.add((GameWatcher) this.players.firstPlayer().get().connector());
        this.watchers.add((GameWatcher) this.players.secondPlayer().get().connector());
    }

    @Override
    protected void onFinish() {
        this.winner = GameMaster.getWinner(board);
        watchers.stream().forEach((el) -> el.onGameEnd(this));
    }

    private Player<T> getRandomPlayer() {
        boolean isThatFirst = new Random().nextBoolean();
        return isThatFirst ? this.players.firstPlayer().get() : this.players.secondPlayer().get();
    }

    private Player<T> getNextPlayer() {
        Move lastMove = moves.getLast();
        return lastMove.getInvoker().get().equals(players.firstPlayer().get()) ? players.secondPlayer().get() : players.firstPlayer().get();
    }
}
