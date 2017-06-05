/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.michal.szymanski.tictactoe.play;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import pl.michal.szymanski.tictactoe.model.Board;
import pl.michal.szymanski.tictactoe.model.BoardField;
import pl.michal.szymanski.tictactoe.model.Move;
import pl.michal.szymanski.tictactoe.model.Player;
import pl.michal.szymanski.tictactoe.model.Point;


/**
 *
 * @author Michał Szymański, kontakt: michal.szymanski.aajar@gmail.com
 */
public class GameMaster {

    public static boolean isDone(Board board) {
        return !getWinner(board).isEmpty();
    }

    public static List<Player> getWinner(Board board) {
        List<BoardField[]> possibleLines = board.getSelector().getAllPossibleWinningLines();
        Set<Player> players = findAllPlayersOnBoard(board);

        if (players.isEmpty()) {
            return new ArrayList();
        }

        List<Player> winners = lookForWinners(possibleLines, players);
        if (winners.size() == 0 && possibleLines.stream().allMatch(line -> Stream.of(line).allMatch(field -> field.getOwner().isPresent()))) {
            winners.addAll(players);
        }
        return winners;
    }

    private static List<Player> lookForWinners(List<BoardField[]> lines, Set<Player> players) {
        return players.stream().filter(player
                -> lines
                        .stream()
                        .anyMatch(line -> Stream.of(line)
                        .allMatch(field -> field.getOwner().isPresent() && field.getOwner().get().equals(player)))
        ).collect(Collectors.toList());
    }

    public static List<BoardField[]> getWinCombinations(List<BoardField[]> lines) {
        return lines.stream()
                .filter(el -> areLineOwnedOnlyByOnePlayer(el)).collect(Collectors.toList());
    }

    public static boolean areLineOwnedOnlyByOnePlayer(BoardField[] line) {

        Player p = null;
        int matches = 0;
        for (BoardField f : line) {
            if (f.getOwner().isPresent()) {
                if (p == null) {
                    p = f.getOwner().get();
                    matches++;
                } else if (!(f.getOwner().get().equals(p))) {
                    return false;
                } else {
                    matches++;
                }
            }
        }
        return matches == line.length;

    }

    private static Set<Player> findAllPlayersOnBoard(Board board) {
        return board.getSelector().getAllFields().stream().map(el -> el.getOwner().orElse(null)).filter(el -> el != null).collect(Collectors.toSet());
    }

    public static boolean isValidMove(Move move, Board board) {
        Point<Integer> field = move.getPoint();
        return !board.getBoard()[field.getY()][field.getX()].getOwner().isPresent();
    }

}